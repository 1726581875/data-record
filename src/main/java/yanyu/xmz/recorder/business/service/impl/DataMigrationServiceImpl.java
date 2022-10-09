package yanyu.xmz.recorder.business.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import yanyu.xmz.recorder.business.dao.BaseDAO;
import yanyu.xmz.recorder.business.dao.MysqlBaseDAO;
import yanyu.xmz.recorder.business.enums.StateEnum;
import yanyu.xmz.recorder.business.model.entity.SysDataSource;
import yanyu.xmz.recorder.business.model.entity.SysDataSyncRecord;
import yanyu.xmz.recorder.business.model.entity.SysTenantTable;
import yanyu.xmz.recorder.business.model.dto.DataMigrationDTO;
import yanyu.xmz.recorder.business.service.DataMigrationService;
import yanyu.xmz.recorder.business.service.DataSourceService;
import yanyu.xmz.recorder.business.service.dm.MysqlDataMigration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2022/9/14
 */
@Service
public class DataMigrationServiceImpl implements DataMigrationService {


    @Autowired
    private DataSourceService dataSourceService;


    private static final Logger log = LoggerFactory.getLogger(DataMigrationServiceImpl.class);


    @Autowired
    private ThreadPoolTaskExecutor dataMigrationThreadPoolExecutor;

    @Override
    public void doDataMigration(DataMigrationDTO dto) {
        dataMigrationThreadPoolExecutor.execute(() -> {
                    SysDataSyncRecord syncRecord = new SysDataSyncRecord();
                    try {
                        // 插入一条同步记录
                        syncRecord = saveSyncRecord(dto);

                        // 开始同步数据
                        List<String> tableNameList = getSyncTableList(dto);
                        syncTables(tableNameList, dto.getTenantId(), dto.getDataSourceId());

                        // 更新同步状态，状态:SUCCESS
                        updateSyncRecord(syncRecord, StateEnum.SUCCESS.name());
                        syncRecord.setSyncStatus(StateEnum.SUCCESS.name());
                        BaseDAO.mysqlInstance().updateById(syncRecord);
                    } catch (Exception e) {
                        log.error("备份数据失败", e);
                        // 更新同步状态，状态:FAIL
                        syncRecord.setSyncStatus(StateEnum.FAIL.name());
                        BaseDAO.mysqlInstance().updateById(syncRecord);
                    }
                }
        );
    }


    private SysDataSyncRecord saveSyncRecord(DataMigrationDTO dto) {
        SysDataSyncRecord syncRecord = new SysDataSyncRecord();
        syncRecord.setTenantId(dto.getTenantId());
        syncRecord.setDataSourceId(dto.getDataSourceId());
        if (StringUtils.hasLength(dto.getTableName())) {
            // 设置同步表名
            syncRecord.setSourceTableName(dto.getTableName());
        } else {
            // 设置数据库名称
            SysDataSource dataSource = dataSourceService.getDataSource(dto.getTenantId(), dto.getDataSourceId());
            syncRecord.setDbName(dataSource.getSchemaName());
        }
        // 插入一条同步记录，状态:RUNNING
        syncRecord.setSyncStatus(StateEnum.RUNNING.name());
        Long insertReturnKey = BaseDAO.mysqlInstance().insertReturnKey(syncRecord);
        // 设置id
        syncRecord.setId(insertReturnKey);
        return syncRecord;
    }

    private void updateSyncRecord(SysDataSyncRecord syncRecord, String syncStatus) {
        syncRecord.setSyncStatus(syncStatus);
        BaseDAO.mysqlInstance().updateById(syncRecord);
    }


    private List<String> getSyncTableList(DataMigrationDTO dto) {
        // 判断是否仅仅同步单个表
        if (StringUtils.hasLength(dto.getTableName())) {
            return Arrays.asList(dto.getTableName());
        } else {
            // tableName参数为空，则同步该数据库下的所有表
            SysDataSource dataSource = dataSourceService.getDataSource(dto.getTenantId(), dto.getDataSourceId());
            // 获取数据库下的所有表名
            BaseDAO netBaseDAO = new MysqlBaseDAO(dataSource.getConfig());
            return netBaseDAO.getList("show tables from `" + dataSource.getSchemaName() + "`", String.class);
        }
    }



    private void syncTables(List<String> tableNameList, String tenantId, Long dataSourceId) {

        if(tableNameList == null || tableNameList.size() == 0) {
            log.info("同步数据结束，tableNameList为空,tenantId={},dataSourceId={}", tenantId, dataSourceId);
            return;
        }

        BaseDAO baseDAO = BaseDAO.mysqlInstance();
        SysDataSource dataSource = dataSourceService.getDataSource(tenantId, dataSourceId);

        BaseDAO netBaseDAO = new MysqlBaseDAO(dataSource.getConfig());
        List<SysTenantTable> sysTenantTableList = new ArrayList<>(tableNameList.size());

        long beginTime = System.currentTimeMillis();
        log.info("===== 全部表，开始同步，数量={} =====",tableNameList.size());
        for (String tableName : tableNameList) {
            long startTime = System.currentTimeMillis();
            log.info("===== 开始同步表 {} =====", tableName);

            String suffix = getSuffix(dataSourceId, tenantId);
            String targetTableName = tableName + suffix;

            // 执行同步程序，同步数据到临时表
            MysqlDataMigration mysqlDataMigration = new MysqlDataMigration(netBaseDAO, baseDAO);
            mysqlDataMigration.syncTable(tableName, suffix + "_tmp");

            // 删除旧表
            dropOldTableIfExists(targetTableName, tenantId, dataSourceId);

            // 临时表重命名为主表
            String temTableName = tableName + suffix + "_tmp";
            baseDAO.exec("ALTER TABLE `"+ temTableName  +"` RENAME TO `"  + targetTableName + "`");

            // 记录到租户关联表
            SysTenantTable tenantTable = new SysTenantTable();
            tenantTable.setDataSourceId(dataSourceId);
            tenantTable.setTenantId(tenantId);
            tenantTable.setTableName(tableName + suffix);
            tenantTable.setSourceTableName(tableName);
            sysTenantTableList.add(tenantTable);

            log.info("===== 表 {} 同步结束.. [end] 耗时={}s =====", tableName, (System.currentTimeMillis() - startTime) / 1000);
        }

        log.info("===== 全部表，结束同步，耗时={}s =====",(System.currentTimeMillis() - beginTime) / 1000);

        if(sysTenantTableList.size() > 0){
            baseDAO.batchInsert(sysTenantTableList);
        }

    }


    private void dropOldTableIfExists(String targetTableName, String tenantId, Long dataSourceId) {
        BaseDAO baseDAO = BaseDAO.mysqlInstance();
        // 本地如果存在该表，则先删除

        baseDAO.exec("DROP TABLE IF EXISTS `"+ targetTableName  +"`");
        // 如果存在租户和该表关系，也需要先删除
        SysTenantTable sysTable = baseDAO.getOne("select * from sys_tenant_table where table_name=? and tenant_id=? and data_source_id=?",
                SysTenantTable.class, targetTableName, tenantId, dataSourceId);
        if(sysTable != null){
            baseDAO.deleteById(SysTenantTable.class, sysTable.getId());
        }
    }



    private String getSuffix(Long dataSourceId, String tenantId) {
        return "_" + dataSourceId + "_" + tenantId;
    }

}

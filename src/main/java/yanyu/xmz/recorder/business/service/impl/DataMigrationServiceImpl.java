package yanyu.xmz.recorder.business.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import yanyu.xmz.recorder.business.dao.BaseDAO;
import yanyu.xmz.recorder.business.dao.MysqlBaseDAO;
import yanyu.xmz.recorder.business.model.entity.SysDataSource;
import yanyu.xmz.recorder.business.model.entity.SysTenantTable;
import yanyu.xmz.recorder.business.model.sys.DataMigrationDTO;
import yanyu.xmz.recorder.business.service.DataMigrationService;
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

    private static final Logger log = LoggerFactory.getLogger(DataMigrationServiceImpl.class);

    @Override
    public void doDataMigration(DataMigrationDTO dto) {
        if (StringUtils.hasLength(dto.getSchemaName())) {
            BaseDAO netBaseDAO = getBaseDAO(dto.getTenantId(), dto.getDataSourceId());
            List<String> tableNameList = netBaseDAO.getList("show tables from `" + dto.getSchemaName() + "`", String.class);
            syncTables(tableNameList, dto.getTenantId(), dto.getDataSourceId());
        } else if (StringUtils.hasLength(dto.getTableName())) {
            syncTables(Arrays.asList(dto.getTableName()), dto.getTenantId(), dto.getDataSourceId());
        }
    }

    private BaseDAO getBaseDAO(String tenantId, Long dataSourceId) {
        BaseDAO baseDAO = BaseDAO.mysqlInstance();
        SysDataSource dataSource = baseDAO.getOne("select * from sys_data_source where tenant_id = ? and id = ?",
                SysDataSource.class, tenantId, dataSourceId);

        if(dataSource == null) {
            throw new RuntimeException("租户数据源不存在,tenantId=" + tenantId + ",dataSourceId=" + dataSourceId);
        }
        return new MysqlBaseDAO(dataSource.getConfig());
    }


    private void syncTables(List<String> tableNameList, String tenantId, Long dataSourceId) {

        if(tableNameList == null || tableNameList.size() == 0) {
            log.info("同步数据结束，tableNameList为空,tenantId={},dataSourceId={}", tenantId, dataSourceId);
            return;
        }

        BaseDAO baseDAO = BaseDAO.mysqlInstance();
        SysDataSource dataSource = baseDAO.getOne("select * from sys_data_source where tenant_id = ? and id = ?",
                SysDataSource.class, tenantId, dataSourceId);

        if(dataSource == null) {
            throw new RuntimeException("租户数据源不存在,tenantId=" + tenantId + ",dataSourceId=" + dataSourceId);
        }

        BaseDAO netBaseDAO = new MysqlBaseDAO(dataSource.getConfig());
        List<SysTenantTable> sysTenantTableList = new ArrayList<>(tableNameList.size());

        long beginTime = System.currentTimeMillis();
        log.info("===== 全部表，开始同步，数量={} =====",tableNameList.size());
        for (String tableName : tableNameList) {
            long startTime = System.currentTimeMillis();
            log.info("===== 开始同步表 {} =====", tableName);

            String suffix = getSuffix(dataSourceId, tenantId);
            // 本地如果存在该表，则先删除
            String targetTableName = tableName + suffix;
            baseDAO.exec("DROP TABLE IF EXISTS `"+ targetTableName  +"`");
            // 如果存在租户和该表关系，也需要先删除
            SysTenantTable sysTable = baseDAO.getOne("select * from sys_tenant_table where table_name=? and tenant_id=? and data_source_id=?",
                    SysTenantTable.class, targetTableName, tenantId, dataSourceId);
            if(sysTable != null){
                baseDAO.deleteById(SysTenantTable.class, sysTable.getId());
            }

            // 执行同步程序
            MysqlDataMigration mysqlDataMigration = new MysqlDataMigration(netBaseDAO, baseDAO);

            mysqlDataMigration.syncTable(tableName, suffix);
            // 记录到租户关联表
            SysTenantTable tenantTable = new SysTenantTable();
            tenantTable.setDataSourceId(dataSourceId);
            tenantTable.setTenantId(tenantId);
            tenantTable.setTableName(tableName + suffix);
            sysTenantTableList.add(tenantTable);

            log.info("===== 表 {} 同步结束.. [end] 耗时={}s =====", tableName, (System.currentTimeMillis() - startTime) / 1000);
        }

        log.info("===== 全部表，结束同步，耗时={}s =====",(System.currentTimeMillis() - beginTime) / 1000);

        if(sysTenantTableList.size() > 0){
            baseDAO.batchInsert(sysTenantTableList);
        }

    }


    private String getSuffix(Long dataSourceId, String tenantId) {
        return "_" + dataSourceId + "_" + tenantId;
    }

}

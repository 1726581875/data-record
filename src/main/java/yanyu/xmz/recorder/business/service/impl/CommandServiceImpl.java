package yanyu.xmz.recorder.business.service.impl;

import com.github.shyiko.mysql.binlog.network.protocol.command.Command;
import com.github.shyiko.mysql.binlog.network.protocol.command.PingCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yanyu.xmz.recorder.business.model.dto.TenantDataSourceDTO;
import yanyu.xmz.recorder.business.model.entity.SysDataSource;
import yanyu.xmz.recorder.business.service.CommandService;
import yanyu.xmz.recorder.business.service.DataSourceService;
import yanyu.xmz.recorder.mysql.channel.ChannelManager;
import yanyu.xmz.recorder.mysql.channel.MyPacketChannel;
import yanyu.xmz.recorder.mysql.common.ResultPacket;
import yanyu.xmz.recorder.mysql.common.ResultParser;
import yanyu.xmz.recorder.mysql.protocol.CapabilityFlags;

import java.io.IOException;

/**
 * @author xiaomingzhang
 * @date 2022/10/9
 */
@Service
public class CommandServiceImpl implements CommandService {

    private static final Logger log = LoggerFactory.getLogger(CommandServiceImpl.class);

    @Autowired
    private DataSourceService dataSourceService;


    @Override
    public boolean ping(TenantDataSourceDTO dto) {
        SysDataSource dataSource = dataSourceService.getDataSource(dto.getTenantId(), dto.getDataSourceId());

        if(dataSource == null){
            throw new RuntimeException("数据源不存在,tenantId=" + dto.getTenantId() + ",dataSourceId=" + dto.getDataSourceId());
        }

        try {
            ResultPacket resultPacket = openChannelAndSendCommand(dataSource, new PingCommand());
            return "OK".equals(resultPacket.getType());
        } catch (Exception e) {
            log.error("ping命令执行失败,dataSource={}", e, dataSource);
            return false;
        }
    }


    private ResultPacket openChannelAndSendCommand(SysDataSource dataSource, Command command) {

        ChannelManager connectionChannel = new ChannelManager(dataSource.getHostname(),
                Integer.valueOf(dataSource.getServerPort()), dataSource.getUsername() , dataSource.getPassword());
        try {
            // 执行Ping命令
            return sendCommand(connectionChannel, command);
        } catch (Exception e) {
            log.error("执行command失败", e);
            throw new RuntimeException("执行command失败");
        } finally {
            try {
                connectionChannel.close();
            } catch (IOException e) {
                log.error("关闭通道失败", e);
                throw new RuntimeException("关闭通道失败");
            }
        }
    }



    private ResultPacket sendCommand(ChannelManager connectionChannel, Command command) throws IOException {
        MyPacketChannel localChannel = connectionChannel.getChanel();
        // 获取能力标记
        CapabilityFlags capabilityFlags = connectionChannel.getCapabilityFlags();
        // 执行查询命令
        localChannel.write(command);
        byte[] resultBytes = localChannel.readAll();
        // 解析返回结果
        return ResultParser.parseResultPacket(resultBytes, capabilityFlags);
    }

}

package yanyu.xmz.recorder.business.handler;

import com.github.shyiko.mysql.binlog.event.Event;
import yanyu.xmz.recorder.business.entity.event.EventRecord;

/**
 * @author xiaomingzhang
 * @date 2022/8/10
 */
public class DefaultEventHandler extends AbstractMysqlEventHandler {
    @Override
    protected EventRecord saveEventDetailToDatabase(Event event, EventRecord eventRecord) {
        return eventRecord;
    }
}

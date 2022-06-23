package yanyu.xmz.recorder.handler;

import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.XidEventData;
import yanyu.xmz.recorder.entity.EventRecord;

/**
 * @author xiaomingzhang
 * @date 2022/6/23
 */
public class XidHandler extends AbstractMysqlEventHandler {

    @Override
    protected EventRecord saveEventDetailToDatabase(Event event, EventRecord eventRecord) {
        XidEventData data = event.getData();
        return eventRecord;
    }
}

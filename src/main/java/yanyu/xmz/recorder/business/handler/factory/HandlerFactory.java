package yanyu.xmz.recorder.business.handler.factory;

import com.github.shyiko.mysql.binlog.event.*;
import yanyu.xmz.recorder.business.handler.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaomingzhang
 * @date 2022/6/23
 */
public class HandlerFactory {

    private static final Map<EventType, DbEventHandler> eventHandlerMap = new HashMap<>(16);

    static {
        eventHandlerMap.put(EventType.TABLE_MAP, new TableMapEventHandler());
        eventHandlerMap.put(EventType.EXT_WRITE_ROWS, new InsertEventHandler());
        eventHandlerMap.put(EventType.EXT_UPDATE_ROWS, new UpdateEventHandler());
        eventHandlerMap.put(EventType.EXT_DELETE_ROWS, new DeleteEventHandler());
        eventHandlerMap.put(EventType.XID, new XidHandler());
        eventHandlerMap.put(EventType.QUERY, new QueryEventHandler());
        eventHandlerMap.put(null, new DefaultEventHandler());
    }

    public static DbEventHandler getHandler(EventType eventType) {
        return eventHandlerMap.getOrDefault(eventType, eventHandlerMap.get(null));
    }




}

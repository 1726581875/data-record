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

    private static final Map<Class<?>, DbEventHandler> eventHandlerMap = new HashMap<>(16);

    static {
        eventHandlerMap.put(TableMapEventData.class, new TableMapEventHandler());
        eventHandlerMap.put(WriteRowsEventData.class, new InsertEventHandler());
        eventHandlerMap.put(UpdateRowsEventData.class, new UpdateEventHandler());
        eventHandlerMap.put(DeleteRowsEventData.class, new DeleteEventHandler());
        eventHandlerMap.put(XidEventData.class, new XidHandler());
    }

    public static DbEventHandler getHandler(Class<?> eventData) {
        return eventHandlerMap.get(eventData);
    }




}

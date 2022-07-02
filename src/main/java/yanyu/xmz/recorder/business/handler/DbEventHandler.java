package yanyu.xmz.recorder.business.handler;

import com.github.shyiko.mysql.binlog.event.Event;

/**
 * @author xiaomingzhang
 * @date 2022/6/14
 */
public interface DbEventHandler {
    /**
     * 保存数据操作记录
     * @param startPos
     * @param event
     */
    void saveEvent(Long startPos, Event event);

}

package cn.whl.graphiteSinker.util;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Priority;

/**
 * Created by didi on 2017/6/22.
 */
public class GraphiteSinkerLogAppender extends DailyRollingFileAppender {


    public boolean isAsSevereAsThreshold(Priority priority) {
        //只判断是否相等，而不判断优先级
        return this.getThreshold().equals(priority);
    }
}

package com.forcs.eformbot.common.log.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import com.forcs.eformbot.common.log.LogMarker;
import org.slf4j.Marker;

public class CustomRollingFileAppender<E> extends RollingFileAppender<E> {
    private String category;
    private Marker logMarker;
    private boolean isAppend = false;

    @Override
    protected void append(E eventObject) {
        if(!isAppend(eventObject)) {
            return;
        }

        super.append(eventObject);
    }

    private boolean isAppend(E eventObject) {
        if(!this.isAppend) {
            return false;
        }

        boolean result = false;

        ILoggingEvent loggingEvent = (ILoggingEvent) eventObject;
        Marker marker = loggingEvent.getMarker();

        if(marker == null) { // log marker가 정해지지 않은 경우 SYSTEM으로 처리
            marker = LogMarker.SYSTEM;
        }

        // 출력할 marker와 this의 marker가 같은 경우 출력
        if(marker.equals(this.logMarker)) {
            result = true;
        }

        return result;
    }

    public void setCategory(String category) {
        this.category = category;

        this.logMarker = LogMarker.getLogMaker(this.category);
        if(this.logMarker.equals(LogMarker.SYSTEM)
                || this.logMarker.equals(LogMarker.USER)
                || this.logMarker.equals(LogMarker.MATCHED)
                || this.logMarker.equals(LogMarker.NOT_MATCHED)) {
            this.isAppend = true;
        }
    }
}

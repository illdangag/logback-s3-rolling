package com.forcs.eformbot.common.log.converter;

import ch.qos.logback.classic.pattern.NamedConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.forcs.eformbot.common.log.LogMarker;
import org.slf4j.Marker;

public class CategoryConverter extends NamedConverter {
    @Override
    protected String getFullyQualifiedName(ILoggingEvent event) {
        Marker marker = event.getMarker();

        if(marker == null ||
                (!marker.equals(LogMarker.SYSTEM)
                        && !marker.equals(LogMarker.USER)
                        && !marker.equals(LogMarker.MATCHED)
                        && !marker.equals(LogMarker.NOT_MATCHED))) {
            marker = LogMarker.SYSTEM;
        }

        return marker.getName();
    }
}

package com.forcs.eformbot.common.log.converter;

import ch.qos.logback.classic.pattern.NamedConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class CallerConverter extends NamedConverter {
    @Override
    protected String getFullyQualifiedName(ILoggingEvent event) {
        if(event.getCallerData() == null) {
            return "";
        }

        StackTraceElement trace = getStackTraceElement(event.getCallerData());
        StringBuilder sb = new StringBuilder();

        if(isFileLoggerEvent(event)) {
            sb.append(trace.getClassName())
                    .append(".")
                    .append(trace.getMethodName())
                    .append(":")
                    .append(trace.getLineNumber());
        } else {
            sb.append("(")
                    .append(trace.getFileName())
                    .append(":")
                    .append(trace.getLineNumber())
                    .append(")");
        }

        return sb.toString();
    }

    private StackTraceElement getStackTraceElement(StackTraceElement[] stackTraceElements) {
        return stackTraceElements[0];
    }

    private boolean isFileLoggerEvent(ILoggingEvent event) {
        String loggerName = event.getLoggerName();
        boolean result;

        if(loggerName == null || loggerName.equals("")) {
            result = false;
        } else if(loggerName.startsWith("FILE_") || loggerName.startsWith("S3_")) {
            result = true;
        } else {
            result = false;
        }

        return result;
    }
}

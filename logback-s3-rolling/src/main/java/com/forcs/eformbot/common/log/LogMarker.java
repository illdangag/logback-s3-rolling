package com.forcs.eformbot.common.log;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class LogMarker {
    private LogMarker() {}
    public static final Marker SYSTEM = MarkerFactory.getMarker("SYS");
    public static final Marker USER = MarkerFactory.getMarker("USR");
    public static final Marker MATCHED = MarkerFactory.getMarker("MATCHED");
    public static final Marker NOT_MATCHED = MarkerFactory.getMarker("NOT_MATCHED");

    public static Marker getLogMaker(String name) {
        Marker result;
        if(name.equals("system") || name.equals("SYS")) {
            result = SYSTEM;
        } else if(name.equals("user") || name.equals("USR")) {
            result = USER;
        } else if(name.equals("matched") || name.equals("MATCHED")) {
            result = MATCHED;
        } else if(name.equals("not_matched") || name.equals("NOT_MATCHED")) {
            result = NOT_MATCHED;
        } else {
            result = SYSTEM;
        }
        return result;
    }
}
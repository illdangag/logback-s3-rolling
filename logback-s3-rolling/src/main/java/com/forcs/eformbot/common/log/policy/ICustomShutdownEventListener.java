package com.forcs.eformbot.common.log.policy;

public interface ICustomShutdownEventListener {
    void shutdown(CustomSizeAndTimeBasedRollingPolicy policy);
}

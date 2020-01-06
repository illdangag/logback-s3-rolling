package com.forcs.eformbot.common.log.policy;

public interface ICustomRolloverEventListener {
    void rollover(String filePath);
}

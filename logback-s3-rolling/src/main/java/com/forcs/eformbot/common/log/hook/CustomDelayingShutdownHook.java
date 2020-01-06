package com.forcs.eformbot.common.log.hook;

import ch.qos.logback.core.hook.DelayingShutdownHook;
import com.forcs.eformbot.common.log.policy.CustomSizeAndTimeBasedRollingPolicy;
import com.forcs.eformbot.common.log.policy.ICustomShutdownEventListener;

import java.util.List;

public class CustomDelayingShutdownHook extends DelayingShutdownHook {

    @Override
    public void run() {
        super.run();
        List<CustomSizeAndTimeBasedRollingPolicy> policyList = CustomShutdownHook.getPolicyList();
        for(CustomSizeAndTimeBasedRollingPolicy policy : policyList) {
            ICustomShutdownEventListener shutDownEventListener = CustomSizeAndTimeBasedRollingPolicy.getShutdownEventListener();
            if (shutDownEventListener != null) {
                shutDownEventListener.shutdown(policy);
            }
        }
    }
}

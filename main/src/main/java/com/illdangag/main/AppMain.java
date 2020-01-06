package com.illdangag.main;

import com.forcs.eformbot.common.log.LogMarker;
import com.forcs.eformbot.common.log.policy.ICustomRolloverEventListener;
import com.forcs.eformbot.common.log.policy.CustomSizeAndTimeBasedRollingPolicy;
import com.forcs.eformbot.common.log.policy.ICustomShutdownEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppMain {
    public static final Logger logger = LoggerFactory.getLogger("");

    public static void main(String[] args) throws InterruptedException {
        ICustomRolloverEventListener rolloverEventListener = new ICustomRolloverEventListener() {
            @Override
            public void rollover(String filePath) {
                System.out.println("======== rollover - " + filePath);
            }
        };
        ICustomShutdownEventListener shutDownEventListener = new ICustomShutdownEventListener() {
            @Override
            public void shutdown(CustomSizeAndTimeBasedRollingPolicy policy) {
                System.out.println("======== shutdown - " + policy);
                policy.rollover();
            }
        };

        CustomSizeAndTimeBasedRollingPolicy.setRolloverEventListener(rolloverEventListener);
        CustomSizeAndTimeBasedRollingPolicy.setShutDownEventListener(shutDownEventListener);

        for (int i = 0; i < 30; i++) {
            Thread.sleep(200);
            logger.info(LogMarker.SYSTEM, "system log");
            logger.info(LogMarker.USER, "user log");
        }
    }
}

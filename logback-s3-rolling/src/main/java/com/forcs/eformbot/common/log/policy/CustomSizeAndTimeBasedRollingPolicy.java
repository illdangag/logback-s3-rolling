package com.forcs.eformbot.common.log.policy;

import ch.qos.logback.core.rolling.*;
import ch.qos.logback.core.util.FileSize;
import com.forcs.eformbot.common.log.hook.CustomShutdownHook;

import java.util.List;

public class CustomSizeAndTimeBasedRollingPolicy<E> extends SizeAndTimeBasedRollingPolicy<E> {

    private static ICustomRolloverEventListener rolloverEventListener = null;
    private static ICustomShutdownEventListener shutdownEventListener = null;

    @Override
    public void start() {
        super.start();
        CustomShutdownHook.addPolicy(this);
    }

    @Override
    public void rollover() {
        super.rollover();
        if (CustomSizeAndTimeBasedRollingPolicy.rolloverEventListener != null) {
            TimeBasedFileNamingAndTriggeringPolicy<E> timeBasedFileNamingAndTriggeringPolicy = this.getTimeBasedFileNamingAndTriggeringPolicy();
            String filePath = timeBasedFileNamingAndTriggeringPolicy.getElapsedPeriodsFileName();

            CustomSizeAndTimeBasedRollingPolicy.rolloverEventListener.rollover(filePath);
        }
    }

    public static void setRolloverEventListener(ICustomRolloverEventListener eventListener) {
        CustomSizeAndTimeBasedRollingPolicy.rolloverEventListener = eventListener;
    }

    public static void setShutdownEventListener(ICustomShutdownEventListener eventListener) {
        CustomSizeAndTimeBasedRollingPolicy.shutdownEventListener = eventListener;
    }

    public static ICustomShutdownEventListener getShutdownEventListener() {
        return CustomSizeAndTimeBasedRollingPolicy.shutdownEventListener;
    }

    public static void shutdown() {
        List<CustomSizeAndTimeBasedRollingPolicy> policyList = CustomShutdownHook.getPolicyList();
        for (CustomSizeAndTimeBasedRollingPolicy policy : policyList) {
            CustomSizeAndTimeBasedRollingPolicy.getShutdownEventListener().shutdown(policy);
        }
    }
}

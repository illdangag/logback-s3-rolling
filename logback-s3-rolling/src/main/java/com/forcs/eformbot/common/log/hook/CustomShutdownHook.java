package com.forcs.eformbot.common.log.hook;

import com.forcs.eformbot.common.log.policy.CustomSizeAndTimeBasedRollingPolicy;

import java.util.ArrayList;
import java.util.List;

public class CustomShutdownHook {
    private static List<CustomSizeAndTimeBasedRollingPolicy> policyList = new ArrayList<>();

    public static void addPolicy(CustomSizeAndTimeBasedRollingPolicy policy) {
        CustomShutdownHook.policyList.add(policy);
    }

    public static List<CustomSizeAndTimeBasedRollingPolicy> getPolicyList() {
        return CustomShutdownHook.policyList;
    }
}

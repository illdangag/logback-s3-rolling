package com.illdangag.servlet.context;

import com.forcs.eformbot.common.log.LogMarker;
import com.forcs.eformbot.common.log.policy.CustomSizeAndTimeBasedRollingPolicy;
import com.forcs.eformbot.common.log.policy.ICustomRolloverEventListener;
import com.forcs.eformbot.common.log.policy.ICustomShutdownEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;

public class ContextListener implements ServletContextListener {
	public static final Logger logger = LoggerFactory.getLogger("");

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println(this.getClass().getResource("").getPath());
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
                if (policy.isTriggeringEvent(new File(policy.getActiveFileName()), null)) {
                    policy.rollover();
                }
			}
		};

		CustomSizeAndTimeBasedRollingPolicy.setRolloverEventListener(rolloverEventListener);
		CustomSizeAndTimeBasedRollingPolicy.setShutDownEventListener(shutDownEventListener);

		for (int i = 0; i < 30; i++) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			logger.info(LogMarker.SYSTEM, "system log");
			logger.info(LogMarker.USER, "user log");
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("contextDestroyed");
	}
}

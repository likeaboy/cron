package org.jrocky.cron.job.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cron工具类
 * @author rocky.wang
 *
 */
public class CronUtils {
	private final static Logger logger = LoggerFactory.getLogger(CronUtils.class);
	private static final String BLANK_SPLIT = " ";
	
	public static String cronExpPreProcess(String exp){
		exp = exp.trim();
		logger.info("expresson ：" + exp +" will be process...");
		exp = exp.substring(0, exp.lastIndexOf(BLANK_SPLIT));
		return exp;
	}
}

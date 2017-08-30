package org.jrocky.cron.job.core;

import java.util.List;
/**
 * 定时任务加载器接口
 * @author rocky.wang
 *
 */
public interface ITimedTaskLoader {
	/**
	 * 加载
	 * @return
	 */
	List<TimedTask> load();
}

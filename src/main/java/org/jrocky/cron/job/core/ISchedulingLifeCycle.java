package org.jrocky.cron.job.core;


/**
 * 任务调度生命周期
 * @author rocky.wang
 *
 */
public interface ISchedulingLifeCycle {
	/**
	 * 调度
	 */
	void scheduling();
	/**
	 * 提交任务
	 * @param timedTask
	 * @return
	 */
	boolean commit(TimedTask timedTask);
	/**
	 * 取消任务
	 * @param taskId
	 * @return
	 */
	void cancel(String taskId);
	/**
	 * 唤醒取消的任务
	 * @param taskId
	 * @return
	 */
	boolean reCommit(String taskId);
}

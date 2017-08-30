package org.jrocky.cron.job.core;


/**
 * 定时任务
 * @author rocky.wang
 *
 */
public class TimedTask {
	/**
	 * 任务ID
	 */
	private String taskId;
	/**
	 * 任务类型
	 */
	private TaskType taskType;
	/**
	 * cron表达式
	 */
	private String cron;
	
	public TimedTask(String taskId, TaskType taskType,String cron) {
		this.taskId = taskId;
		this.taskType = taskType;
		this.cron = cron;
	}
	
	public TimedTask(String taskId,String cron) {
		this(taskId,TaskType.FIXED,cron);
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public TaskType getTaskType() {
		return taskType;
	}

	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}
}

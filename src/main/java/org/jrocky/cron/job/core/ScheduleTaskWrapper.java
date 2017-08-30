package org.jrocky.cron.job.core;

import org.springframework.scheduling.config.ScheduledTask;

public class ScheduleTaskWrapper {
	
	private ScheduledTask springScheduleTask;
	private TimedTask timedTask;
	private CronTaskState state;
	
	public ScheduleTaskWrapper(ScheduledTask task,TimedTask timedTask){
		this(task,timedTask,CronTaskState.RUNNING);
	}
	
	public ScheduleTaskWrapper(ScheduledTask task,TimedTask timedTask,CronTaskState state){
		this.springScheduleTask = task;
		this.timedTask = timedTask;
		this.state = state;
	}
	
	public ScheduledTask getSpringScheduleTask() {
		return springScheduleTask;
	}
	public void setSpringScheduleTask(ScheduledTask springScheduleTask) {
		this.springScheduleTask = springScheduleTask;
	}
	public CronTaskState getState() {
		return state;
	}
	public void setState(CronTaskState state) {
		this.state = state;
	}
	public TimedTask getTimedTask() {
		return timedTask;
	}

	public void setTimedTask(TimedTask timedTask) {
		this.timedTask = timedTask;
	}
	
}

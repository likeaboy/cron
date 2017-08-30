package org.jrocky.cron.job.support;

import org.jrocky.cron.job.core.TimedTask;

/**
 * 定时任务
 * @author rocky.wang
 *
 */
public class TimedTaskJob implements Runnable{

	private TimedTask timedTask;
	
	public TimedTaskJob(TimedTask timedTask){
		this.timedTask = timedTask;
	}
	
	@Override
	public void run() {
		/*TaskJob job = new TaskJob(timedTask.getTaskId(),timedTask.getTaskExeType().code);
    	AkkaClientProxy.getInstance().doProcess(new StartCommand(job));*/
	}

}

package org.jrocky.cron.job.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;

import org.jrocky.cron.job.core.TimedTask;
import org.jrocky.cron.job.support.TimedTaskManager;


public class TestJobCallable implements Callable{
	
	private TimedTask timedTask;

	public TestJobCallable(TimedTask timedTask){
		this.timedTask = timedTask;
	}

	@Override
	public Object call() throws Exception {
		/*TaskJob job = new TaskJob(timedTask.getTaskId(),timedTask.getTaskExeType().code);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		System.out.println("test job runing..." + sdf.format(new Date()));
		System.out.println("job task id=" + job.getTaskId() + " taskType : " + job.getTaskType());
		
		try {
			Thread.sleep(1000 * 65);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("moni akka proxy send msg...");
		
		return job.getTaskId()+" success";*/
		boolean isSuc = TimedTaskManager.getInstance().commit(timedTask);
		return timedTask.getTaskId() + " " + isSuc;

	}
}

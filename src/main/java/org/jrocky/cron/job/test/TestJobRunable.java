package org.jrocky.cron.job.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jrocky.cron.job.core.TimedTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 测试任务
 * @author wangzhijie
 *
 */
public class TestJobRunable implements Runnable{
	
	private final static Logger logger = LoggerFactory.getLogger(TestJobRunable.class);

	private TimedTask timedTask;
	
	public TestJobRunable(TimedTask timedTask){
		this.timedTask = timedTask;
	}
	
	@Override
	public void run() {
//		TaskJob job = new TaskJob(timedTask.getTaskId(),timedTask.getTaskExeType().code);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		logger.info("test job runing... moni akka proxy send msg..." + sdf.format(new Date()) + " " +timedTask.getTaskId());
//		System.out.println("job task id=" + job.getTaskId() + " taskType : " + job.getTaskType());
		
		/*try {
			Thread.sleep(1000 * 65);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		logger.info("moni akka proxy send msg...");
		
//    	AkkaClientProxy.getInstance().doProcess(new StartCommand(job));
	}

}

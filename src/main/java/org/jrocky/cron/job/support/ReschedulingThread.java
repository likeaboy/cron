package org.jrocky.cron.job.support;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.print.attribute.standard.SheetCollate;

import org.jrocky.cron.job.core.ScheduleTaskWrapper;
import org.jrocky.cron.job.core.TimedTask;
import org.jrocky.cron.job.test.TestJobRunable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

/**
 * 任务调度线程
 * @author rocky.wang
 *
 */
public class ReschedulingThread implements Runnable{
	
	private final static Logger logger = LoggerFactory.getLogger(ReschedulingThread.class);

	private BlockingQueue<TimedTask> timedTaskQueue;
	
	private ScheduledTaskRegistrar registrar;
	
	private Map<String,ScheduleTaskWrapper> cronTaskMap;
	
	private List<TimedTask> consumed = new LinkedList<>();
	
	public ReschedulingThread(BlockingQueue<TimedTask> timedTaskQueue,ScheduledTaskRegistrar registrar,Map<String,ScheduleTaskWrapper> cronTaskMap){
		this.timedTaskQueue = timedTaskQueue;
		this.registrar = registrar;
		this.cronTaskMap = cronTaskMap;
	}
	
	
	@Override
	public void run() {
		while(true){
			try {
				TimedTask t = timedTaskQueue.take();
				logger.info("take timedTask" + t.getTaskId());
				ScheduledTask scheTask = this.registrar.scheduleCronTask(new CronTask(new TestJobRunable(t), new CronTrigger(CronUtils.cronExpPreProcess(t.getCron()))));
				logger.info("do schedule cron task...");
				ScheduleTaskWrapper taskWrapper = new ScheduleTaskWrapper(scheTask,t);
				cronTaskMap.put(t.getTaskId(), taskWrapper);
				consumed.add(t);
			} catch (InterruptedException e) {
				logger.error("",e);
			}
		}
	}

}

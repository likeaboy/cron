package org.jrocky.cron.job.test;

import java.util.Map;

import org.jrocky.cron.job.core.ScheduleTaskWrapper;
import org.jrocky.cron.job.support.CronUtils;
import org.jrocky.cron.job.support.TimedTaskManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 测试取消以及唤醒任务
 * @author rocky.wang
 *
 */
public class TestRemoveAndRecommitRunnable implements Runnable{
	
	private final static Logger logger = LoggerFactory.getLogger(CronUtils.class);
	
	private Map<String,ScheduleTaskWrapper> cronTaskMap;
	
	public TestRemoveAndRecommitRunnable(Map<String,ScheduleTaskWrapper> cronTaskMap){
		this.cronTaskMap = cronTaskMap;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int loop = 0;
		while(true){
			try {
				loop ++;
				Thread.sleep(1000*10);
				print();
				if(loop == 6){
					TimedTaskManager.getInstance().cancel("LAB0");
					logger.info("cancel task id:LAB0");
				}
				if(loop == 18){
					TimedTaskManager.getInstance().reCommit("LAB0");
					logger.info("recommit task id:LAB0");
				}
			} catch (InterruptedException e) {
				logger.error("",e);
			}
		}
	}
	
	private void print(){
		for(String taskId : cronTaskMap.keySet()){
			logger.info("taskId : " + taskId + " state : " + cronTaskMap.get(taskId).getState());
		}
	}

}

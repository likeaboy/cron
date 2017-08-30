package org.jrocky.cron.job.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.jrocky.cron.job.core.TaskType;
import org.jrocky.cron.job.core.TimedTask;
import org.jrocky.cron.job.support.TimedTaskManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 测试类
 * @author rocky.wang
 *
 */
public class TestRunner {
	
	private final static Logger logger = LoggerFactory.getLogger(TestRunner.class);
	
	private static List<TimedTask> createTestData(){
		List<TimedTask> tasks = new ArrayList<TimedTask>();
		/*for(int i =0;i <3; i++){
			
		}*/
		
		TimedTask t1 = new TimedTask("LAB0",TaskType.FIXED,"0 0/1 * * * * *");
		tasks.add(t1);
		TimedTask t2 = new TimedTask("LAB1",TaskType.FIXED,"0 0/1 * * * * *");
		tasks.add(t2);
		TimedTask t3 = new TimedTask("LAB2",TaskType.FIXED,"0 0/1 * * * * *");
		tasks.add(t3);
		
		return tasks;
	}
	
	public static void main(String[] args) {
		List<TimedTask> tasks = createTestData();
		int taskSize = 6;
		ExecutorService threadPool =  Executors.newFixedThreadPool(taskSize);
		// 创建多个有返回值的任务  
		   List<Future> list = new ArrayList<Future>();  
		   for (int i = 0; i < tasks.size(); i++) {  
		    Callable c = new TestJobCallable(tasks.get(i));  
		    // 执行任务并获取Future对象  
		    Future f = threadPool.submit(c);  
		    // System.out.println(">>>" + f.get().toString());  
		    list.add(f);  
		   }  
		   
		   for(Future f : list){
			   try {
				   logger.info(f.get().toString());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		   }
		   // 关闭线程池  
//		   threadPool.shutdown();  
		
		   Thread t = new Thread(new TestRemoveAndRecommitRunnable(TimedTaskManager.getInstance().getMap()));
		   t.start();
		/*TimedTask t = new TimedTask("L111111111111111",TaskConstant.TaskType.STRUCTURED,TaskConstant.TaskExeType.FIXEDTIME,"0 0/1 * * * * *");
		TimedTask t2 = new TimedTask("L222222222222222",TaskConstant.TaskType.STRUCTURED,TaskConstant.TaskExeType.FIXEDTIME,"0 0/1 * * * * *");
		TimedTask t3 = new TimedTask("Q3333333333333333",TaskConstant.TaskType.STRUCTURED,TaskConstant.TaskExeType.FIXEDTIME,"0 0/1 * * * * *");
		TimedTaskManager.getInstance().commit(t);
		TimedTaskManager.getInstance().commit(t2);
		TimedTaskManager.getInstance().commit(t3);*/
		while(true){
			//idle
		}
	}
	
/*	private long getMinutesPeriod(String expression){
//		String expression = "0 0 12 1/1 * ? *";
//		expression = "1 * *  * * ? ";
		expression = "0 0/1 * * * * *";
		expression = expression.substring(0, expression.lastIndexOf(BLANK_SPLIT));
		System.out.println(expression);
		CronSequenceGenerator cronParser = new CronSequenceGenerator(expression);
		
		Date nextDate = cronParser.next(new Date());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		System.out.println(sdf.format(nextDate));
		return 1l;
	}*/
}

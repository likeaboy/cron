package org.jrocky.cron.job.support;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jrocky.cron.job.core.CronTaskState;
import org.jrocky.cron.job.core.ISchedulingLifeCycle;
import org.jrocky.cron.job.core.ITimedTaskLoader;
import org.jrocky.cron.job.core.ScheduleTaskWrapper;
import org.jrocky.cron.job.core.TaskType;
import org.jrocky.cron.job.core.TimedTask;
import org.jrocky.cron.job.test.TestJobCallable;
import org.jrocky.cron.job.test.TestRemoveAndRecommitRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.scheduling.support.CronTrigger;

/**
 * 定时任务管理服务
 * @author rocky.wang
 *
 */
public class TimedTaskManager implements ISchedulingLifeCycle{
	
	private final static Logger logger = LoggerFactory.getLogger(TimedTaskManager.class);
	/**
	 * 定时任务管理服务单例
	 */
	private volatile static TimedTaskManager instance = new TimedTaskManager();
	/**
	 * Spring 任务调度注册器
	 */
	private ScheduledTaskRegistrar registrar = new ScheduledTaskRegistrar();
	/**
	 * 定时任务加载器
	 */
	private ITimedTaskLoader timedTaskLoader = new TimedTaskLoader();
	/**
	 * 定时任务队列，缓存提交的定时任务数据
	 */
	private BlockingQueue<TimedTask> timedTaskQueue = new ArrayBlockingQueue<TimedTask>(128); 
	/**
	 * 提交的定时任务集合
	 */
//	private List<ScheduledTask> cronTasks = new ArrayList<ScheduledTask>();
	private final Map<String,ScheduleTaskWrapper> cronTaskMap = new ConcurrentHashMap<>();
	/**
	 * 取消的任务集合
	 */
	private List<ScheduleTaskWrapper> cancelList = new LinkedList<>();
	/**
	 * 定时任务启动服务
	 */
	private ReschedulingThread backend;
	/**
	 * 初始化锁
	 */
	private final Object initLock = new Object();
	private final Object cancelLock = new Object();
	/**
	 * 是否初始化状态
	 */
	private boolean isInit = false;
	/**
	 * 加载所有数据库中的定时任务
	 */
	private boolean isLoadAllTask = false;
	
	private TimedTaskManager(){
		init();
	}
	
	public static TimedTaskManager getInstance(){
		return instance;
	}
	
	public Map<String,ScheduleTaskWrapper> getMap(){
		return cronTaskMap;
	}
	
	private void init(){
		if(!this.isInit){
			synchronized (initLock) {
				if(!this.isInit){
					ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
					scheduler.initialize();
					registrar.setTaskScheduler(scheduler);
					//初始化消费队列线程
					backend = new ReschedulingThread(timedTaskQueue,registrar,cronTaskMap);
					Thread t = new Thread(backend);
					t.start();
					this.isInit = true;
				}
			}
		}
	}
	
	private void schedulingAllTask(){
		List<TimedTask> timedTasks = timedTaskLoader.load();
		timedTasks.forEach(t -> commit(t));
		isLoadAllTask = true;
	}
	
	/**
	 * 应用启动完成后调用
	 */
	@Override
	public void scheduling() {
		if(isLoadAllTask) return;
		schedulingAllTask();
	}
	/**
	 * 提交一个定时任务到队列
	 */
	@Override
	public boolean commit(TimedTask timedTask){
		//TimeZone.getTimeZone("GMT+08:00");
		try {
			logger.info("commit timedTask : " + timedTask.getTaskId());
			//超时3秒
			return timedTaskQueue.offer(timedTask, 3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		/*synchronized (registrar) {
			registrar.scheduleCronTask(new CronTask(new TestJobRunable(timedTask), new CronTrigger(cronExpPreProcess(timedTask.getCron()))));
		}*/
	}
	
	@Override
	public void cancel(String taskId) {
		// TODO Auto-generated method stub
		synchronized (cancelLock) {
			ScheduleTaskWrapper taskWrapper = cronTaskMap.get(taskId);
			taskWrapper.getSpringScheduleTask().cancel();
			taskWrapper.setState(CronTaskState.CANCELED);
			cancelList.add(taskWrapper);
		}
	}
	
	@Override
	public boolean reCommit(String taskId) {
		// TODO Auto-generated method stub
		ScheduleTaskWrapper taskWrapper = cronTaskMap.get(taskId);
		if(taskWrapper == null)
			return false;
		if(taskWrapper.getState() == CronTaskState.RUNNING)
			return false;
		if(!cancelList.contains(taskWrapper))
			return false;
		boolean ok = commit(taskWrapper.getTimedTask());
		if(ok){
			cancelList.remove(taskWrapper);
			taskWrapper.setState(CronTaskState.RUNNING);
			return true;
		}
		return false;
	}
	
}

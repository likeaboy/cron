package org.jrocky.cron.job;

import javax.annotation.PostConstruct;

import org.jrocky.cron.job.support.TimedTaskManager;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
/**
 * 集成spring框架的web项目启动定时任务管理器
 * @author rocky.wang
 *
 */
@Component("SchedulingInitializationProcessor")
public class SchedulingInitializationProcessor implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		//root application context 没有parent
		if(event.getApplicationContext().getParent() == null){
			//需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
			TimedTaskManager.getInstance().scheduling();
		}
		
	}

}

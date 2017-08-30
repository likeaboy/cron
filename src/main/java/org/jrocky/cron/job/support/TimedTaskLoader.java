package org.jrocky.cron.job.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jrocky.cron.job.core.ITimedTaskLoader;
import org.jrocky.cron.job.core.TimedTask;
import org.springframework.context.ApplicationContext;

/**
 * 定时任务加载器
 * @author rocky.wang
 *
 */
public class TimedTaskLoader implements ITimedTaskLoader{

//	private ITaskService taskService;
	
	@Override
	public List<TimedTask> load() {
		/*taskService = ContextUtils.getBean(ITaskService.class);
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("taskExecution", String.valueOf(TaskConstant.TaskExeType.FIXEDTIME.code));
		List<Task> tasks = taskService.query(parameterMap);
		List<TimedTask> timedTasks = new ArrayList<TimedTask>();
		TimedTask e;
		for(Task t : tasks){
			e = new TimedTask(t.getTaskId(),
					TaskConstant.TaskType.getTaskType(Integer.parseInt(t.getTaskType())),
					TaskConstant.TaskExeType.FIXEDTIME,
					t.getCron());
			timedTasks.add(e);
		}*/
		List<TimedTask> timedTasks = new ArrayList<TimedTask>();
		
		return timedTasks;
	}

	
}

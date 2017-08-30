# cron
基于spring-scheduing扩展的任务调度模块

##背景：
由于产品需求，需要根据用户在界面的配置，生成定时任务，并且管理定时任务。
产品项目中已集成spring框架，而spring框架提供配置定时任务的功能，也就是我们常用的Cron注解，
由此引出思路，如何复用spring scheduing模块？将在程序中定义好的定时任务变成可根据用户配置动态生成定时任务，并进行管理？
基于以上思路，本人研读了spring框架scheduing模块，该模块具体实现了Spring Cron注解的功能，其中关键核心类有org.springframework.scheduling.config.ScheduledTaskRegistrar（任务注册器）、org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler（任务执行容器）、
org.springframework.scheduling.config.CronTask.CronTask（定时任务）、
org.springframework.scheduling.support.CronTrigger.CronTrigger（定时任务触发器）、
org.springframework.scheduling.support.CronSequenceGenerator（cron表达式解析器）、
org.springframework.scheduling.concurrent.ReschedulingRunnable.ReschedulingRunnable，
其中ReschedulingRunnable为具体定时任务实现的逻辑封装，默认defaut权限，spring内部实现，不提供扩展，如果外部想要复用这块逻辑，
只能从调用父级入手。

##原理：
cron模块org.jrocky.cron.job.support.TimedTaskManager封装了ScheduledTaskRegistrar，在初始化过程中指定ThreadPoolTaskScheduler
为任务执行器，采用阻塞队列缓存用户提交的任务（多线程），任务处理线程org.jrocky.cron.job.support.ReschedulingThread取出队列任务交给
任务注册器进行处理，通过实现org.jrocky.cron.job.core.ITimedTaskLoader接口，可动态从数据库、文件等其他存储介质获取任务信息，动态生成
定时任务，具体细节实现可参加项目源码。

##功能简介：
* 实现了spring监听器ApplicationListener，在应用加载spring容器初始化完成后可进行定时任务模块的初始化，具体参见org.jrocky.cron.job.SchedulingInitializationProcessor；
* 支持定时任务调度，具体对应接口org.jrocky.cron.job.core.ISchedulingLifeCycle的scheduling方法；
* 支持动态提交任务，具体对应接口org.jrocky.cron.job.core.ISchedulingLifeCycle的commit方法；
* 支持取消正在执行的定时任务，具体对应接口org.jrocky.cron.job.core.ISchedulingLifeCycle的cancel方法；
* 支持唤醒取消的任务，具体对应接口org.jrocky.cron.job.core.ISchedulingLifeCycle的reCommit方法；



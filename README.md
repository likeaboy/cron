# cron
基于spring-scheduing扩展的任务调度模块

## 背景：
由于产品需求，需要对用户提交的任务进行动态管理，于是联想到spring的cron，通过研读spring-scheduing模块源码，了解原理，决定复用并进行扩展。 
注意：对任务调度有更高要求的同学，可以使用Quartz，这是一个开源的作业调度框架，为在 Java 应用程序中进行作业调度提供了简单却强大的机制。

scheduing模块模块具体实现了Spring Cron注解的功能，其中关键核心类有
org.springframework.scheduling.config.ScheduledTaskRegistrar（任务注册器）、org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler（任务执行容器）、
org.springframework.scheduling.config.CronTask.CronTask（定时任务）、
org.springframework.scheduling.support.CronTrigger.CronTrigger（定时任务触发器）、
org.springframework.scheduling.support.CronSequenceGenerator（cron表达式解析器）、
org.springframework.scheduling.concurrent.ReschedulingRunnable.ReschedulingRunnable，
其中ReschedulingRunnable为具体定时任务实现的逻辑封装，默认defaut权限，spring内部实现，不提供扩展，如果外部想要复用这块逻辑，
只能从调用父级入手。

## 原理：
cron模块org.jrocky.cron.job.support.TimedTaskManager封装了ScheduledTaskRegistrar，在初始化过程中指定ThreadPoolTaskScheduler
为任务执行器，采用阻塞队列缓存用户提交的任务（多线程），任务处理线程org.jrocky.cron.job.support.ReschedulingThread取出队列任务交给
任务注册器进行处理，通过实现org.jrocky.cron.job.core.ITimedTaskLoader接口，可动态从数据库、文件等其他存储介质获取任务信息，动态生成
定时任务，具体细节实现可参加项目源码。

## 功能简介：
* 实现了spring监听器ApplicationListener，在应用加载spring容器初始化完成后可进行定时任务模块的初始化，具体参见org.jrocky.cron.job.SchedulingInitializationProcessor；
* 支持定时任务调度，具体对应接口org.jrocky.cron.job.core.ISchedulingLifeCycle的scheduling方法；
* 支持动态提交任务，具体对应接口org.jrocky.cron.job.core.ISchedulingLifeCycle的commit方法；
* 支持取消正在执行的定时任务，具体对应接口org.jrocky.cron.job.core.ISchedulingLifeCycle的cancel方法；
* 支持唤醒取消的任务，具体对应接口org.jrocky.cron.job.core.ISchedulingLifeCycle的reCommit方法；

## 测试：
运行
org.jrocky.cron.job.test.TestRunner进行模拟测试，该测试类采用线程池，模拟前端用户多线程提交任务，同时启动
TestRemoveAndRecommitRunnable线程，在运行过程中执行cancel任务以及recommit任务，根据log观察运行结果

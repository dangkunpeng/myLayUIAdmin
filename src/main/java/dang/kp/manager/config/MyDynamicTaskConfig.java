package dang.kp.manager.config;

import dang.kp.manager.biz.dao.MyDynaTaskDao;
import dang.kp.manager.biz.pojo.MyDynaTask;
import dang.kp.manager.common.task.BaseTask;
import dang.kp.manager.common.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Configuration
@EnableScheduling
public class MyDynamicTaskConfig implements SchedulingConfigurer {
    @Autowired
    private MyDynaTaskDao myDynaTaskDao;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        List<MyDynaTask> list = myDynaTaskDao.findAll();
        if (CollectionUtils.isEmpty(list)) {
            log.info("no task");
            return;
        }
        //获取单例applicationContext容器
        ApplicationContext application = SpringUtil.getApplicationContext();
        for (MyDynaTask item : list) {
            taskRegistrar.addTriggerTask(
                //1.添加任务内容(Runnable)
                () -> {
                    try {
                        log.info("Schedule class = {}", item.getTaskClass());
                        BaseTask baseTask = (BaseTask) application.getBean(item.getTaskClass());
                        baseTask.run();
                    } catch (BeansException e) {
                        log.info("{}.run has Exception {}", e);
                    }
                },
                //2.设置执行周期(Trigger)
                triggerContext -> {
                    //2.1 从数据库获取执行周期
                    MyDynaTask task = this.myDynaTaskDao.findById(item.getTaskClass()).get();
                    String cron = task.getTaskTrigger();
                    log.info("cron = {}", cron);
                    //2.2 合法性校验.
                    if (StringUtils.isEmpty(cron)) {
                        log.info("TaskTrigger is blank", cron);
                    }
                    //2.3 返回执行周期(Date)
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
            );
        }

    }
}

package dang.kp.manager.common.task.impl;

import dang.kp.manager.biz.dao.MyDynaTaskDao;
import dang.kp.manager.biz.pojo.MyDynaTask;
import dang.kp.manager.common.task.BaseTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
public class ExchangeData implements BaseTask {

    @Autowired
    private MyDynaTaskDao myDynaTaskDao;

    @Override
    public void run() {
        List<MyDynaTask> list = myDynaTaskDao.findAll();
        if (CollectionUtils.isEmpty(list)) {
            log.info("No Data");
            return;
        }
        list.forEach(item -> {
            log.info("service = {}, trigger = {}", item.getTaskClass(), item.getTaskTrigger());
        });
    }
}

package dang.kp.manager.sys.controller;

import com.alibaba.fastjson.JSONObject;
import dang.kp.manager.common.response.PageDataResult;
import dang.kp.manager.common.result.Result;
import dang.kp.manager.common.result.ResultUtils;
import dang.kp.manager.common.utils.BatchUtils;
import dang.kp.manager.common.utils.MyConstants;
import dang.kp.manager.common.utils.PageUtils;
import dang.kp.manager.sys.dao.DictItemDao;
import dang.kp.manager.sys.dao.DictTypeDao;
import dang.kp.manager.sys.pojo.DictItem;
import dang.kp.manager.sys.pojo.DictType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("dictItem")
public class DictItemController {

    @Autowired
    private DictItemDao dictItemDao;
    @Autowired
    private DictTypeDao dictTypeDao;

    /**
     * 跳转到数据字典列表页面
     *
     * @param model
     * @return
     */
    @RequestMapping("")
    public String dictItemManage(ModelMap model) {
        log.info("跳转到数据字典项列表页面");
        return "sys/dict/dictItemManage";
    }

    /**
     * 功能描述: 获取字典项列表
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/30 10:30
     */
    @PostMapping("/list")
    @ResponseBody
    public PageDataResult permissionList(@RequestParam("pageNum") Integer pageNum,
                                         @RequestParam("pageSize") Integer pageSize,
                                         DictItem dictItem) {
        log.info("获取字典项列表");
        Sort sort = Sort.by(Sort.Order.asc("lineIndex"));
        // 匹配模式
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("typeId", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("text", ExampleMatcher.GenericPropertyMatchers.contains())
                .withIgnoreNullValues();
        // 查询模板
        Example<DictItem> example = Example.of(dictItem, matcher);
        Pageable pageable = PageRequest.of(0, 0, sort);
        Page<DictItem> pageResult = this.dictItemDao.findAll(example, pageable);
        return PageUtils.getPage(pageResult);
    }

    @PostMapping("/save")
    @ResponseBody
    public Result setDictItem(DictItem dictItem) {
        log.info("设置字典项[新增或更新]！DictItem:{}" + JSONObject.toJSONString(dictItem));
        if (StringUtils.isBlank(dictItem.getItemId())) {
            //新增权限
            dictItem.setItemId(BatchUtils.getKey(MyConstants.MyKey.DictItem));
        }
        this.dictItemDao.save(dictItem);
        return ResultUtils.success(dictItem);
    }

    @PostMapping("/del")
    @ResponseBody
    public Result delDictType(DictItem dictItem) {
        log.info("设置字典项[删除]！DictItem:{}" + JSONObject.toJSONString(dictItem));
        if (StringUtils.isBlank(dictItem.getItemId())) {
            return ResultUtils.fail("参数异常");
        }
        this.dictItemDao.deleteById(dictItem.getItemId());
        return ResultUtils.success(dictItem);
    }
}

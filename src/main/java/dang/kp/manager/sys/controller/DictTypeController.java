package dang.kp.manager.sys.controller;

import com.alibaba.fastjson.JSONObject;
import dang.kp.manager.common.response.PageDataResult;
import dang.kp.manager.common.result.Result;
import dang.kp.manager.common.result.ResultUtils;
import dang.kp.manager.common.utils.BatchUtils;
import dang.kp.manager.common.utils.MyConstants;
import dang.kp.manager.common.utils.PageUtils;
import dang.kp.manager.sys.dao.DictTypeDao;
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

import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("dictType")
public class DictTypeController {

    @Autowired
    private DictTypeDao dictTypeDao;
    /**
     * 跳转到数据字典列表页面
     * @param model
     * @return
     */
    @RequestMapping("")
    public String dictManage(ModelMap model) {
        log.info("跳转到数据字典列表页面");
        return "/sys/dict/dictTypeManage";
    }
    /**
     * 功能描述: 获取字典列表
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
                                         DictType dictType) {
        log.info("获取字典列表");
        Sort sort = Sort.by(Sort.Order.desc("typeId"));
        if (Objects.isNull(pageNum)) {
            pageNum = 1;
        }
        pageNum -= 1;
        if (Objects.isNull(pageSize)) {
            pageSize = 10;
        }
        // 匹配模式
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("typeName", ExampleMatcher.GenericPropertyMatchers.contains())
                .withIgnoreNullValues();
        // 查询模板
        Example<DictType> example = Example.of(dictType, matcher);
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<DictType> pageResult = this.dictTypeDao.findAll(example, pageable);
        return PageUtils.getPage(pageResult);
    }
    @PostMapping("/save")
    @ResponseBody
    public Result setDictType(DictType dictType) {
        log.info("设置字典[新增或更新]！DictType:{}" + JSONObject.toJSONString(dictType));
        if (StringUtils.isBlank(dictType.getTypeId())) {
            //新增权限
            dictType.setTypeId(BatchUtils.getKey(MyConstants.MyKey.DictType));
        }
        this.dictTypeDao.save(dictType);
        return ResultUtils.success(dictType);
    }

    @PostMapping("/del")
    @ResponseBody
    public Result delDictType(DictType dictType) {
        log.info("设置字典[删除]！permission:{}" + JSONObject.toJSONString(dictType));
        if (StringUtils.isBlank(dictType.getTypeId())) {
            return ResultUtils.fail("参数异常");
        }
        this.dictTypeDao.deleteById(dictType.getTypeId());
        return ResultUtils.success(dictType);
    }
}

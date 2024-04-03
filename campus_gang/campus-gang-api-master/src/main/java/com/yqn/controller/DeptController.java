package com.yqn.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yqn.pojo.Class;
import com.yqn.pojo.Dept;
import com.yqn.service.ClassService;
import com.yqn.service.DeptService;
import com.yqn.common.tools.MessageTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author sxc
 */
@RestController
@RequestMapping("/dept")
public class DeptController {
    @Autowired
    private DeptService deptService;
    @Autowired
    private MessageTools message;
    @Resource
    private ClassService classService;

    // 获取全部dept
    @GetMapping
    public Map<String, Object> depts() {
        List<Dept> depts = deptService.list();
        return message.message(true, "请求成功", "dept", depts);
    }

    // 根据id获取dept
    @GetMapping("/{id}")
    public Map<String, Object> dept(@PathVariable Long id) {
        Dept dept = deptService.getById(id);
        return message.message(true, "请求成功", "dept", dept);
    }

    // 添加dept
    @PostMapping
    public Map<String, Object> saveDept(Dept dept) {
        LambdaQueryWrapper<Dept> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dept::getSchoolId, dept.getSchoolId())
                .eq(Dept::getName, dept.getName());
        Dept one = deptService.getOne(wrapper);
        if (one == null) {
            deptService.save(dept);
            return message.message(true, "添加系别成功", "", null);
        }

        return message.message(false, "error, 该系已存在", "", null);
    }

    // 删除
    @DeleteMapping("/{id}")
    public Map<String, Object> delDept(@PathVariable Long id) {
        LambdaQueryWrapper<Class> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Class::getDeptId, id);
        List<Class> list = classService.list(wrapper);
        if (!CollectionUtils.isEmpty(list)){
            return message.message(false, "error, 删除失败，系别下已有班级", "", null);
        }
        boolean remove = deptService.removeById(id);
        if (remove) {
            return message.message(true, "删除成功", "", null);
        }
        return message.message(false, "error, 删除失败", "", null);
    }
}

package com.yqn.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yqn.pojo.Class;
import com.yqn.pojo.User;
import com.yqn.service.ClassService;
import com.yqn.common.tools.MessageTools;
import com.yqn.service.UserService;
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
@RequestMapping("/class")
public class ClassController {
    @Resource
    private ClassService classService;
    @Resource
    private MessageTools message;
    @Resource
    private UserService userService;

    // 获取全部class
    @GetMapping
    public Map<String, Object> myClass() {
        List<Class> c = classService.list();
        return message.message(true, "请求成功", "class", c);
    }

    // 根据id获取myClass
    @GetMapping("/{id}")
    public Map<String, Object> myClass(@PathVariable Long id) {
        Class c = classService.getById(id);
        return message.message(true, "请求成功", "class", c);
    }

    // 添加myClass
    @PostMapping
    public Map<String, Object> saveClass(Class c) {
        QueryWrapper<Class> wrapper = new QueryWrapper<>();
        wrapper.eq("school_id", c.getSchoolId())
                .eq("dept_id", c.getDeptId())
                .eq("name", c.getName());

        Class one = classService.getOne(wrapper);
        if (one == null) {
            classService.save(c);
            return message.message(true, "添加班级成功", "", null);
        }

        return message.message(true, "error, 班级已存在", "", null);
    }

    // 删除
    @DeleteMapping("/{id}")
    public Map<String, Object> delClass(@PathVariable Long id) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getClassId,id);
        List<User> list = userService.list(wrapper);
        if (!CollectionUtils.isEmpty(list)){
            return message.message(true, "error, 删除失败，班级下已有学生", "", null);
        }
        boolean remove = classService.removeById(id);
        if (remove) {
            return message.message(true, "删除成功", "", null);
        }
        return message.message(true, "error, 删除失败", "", null);
    }
}

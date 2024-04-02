package com.yqn.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yqn.common.tools.MessageTools;
import com.yqn.pojo.Dept;
import com.yqn.pojo.School;
import com.yqn.service.DeptService;
import com.yqn.service.SchoolService;
import jdk.nashorn.internal.runtime.Debug;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/school")
public class SchoolController {
    @Resource
    private SchoolService schoolService;
    @Resource
    private MessageTools message;
    @Resource
    private DeptService deptService;

    // 获取全部school
    @GetMapping
    public Map<String, Object> schools() {
        List<School> schools = schoolService.list();
        return message.message(true, "请求成功", "school", schools);
    }

    // 根据id获取school
    @GetMapping("/{id}")
    public Map<String, Object> findSchoolsById(@PathVariable String id) {
        School school = schoolService.getById(id);
        return message.message(true, "请求成功", "school", school);
    }

    // 添加school
    @PostMapping
    public Map<String, Object> saveSchool(School school) {
        boolean save = schoolService.save(school);
        if (save) {
            return message.message(true, "添加学校成功", "", null);
        }
        return message.message(false, "error,学校已存在", "", null);
    }


    // 删除school
    @DeleteMapping("/{id}")
    public Map<String, Object> delSchool(@PathVariable Long id) {
        LambdaQueryWrapper<Dept> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dept::getSchoolId,id);
        List<Dept> list = deptService.list(wrapper);
        if (!CollectionUtils.isEmpty(list)){
            return message.message(false, "error,删除学校失败,学校下有专业，不能删除", "", null);
        }
        boolean remove = schoolService.removeById(id);
        if (remove) {
            return message.message(true, "删除学校成功", "", null);
        }
        return message.message(false, "error,删除学校失败", "", null);
    }

    // 更新school
    @PutMapping
    public Map<String, Object> putSchool(School school) {
        boolean update = schoolService.updateById(school);
        if (update) {
            return message.message(true, "更新学校信息成功", "", null);
        }
        return message.message(false, "error,更新学校信息失败", "", null);
    }
}

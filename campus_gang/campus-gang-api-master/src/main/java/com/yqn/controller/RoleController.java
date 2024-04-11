package com.yqn.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yqn.common.tools.MessageTools;
import com.yqn.pojo.Admin;
import com.yqn.pojo.Role;
import com.yqn.pojo.Task;
import com.yqn.pojo.User;
import com.yqn.service.AdminService;
import com.yqn.service.RoleService;
import com.yqn.service.TaskService;
import com.yqn.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/role")
public class RoleController {
    @Resource
    private RoleService roleService;
    @Resource
    private UserService userService;
    @Resource
    private AdminService adminService;
    @Resource
    private MessageTools message;
    @Resource
    private TaskService taskService;

    // 获取全部Role
    @GetMapping
    public Map<String, Object> roles(String name) {
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        if (name != null) {
            wrapper.like("name", name);
            return message.message(true, "请求成功", "role", roleService.list(wrapper));
        }
        List<Role> users = roleService.list();
        return message.message(true, "请求成功", "role", users);
    }

    // 根据id获取Role
    @GetMapping("/{id}")
    public Map<String, Object> role(@PathVariable String id) {
        Role role = roleService.getById(id);
        return message.message(true, "请求成功", "role", role);
    }

    // 添加User
    @PostMapping
    public Map<String, Object> saveRole( Role role) {
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        wrapper.eq("name", role.getName()).or().eq("key",role.getKey());
        Role one = roleService.getOne(wrapper);
        if (one == null) {
            roleService.save(role);
            return message.message(true, "新增角色成功", "", null);
        }
        return message.message(false, "error, 该角色已存在", "", null);
    }

    // 更新信息
    @PutMapping
    public Map<String, Object> putRole(Role role) {
        boolean update = roleService.updateById(role);
        if (update) {
            return message.message(true, "更新信息成功", "", null);
        }
        return message.message(false, "error, 更新信息失败", "", null);
    }

    // 删除角色
    @DeleteMapping("/{id}")
    public Map<String, Object> delRole(@PathVariable Long id) {
        boolean remove;
        if (id == 1){
            List<Admin> list = adminService.list();
            if (CollectionUtils.isEmpty(list)) {
                remove = roleService.removeById(id);
            }else {
                return message.message(false, "error,此角色已绑定用户", "", null);
            }
        }else {
            List<User> list = userService.list();
            if (CollectionUtils.isEmpty(list)) {
                remove = roleService.removeById(id);
            }else {
                return message.message(false, "error,此角色已绑定用户", "", null);
            }
        }
        if (remove) {
            return message.message(true, "删除角色成功", "", null);
        }
        return message.message(false, "error,删除角色失败", "", null);
    }

    // 根据role获取User
    @GetMapping("/users/{key}")
    public Map<String, Object> usersByRole(@PathVariable String key) {
        if (key.equals("admin")){
            List<User> list = userService.list();
            return message.message(true, "请求成功", "users", list);
        }else {
            List<Admin> list = adminService.list();
            return message.message(true, "请求成功", "users", list);
        }
    }


    //根据role和ids为角色分配用户
    @Transactional
    @PostMapping("/{role}/{ids}")
    public Map<String, Object> saveRole( @PathVariable String role,@PathVariable String ids) {
        if (StringUtils.isBlank(role) || StringUtils.isBlank(ids)){
            return message.message(false, "请求失败,未选中人员", "users", "");
        }
        //普通用户转换为管理员
        if (role.equals("admin")){
            String[] split = ids.split(",");
            for (String s : split) {
                User byId = userService.getById(Long.parseLong(s));
                if (byId.getBalance()!=0){
                    return message.message(false, "请求失败"+byId.getUsername()+"的余额不为0，不能分配为管理员", "users", "");
                }
                LambdaQueryWrapper<Task> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Task::getAcceptId,Long.parseLong(s));
                List<Task> list = taskService.list(wrapper);
                if (!CollectionUtils.isEmpty(list)) {
                    return message.message(false, "请求失败"+byId.getUsername()+"已接受任务，不能分配为管理员", "users", "");
                }
                LambdaQueryWrapper<Task> wrapper1 = new LambdaQueryWrapper<>();
                wrapper1.eq(Task::getPublishId,Long.parseLong(s));
                List<Task> publishList = taskService.list(wrapper1);
                if (!CollectionUtils.isEmpty(publishList)) {
                    return message.message(false, "请求失败"+byId.getUsername()+"已发布任务，不能分配为管理员", "users", "");
                }
                Admin admin = new Admin();
                admin.setAccount(byId.getStudentId());
                admin.setPassword(byId.getPassword());
                admin.setUsername(byId.getUsername());
                admin.setCreateTime(new Date());
                admin.setState(0);
                adminService.save(admin);
                userService.removeById(Long.parseLong(s));
            }
        }else {
            //管理员转换为普通用户
            String[] split = ids.split(",");
            for (String s : split) {
                if (Long.parseLong(s)==1){
                    return message.message(false, "系统初始管理员不可操作", "users", "");
                }
                Admin byId = adminService.getById(Long.parseLong(s));
                User user = new User();
                user.setUsername(byId.getUsername());
                user.setPassword(DigestUtils.md5DigestAsHex(byId.getPassword().getBytes()));
                user.setCreateTime(new Date());
                user.setStudentId(byId.getAccount());
                user.setBalance(0d);
                user.setState(0);
                user.setSchoolId(1l);
                user.setDeptId(1l);
                user.setClassId(1l);
                userService.save(user);
                adminService.removeById(Long.parseLong(s));
            }
        }
        return message.message(true, "请求成功", "users", "");
    }
}

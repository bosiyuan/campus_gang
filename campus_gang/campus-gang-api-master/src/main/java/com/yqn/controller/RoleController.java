package com.yqn.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yqn.common.tools.MessageTools;
import com.yqn.pojo.Admin;
import com.yqn.pojo.Role;
import com.yqn.pojo.User;
import com.yqn.pojo.UserRole;
import com.yqn.service.AdminService;
import com.yqn.service.RoleService;
import com.yqn.service.UserRoleService;
import com.yqn.service.UserService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
    @GetMapping("/{role}")
    public Map<String, Object> usersByRole(@PathVariable String role) {
        if (role.equals("admin")){
            List<User> list = userService.list();
            return message.message(true, "请求成功", "users", list);
        }else {
            List<Admin> list = adminService.list();
            return message.message(true, "请求成功", "users", list);
        }
    }

}

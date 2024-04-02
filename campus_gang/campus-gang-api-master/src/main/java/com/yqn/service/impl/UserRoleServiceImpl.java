package com.yqn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqn.mapper.UserRoleMapper;
import com.yqn.pojo.UserRole;
import com.yqn.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * @author sxc
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
}

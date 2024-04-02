package com.yqn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqn.mapper.RoleMapper;
import com.yqn.pojo.Role;
import com.yqn.service.RoleService;
import org.springframework.stereotype.Service;

/**
 * @author sxc
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
}

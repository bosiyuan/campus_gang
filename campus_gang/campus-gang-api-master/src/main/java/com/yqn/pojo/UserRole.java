package com.yqn.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
@TableName("user_role")
public class UserRole {
    private Long userid;
    private Long roleid;
}

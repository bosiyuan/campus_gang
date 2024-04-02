package com.yqn.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author sxc
 */
@ToString
@Data
@TableName(resultMap = "roleResultMap")
public class Role {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "name")
    private String name;
    @TableField(value = "key")
    private String key;
    @TableField(value = "order")
    private Integer order;
    @TableField(value = "state")
    private Integer state;
}

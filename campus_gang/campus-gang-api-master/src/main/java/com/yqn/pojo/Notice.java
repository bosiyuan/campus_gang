package com.yqn.pojo;

import com.yqn.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 通知公告公告对象 notice
 *
 * @author sxc
 * @date 2022-05-13
 */
@Data
@ToString
public class Notice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 公告ID */
    private Integer noticeId;

    /** 公告标题 */
    private String noticeTitle;

    /** 公告内容 */
    private String noticeContent;

}

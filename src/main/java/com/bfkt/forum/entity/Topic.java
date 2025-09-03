package com.bfkt.forum.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author lvqi
 * @since 2025-09-02
 */
@Getter
@Setter
public class Topic implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 采集地址
     */
    private String caijiUrl;

    /**
     * 创建时间
     */
    private LocalDateTime createAt;

    /**
     * 最新采集时间
     */
    private LocalDateTime lastCaijiDate;

    /**
     * 采集替换规则
     */
    private String replaceRule;

    /**
     * 当前目录
     */
    private String curDir;
}

package com.bfkt.forum.model.po;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TopicPo {
    @Schema(description = "页码")
    private Integer pageNo;

    @Schema(description = "页大小")
    private Integer pageSize;

    @Schema(description = "标题")
    private String title;
}

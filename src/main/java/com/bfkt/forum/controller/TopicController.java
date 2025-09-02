package com.bfkt.forum.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bfkt.forum.common.Response;
import com.bfkt.forum.entity.Topic;
import com.bfkt.forum.model.po.TopicPo;
import com.bfkt.forum.service.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lvqi
 * @since 2025-09-02
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/topic")
public class TopicController {

    private final TopicService topicService;

    @Operation(summary = "新增或保存")
    @PostMapping("/saveOrUpdate")
    public Response<Void> saveOrUpdate(@RequestBody Topic topic) {
        topicService.saveOrUpdate(topic);
        return Response.ok(null);
    }

    @Operation(summary = "删除")
    @PostMapping("/remove")
    public Response<Void> remove(@RequestBody List<Long> ids) {
        topicService.remove(ids);
        return Response.ok(null);
    }

    @Operation(summary = "分页查询")
    @PostMapping("/page")
    public Response<IPage<Topic>> page(@RequestBody TopicPo topicPo) {
        IPage<Topic> page = topicService.page(topicPo);
        return Response.ok(page);
    }

    @Operation(summary = "替换所有")
    @PostMapping("/replaceAll")
    public Response<Void> replaceAll(@RequestBody List<Topic> topics) {
        topicService.replaceAll(topics);
        return Response.ok(null);
    }
}

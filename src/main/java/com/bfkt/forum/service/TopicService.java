package com.bfkt.forum.service;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bfkt.forum.entity.Topic;
import com.bfkt.forum.model.po.TopicPo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final ITopicService iTopicService;


    public void saveOrUpdate(Topic topic) {
        if (topic.getId() == null) {
            topic.setCreateAt(LocalDateTime.now());
        }
        topic.setLastCaijiDate(LocalDateTime.now());
        iTopicService.saveOrUpdate(topic);
    }

    public void remove(List<Long> ids) {
        iTopicService.removeBatchByIds(ids);
    }

    public IPage<Topic> page(TopicPo topicPo) {
        LambdaQueryWrapper<Topic> queryWrapper = Wrappers.lambdaQuery(Topic.class);
        queryWrapper.like(CharSequenceUtil.isNotBlank(topicPo.getTitle()), Topic::getTitle, topicPo.getTitle());
        return iTopicService.page(
                new Page<>(topicPo.getPageNo(), topicPo.getPageSize()),
                queryWrapper
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public void replaceAll(List<Topic> topics) {
        topics.forEach(e -> {
            e.setId(null);
            e.setCreateAt(LocalDateTime.now());
            e.setLastCaijiDate(LocalDateTime.now());
        });
        QueryWrapper<Topic> queryWrapper = new QueryWrapper<>();
        iTopicService.remove(queryWrapper);
        iTopicService.saveBatch(topics);
    }
}

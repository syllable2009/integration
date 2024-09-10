package com.jxp.customer;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jxp.customer.dto.EventCallback;
import com.jxp.customer.dto.MessageCallback;
import com.jxp.customer.service.CustomerService;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 客服系统：
 * 知识库：分类 + 问题(标准问，关键词，答案，相似问)
 * 客服：客服管理 + 客服组管理 + 排班管理 + 分配规则
 * 机器人基础配置(欢迎语，欢迎目录，欢迎结尾，推荐问，关联问，高频词设置)
 * 人工基础配置：转人工关键词，转人工选择组，排队与分配规则，转人工前排队前后放弃过多等提示词
 * 留言系统：留言设置 + 留言字段
 * 评价系统：评价模式 + 评价样式，客服是否可见
 * 会话信息：机器人会话超时事件，人工会话超时事件，机器人打招呼语，人工欢迎语，人工接入提示语，人工超时安抚语。
 * 会话转接设置：超时规则 + 转接规则 + 转接确认
 * 会话结束：结束提示词，超时提示词，访客结束规则
 * 告警与质检
 *
 * @author jiaxiaopeng
 * Created on 2024-09-02 17:06
 */
@Slf4j
@RestController
@RequestMapping("/customer")
public class ApiController {

    @Resource
    private CustomerService customerService;

    // 消息回调
    public ResponseEntity<String> messageCallback(@RequestBody MessageCallback dto) {
        // 消息去重
        if (StrUtil.equals("enterApp", dto.getEventType())) {
            // 进入app
        } else if (StrUtil.equals("groupChat", dto.getEventType())) {
            // 客服群聊
        } else if (StrUtil.equals("groupTag", dto.getEventType())) {
            // 普通群聊
        } else if (StrUtil.equals("message", dto.getEventType())) {
            // 消息号单聊
        } else {
            // 其他
        }
        return ResponseEntity.ok("");
    }

    // 事件回调
    public ResponseEntity<String> eventCallback(@RequestBody EventCallback dto) {
        return ResponseEntity.ok("");
    }
}

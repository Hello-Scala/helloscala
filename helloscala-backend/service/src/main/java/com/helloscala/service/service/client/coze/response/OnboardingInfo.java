package com.helloscala.web.service.client.coze.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class OnboardingInfo {
    @JSONField(name = "prologue")
    private String prologue;
    
    @JSONField(name = "suggested_questions")
    private List<String> suggestedQuestions;
}
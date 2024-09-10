package com.jxp.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2024-09-02 17:21
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Application {
    private String aid;
    private String uid;
    private String state;
    // 会话拆分为人工和机器人
    private Boolean sessionSplit;
}

package com.jxp.integration.test.plugin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-28 11:37
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Context {
    private String userId;
    private Boolean anonymous;
    private Long requestTimestamp;
}

package com.jxp.integration.test.auth.bean;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2023-07-14 12:01
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShortCutTreeNode {
    private String id;
    private float weight;
    private List<ShortCutTreeNode> children;
}

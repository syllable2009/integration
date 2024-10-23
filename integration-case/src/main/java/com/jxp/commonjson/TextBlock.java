package com.jxp.commonjson;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2024-10-23 17:24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextBlock implements Block {
    private String content;

    @NotNull
    @Override
    public String getBlockType() {
        return BlockTypeEnum.Text.getCode();
    }
}

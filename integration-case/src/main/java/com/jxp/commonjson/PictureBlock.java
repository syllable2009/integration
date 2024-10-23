package com.jxp.commonjson;

import javax.validation.constraints.NotEmpty;

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
public class PictureBlock implements Block {
    private String type;
    private String url;
    private Long size;
    private String previewUrl;
    private String previewType;

    @NotEmpty
    @Override
    public String getBlockType() {
        return BlockTypeEnum.Picture.getCode();
    }
}

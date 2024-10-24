package com.jxp.commonjson;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2024-10-24 16:15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Picture {
    private String type;
    private String url;
    private Long size;
    private String previewUrl;
    private String previewType;
}

package com.jxp.dto.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2024-10-14 19:59
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FileDTO {

    private String fileKey;

    private String mediaType;

    private String fileUrl;

    private Long fileSize;
}

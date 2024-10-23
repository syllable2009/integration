package com.jxp.commonjson;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.EXTERNAL_PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author jiaxiaopeng
 * Created on 2024-10-23 17:13
 */

@JsonTypeInfo(use = NAME, include = EXTERNAL_PROPERTY, property = "blockType", defaultImpl =
        TextBlock.class, visible = true)
@JsonSubTypes({@JsonSubTypes.Type(value = TextBlock.class, name = "Text"),
        @JsonSubTypes.Type(value = PictureBlock.class, name = "Picture"),
})
public interface Block {
    @NotEmpty
    String getBlockType();
}

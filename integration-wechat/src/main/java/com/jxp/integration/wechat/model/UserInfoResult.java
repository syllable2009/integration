package com.jxp.integration.wechat.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jiaxiaopeng
 * Created on 2022-12-08 12:26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserInfoResult extends BaseResult {
    private int subscribe;
    private String openid;
    private String language;
    @JsonProperty("subscribe_time")
    private int subscribeTime;
    private String unionid;
    private String remark;
    private int groupid;
    @JsonProperty("tagid_list")
    private List<Integer> tagidList;
    @JsonProperty("subscribe_scene")
    private String subscribeScene;
    @JsonProperty("qr_scene")
    private int qrScene;
    @JsonProperty("qr_scene_str")
    private String qrSceneStr;
}

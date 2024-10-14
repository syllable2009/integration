package com.jxp.dto.bo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2024-10-14 20:21
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SpiderTaskResp {

    private List<SingleAddressReq> singleAddressReqList;
}

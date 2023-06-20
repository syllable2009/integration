package com.jxp.es.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-19 17:59
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class Product {
    private String id;
    private String name;
    private String description;
    private int price;
}

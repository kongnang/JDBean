package com.example.jdbean.domain;

import lombok.Data;
import lombok.ToString;

/**
 * @author qiu
 * @create 2022-10-25 10:46
 *
 * 摇京豆奖励中的jingBeanVO
 */
@Data
public class JingBeanVO {
    Integer beanNum;

    @Override
    public String toString() {
        return "获得" + beanNum + "京豆";
    }
}

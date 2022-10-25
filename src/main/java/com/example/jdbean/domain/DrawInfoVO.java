package com.example.jdbean.domain;

/**
 * @author qiu
 * @create 2022-10-25 10:47
 *
 * 摇京豆奖励中的deawVO
 */
public class DrawInfoVO {
    Integer drawNum;

    @Override
    public String toString() {
        return "获得" + drawNum + "次抽奖机会";
    }
}

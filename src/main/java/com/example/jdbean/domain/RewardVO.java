package com.example.jdbean.domain;

import io.swagger.models.auth.In;
import lombok.Data;

/**
 * @author qiu
 * @create 2022-10-25 10:41
 *
 * 摇京豆响应体中的rewardVos
 */
@Data
public class RewardVO {
    String signItemId;
    String signTime;
    Integer pitType;
    Integer rewardSource;
    Integer signType;
//    CouponVO couponVO;
//    HongBaoVO hongBaoVO;
    DrawInfoVO drawInfoVO;
    JingBeanVO jingBeanVO;
}

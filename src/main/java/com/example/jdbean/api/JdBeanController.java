package com.example.jdbean.api;

import com.example.jdbean.result.AjaxResult;
import com.example.jdbean.task.DailyJdBeanTask;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qiu
 * @create 2022-10-22 16:49
 */
@RestController
@Api(tags = "测试定时任务接口")
public class JdBeanController {

    @Autowired
    private DailyJdBeanTask dailyJdBeanTask;

    @GetMapping("/jdbean")
    public AjaxResult jdbeanTest(@RequestParam Integer type) throws Exception {
        if (type == 1) {
            return dailyJdBeanTask.sign();
        }else if (type == 2) {
            return dailyJdBeanTask.sharkBean();
        }else if (type == 3) {
            return dailyJdBeanTask.lottery();
        }
        return AjaxResult.error("请求参数错误");
    }
}

package com.example.jdbean.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.jdbean.domain.JingBeanVO;
import com.example.jdbean.domain.RewardVO;
import com.example.jdbean.result.AjaxResult;
import com.example.jdbean.util.OkHttpUtils;
import com.example.jdbean.util.ResourcesUtils;
import com.example.jdbean.util.StringUtils;
import com.google.common.collect.Maps;
import jdk.internal.org.objectweb.asm.TypeReference;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author qiu
 * @create 2022-10-22 14:41
 */
@Component
@Slf4j
public class DailyJdBeanTask {
    @Value("${jd.fileName}")
    private String FILE_NAME;

    @Autowired
    private ResourcesUtils resourcesUtils;

    private List<String> cookies;

    /**
     * 从文件中获取用户cookie
     * 在服务器加载Servlet的时候运行，并且只会被服务器执行一次
     */
    @PostConstruct
    public void initCookie() {
        log.info("读取pt_key，pt_pin...");
        cookies = new ArrayList<>();
        List<String> strings = resourcesUtils.readFromClassPath(FILE_NAME);
        for (String string : strings) {
            String[] split = string.split(",");
            String ptKey = split[0];
            String ptPin = split[1];
            String cookie = "__jd_ref_cls=JingDou_SceneHome_NewGuidExpo; mba_muid=1645885780097318205272.81.1645885790055; mba_sid=81.5; __jda=122270672.1645885780097318205272.1645885780.1645885780.1645885780.1; __jdb=122270672.1.1645885780097318205272|1.1645885780; __jdc=122270672; __jdv=122270672%7Ckong%7Ct_1000170135%7Ctuiguang%7Cnotset%7C1644027879157; pre_seq=0; pre_session=3acd1f6361f86fc0a1bc23971b2e7bbe6197afb6|143; unpl=JF8EAKZnNSttWRkDURtVThUWHAgEWw1dH0dXOjMMAFVcTQQAEwZORxR7XlVdXhRKFx9sZhRUX1NIVw4YBCsiEEpcV1ZVC0kVAV9XNVddaEpkBRwAExEZQ1lWW1kMTBcEaWcAUVpeS1c1KwUbGyB7bVFeXAlOFQJobwxkXGhJVQQZBR0UFU1bZBUzCQYXBG1vBl1VXElRAR8FGxUWS1hRWVsISCcBb2cHUm1b%7CV2_ZzNtbRYAFxd9DUNcKRxYB2ILGloRUUYcIVpAAHsbWQZjVBEJclRCFnUUR11nGlgUZgIZXkFcQRRFCEJkexhdB24LFFtEUHMQfQ5GXH0pXAQJbRZeLAcCVEULRmR6KV5VNVYSCkVVRBUiAUEDKRgMBTRREV9KUUNGdlxAByhNWwVvBUIKEVBzJXwJdlR6GF0GZAoUWUdRQCUpUBkCJE0ZWTVcIlxyVnMURUooDytAGlU1Vl9fEgUWFSIPRFN7TlUCMFETDUIEERZ3AEBUKBoIAzRQRlpCX0VFIltBZHopXA%253d%253d; " +
                    "pt_key=" + ptKey +
                    "; pt_pin=" + ptPin +
                    "; pwdt_id=jd_505bacd333f6b; sid=1b2c8b7ce820c4188f048e689bf58c8w; visitkey=36446698972455355";
            cookies.add(cookie);
        }
        log.info("拼接cookie完成...");
        log.info("cookie数量: {}", cookies.size());
    }

    /**
     * 每日签到
     */
    @Scheduled(cron = "0 0 8 * * ?")
    public AjaxResult sign() throws Exception {
        log.info("每日签到开始");
        String url = "https://api.m.jd.com/client.action?functionId=signBeanAct&body=%7B%22fp%22%3A%22-1%22%2C%22shshshfp%22%3A%22-1%22%2C%22shshshfpa%22%3A%22-1%22%2C%22referUrl%22%3A%22-1%22%2C%22userAgent%22%3A%22-1%22%2C%22jda%22%3A%22-1%22%2C%22rnVersion%22%3A%223.9%22%7D&appid=ld&client=apple&clientVersion=10.0.4&networkType=wifi&osVersion=14.8.1&uuid=3acd1f6361f86fc0a1bc23971b2e7bbe6197afb6&openudid=3acd1f6361f86fc0a1bc23971b2e7bbe6197afb6&jsonp=jsonp_1645885800574_58482";
        String body = "{\"eid\":\"eidAb47c8121a5s24aIy0D0WQXSKdROGt9BUSeGiNEbMeQodwSwkLi6x5/GTFC7BV7lPMjljpMxVNCcAW/qdrQvDSdhaI5715Sui3MB7nluMccMWqWFL\",\"fp\":\"-1\",\"jda\":\"-1\",\"referUrl\":\"-1\",\"rnVersion\":\"4.7\",\"shshshfp\":\"-1\",\"shshshfpa\":\"-1\",\"userAgent\":\"-1\"}";
        RequestBody requestBody = new FormBody.Builder().add("body", body).build();
        Map<String, String> header= new HashMap<>();

        int n = 0;
        List<String> responses = new ArrayList<>();
        for (String cookie : cookies) {
            String response = OkHttpUtils.post(url, cookie, requestBody, header);
            responses.add(processResponse(response, "1"));
            log.info("京东签到任务执行次数:{}, 结果:{}", ++n, response);
            Thread.sleep(1000);
        }

        // 往微信推送消息
        String title = "京东每日签到";
        StringBuffer desp = new StringBuffer();
        for (String response : responses) {
            desp.append("每日签到结果：");
            desp.append(response).append("\\n");
        }
        OkHttpUtils.weChatPost(title, desp.toString());

        return AjaxResult.success(responses);
    }

    /**
     * 摇京豆签到
     */
    @Scheduled(cron = "0 0 9 * * ?")
    public AjaxResult sharkBean() throws Exception {
        log.info("摇京豆签到开始");

        List<String> responses = new ArrayList<>();
        int n = 0;
        for (String cookie : cookies) {
            for (int i = 1; i < 8; i++) {
                String url = "https://api.m.jd.com/?appid=sharkBean&functionId=pg_interact_interface_invoke&body=%7B%22floorToken%22:%22f1d574ec-b1e9-43ba-aa84-b7a757f27f0e%22,%22dataSourceCode%22:%22signIn%22,%22argMap%22:%7B%22currSignCursor%22:" +
                        i + "%7D,%22riskInformation%22:%7B%22platform%22:1,%22pageClickKey%22:%22%22,%22eid%22:%227IJ4SBWVAY6L5FOEQHCBZ57B3CYAYAA4LGJH2NGO6F6BE7PLEAJUY5WQOUI4BDGFRPH3RSGPLV5APHF4YV4DMJZ2UQ%22,%22fp%22:%22e0e4fadfadac7be71f89b78901f60fe4%22,%22shshshfp%22:%2298d7f7d062531be7af606b13b9c57a3e%22,%22shshshfpa%22:%222768c811-4a2f-1596-cf01-9d0cbd0319b9-1651280386%22,%22shshshfpb%22:%22iMZyawmZjTHrSJ72sZmuHog%22%7D%7D";
                Map<String, String> header = new HashMap<>();
                header.put("origin", "https://spa.jd.com");
                header.put("referer", "https://spa.jd.com/");
                RequestBody requestBody = new FormBody.Builder().build();
                String response = OkHttpUtils.post(url, cookie, requestBody, header);
                log.info("摇京豆执行{}次，response:{}", ++n, response);
                response = processResponse(response, "2");
                // 如果提示已签到就退出循环
                if ("今日已签到~".equals(response)){
                    break;
                }
                responses.add("第" + n + "次摇京豆结果" + response);

                // 防止频控
                Thread.sleep(1000);
            }
        }

        // 往微信推送消息
        String title = "摇京豆签到";
        StringBuffer desp = new StringBuffer();
        for (String response : responses) {
            desp.append(response).append("\\n");
        }
        OkHttpUtils.weChatPost(title, desp.toString());

        return AjaxResult.success(responses);
    }

    /**
     * 京豆抽奖任务，抽奖获取的京豆随机
     */
    @Scheduled(cron = "0 0 8 * * ?")
    public AjaxResult lottery() throws Exception {
        String url = "https://api.m.jd.com/client.action?functionId=babelGetLottery";
        String body = "{\"enAwardK\":\"ltvTJ/WYFPZcuWIWHCAjRz/NdrezuUkm8ZIGKKD06/oaqi8FPY5ILISE5QLULmK6RUnNSgnFndqy\\ny4p8d6/bK/bwdZK6Aw80mPSE7ShF/0r28HWSugMPNPm5JQ8b9nflgkMfDwDJiaqThDW7a9IYpL8z\\n7mu4l56kMNsaMgLecghsgTYjv+RZ8bosQ6kKx+PNAP61OWarrOeJ2rhtFmhQncw6DQFeBryeMUM1\\nw9SpK5iag4uLvHGIZstZMKOALjB/r9TIJDYxHs/sFMU4vtb2jX9DEwleHSLTLeRpLM1w+RakAk8s\\nfC4gHoKM/1zPHJXq1xfwXKFh5wKt4jr5hEqddxiI8N28vWT05HuOdPqtP+0EbGMDdSPdisoPmlru\\n+CyHR5Kt0js9JUM=_babel\",\"awardSource\":\"1\",\"srv\":\"{\\\"bord\\\":\\\"0\\\",\\\"fno\\\":\\\"0-0-2\\\",\\\"mid\\\":\\\"70952802\\\",\\\"bi2\\\":\\\"2\\\",\\\"bid\\\":\\\"0\\\",\\\"aid\\\":\\\"01155413\\\"}\",\"encryptProjectId\":\"3u4fVy1c75fAdDN6XRYDzAbkXz1E\",\"encryptAssignmentId\":\"2x5WEhFsDhmf8JohWQJFYfURTh9w\",\"authType\":\"2\",\"riskParam\":{\"platform\":\"3\",\"orgType\":\"2\",\"openId\":\"-1\",\"pageClickKey\":\"Babel_WheelSurf\",\"eid\":\"eidI69b381246dseNGdrD6vtTrOauSQ/zRycuDRnbInWZmVfFbyoI59uVkzYYiQZrUGzGkpqNpHHJHv37CthY6ooTnYpqX2mBZ2riJHvc8c9kta1QpZh\",\"fp\":\"-1\",\"shshshfp\":\"98d7f7d062531be7af606b13b9c57a3e\",\"shshshfpa\":\"2768c811-4a2f-1596-cf01-9d0cbd0319b9-1651280386\",\"shshshfpb\":\"iMZyawmZjTHrSJ72sZmuHog\",\"childActivityUrl\":\"https%3A%2F%2Fpro.m.jd.com%2Fmall%2Factive%2F2xoBJwC5D1Q3okksMUFHcJQhFq8j%2Findex.html%3Ftttparams%3DjyJinIeyJnTG5nIjoiMTE2LjQwNjQ1IiwiZ0xhdCI6IjQwLjA2MjkxIn60%253D%26un_area%3D1_2901_55565_0%26lng%3D116.4065317104862%26lat%3D40.06278498159455\",\"userArea\":\"-1\",\"client\":\"\",\"clientVersion\":\"\",\"uuid\":\"\",\"osVersion\":\"\",\"brand\":\"\",\"model\":\"\",\"networkType\":\"\",\"jda\":\"-1\"},\"siteClient\":\"apple\",\"mitemAddrId\":\"\",\"geo\":{\"lng\":\"116.4065317104862\",\"lat\":\"40.06278498159455\"},\"addressId\":\"5777681655\",\"posLng\":\"\",\"posLat\":\"\",\"homeLng\":\"116.40645\",\"homeLat\":\"40.06291\",\"focus\":\"\",\"innerAnchor\":\"\",\"cv\":\"2.0\"}";

        log.info("抽京豆开始");
        Map<String, String> header = Maps.newHashMap();
        header.put("origin", "https://pro.m.jd.com");
        header.put("referer", "https://pro.m.jd.com/");

        RequestBody requestBody = new FormBody.Builder()
                .add("body", body)
                .add("client", "wh5")
                .add("clientVersion", "1.0.0")
                .build();

        List<String> responses = new ArrayList<>();
        int n = 0;
        for (String cookie : cookies) {
            String response = OkHttpUtils.post(url, cookie, requestBody, header);
            responses.add(processResponse(response, "3"));
            log.info("抽京豆执行{}次，response：{}", ++n, response);
            Thread.sleep(1000L);
        }

        // 往微信推送消息
        String title = "抽京豆";
        StringBuffer desp = new StringBuffer();
        for (String response : responses) {
            desp.append("抽取京豆结果：");
            desp.append(response).append("\\n");
        }
        OkHttpUtils.weChatPost(title, desp.toString());

        return AjaxResult.success(responses);
    }

    // 处理响应体内容，获取json中的信息
    public String processResponse(String response, String type) {
        String s1 = StringUtils.replace(response, "jsonp_1645885800574_58482(", "");
        String s2 = StringUtils.replace(s1, ");", "");

        JSONObject jsonObject = JSON.parseObject(s2);
        String data = "";
        if (type.equals("1")) {
            String code = (String) jsonObject.get("code");
            if ("0".equals(code)) {
                JSONObject dailyAward = jsonObject.getJSONObject("data").getJSONObject("dailyAward");
                // 新人第一次签到的奖励信息在newUserAward中
                if (!Objects.isNull(dailyAward)) {
                    String title = (String) dailyAward.get("title");
                    String subTitle = (String) dailyAward.get("subTitle");
                    JSONObject beanAward = dailyAward.getJSONObject("beanAward");
                    String beanCount = (String) beanAward.get("beanCount");
                    data = title + subTitle + beanCount + "京豆";
                }

            } else {
                data = (String) jsonObject.get("errorMessage");
            }
        }
        // 摇京豆
        else if (type.equals("2")) {
            JSONObject sharkBeanData = jsonObject.getJSONObject("data");
            if (!Objects.isNull(sharkBeanData)) {
                List<RewardVO> rewardVos = jsonObject.getObject("rewardVos", List.class);
                if (!Objects.isNull(rewardVos)) {
                    for (RewardVO rewardVo : rewardVos) {
                        if (!Objects.isNull(rewardVo.getJingBeanVO())) {
                            data += rewardVo.getJingBeanVO().toString();
                        }
                    }
                }
            }else {
                data = (String) jsonObject.get("message");
            }
        }
        // 抽京豆
        else if (type.equals("3")) {
            String promptMsg = (String) jsonObject.get("promptMsg");
            if (promptMsg.equals("恭喜您，中奖啦!")) {
                String prizeName = (String) jsonObject.get("prizeName");
                data = promptMsg + prizeName;
            }else {
                data = promptMsg;
            }
        }

        return data;
    }
}

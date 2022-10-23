# 京东定时签到

## 使用说明

1. 获取pt_key和pt_pin

   进入[京东商城](https://m.jd.com/)，使用验证码登录，登陆后F12进入调试模式，选择application，在里面找到pt_key和pt_pin的值，复制到项目中的jd_cookie.txt文件夹，按照pt_key,pt_pin的格式。

2. 注册server酱，实现微信推送功能

   [Server酱·Turbo版 (ftqq.com)](https://sct.ftqq.com/)

   获取sendkey，之后使用get请求方式发送请求。

3. 项目运行后可进入[测试文档](http://localhost:8888/doc.html)手动测试，type参数如下所示：

   - 1 : 每日签到任务

   - 2 : 摇京豆签到

   - 3 : 抽京豆

> 本项目参考[multi_function_github](https://github.com/longbig/multi_function_github)

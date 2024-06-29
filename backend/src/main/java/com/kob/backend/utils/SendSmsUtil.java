// This file is auto-generated, don't edit it. Thanks.
package com.kob.backend.utils;

import com.aliyun.tea.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SendSmsUtil {

    public static com.aliyun.dysmsapi20170525.Client createClient() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setAccessKeyId("LTAI5t6AqN8CzbDTqbFKdzcb")
                .setAccessKeySecret("2VBIiOkt3nWk3OeRjC6loLKuIiQdwu");
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

    public static Map<String , String> sendmessage(String telephone) throws Exception {
        System.err.println(telephone);

        Map<String , String> resp = new HashMap<>();

        if(!isMobile(telephone)){
            resp.put("error_message" , "电话格式错误");
            return resp;
        }

        Random random = new Random();
        Integer num = random.nextInt(900) + 1000;
        String code = num.toString();

        com.aliyun.dysmsapi20170525.Client client = SendSmsUtil.createClient();
        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                .setSignName("测盟会")
                .setTemplateCode("SMS_468415539")
                .setPhoneNumbers(telephone)
                .setTemplateParam("{\"code\":\""+ code + "\"}");
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        try {
            client.sendSmsWithOptions(sendSmsRequest, runtime);
            resp.put("error_message" , "success");
            resp.put("code" , code);
        } catch (TeaException error) {
            // 此处仅做打印展示，请谨慎对待异常处理，在工程项目中切勿直接忽略异常。
            // 错误 message
            System.out.println(error.getMessage());
            com.aliyun.teautil.Common.assertAsString(error.message);
            resp.put("error_message" , "电话错误");
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 此处仅做打印展示，请谨慎对待异常处理，在工程项目中切勿直接忽略异常。
            // 错误 message
            System.out.println(error.getMessage());
            com.aliyun.teautil.Common.assertAsString(error.message);
            resp.put("error_message" , "发生错误");
        }

        return resp;
    }

    public static Boolean isMobile(String mobile) {
        String regex = "^((13[0-9])|(14[579])|(15([0-3]|[5-9]))|(16[56])|(17[0-8])|(18[0-9])|(19[1589]))\\d{8}$";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(mobile);
        return m.matches();
    }
}
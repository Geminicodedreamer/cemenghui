package com.kob.backend.controller.message.send;

import com.kob.backend.service.message.send.MessageSendService;
import com.kob.backend.utils.SendSmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MessageSendController {
    @Autowired
    private MessageSendService messageSendService;

    @PostMapping("/api/send/message")
    public Map<String , String> sendmessage(@RequestParam String telephone) throws Exception {
        return messageSendService.sendmessage(telephone);
    }
}

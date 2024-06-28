package com.kob.backend.service.impl.message.send;

import com.kob.backend.service.message.send.MessageSendService;
import com.kob.backend.utils.SendSmsUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MessageSendServiceImpl implements MessageSendService {
    @Override
    public Map<String, String> sendmessage(String telephone) throws Exception {
        return SendSmsUtil.sendmessage(telephone);
    }
}

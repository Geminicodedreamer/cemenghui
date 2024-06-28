package com.kob.backend.service.message.send;

import java.util.Map;

public interface MessageSendService {
    public Map<String , String> sendmessage(String telephone) throws Exception;
}

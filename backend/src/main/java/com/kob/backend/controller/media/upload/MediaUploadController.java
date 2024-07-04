package com.kob.backend.controller.media.upload;

import com.kob.backend.service.media.upload.MediaUpload;
import com.kob.backend.utils.UploadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MediaUploadController {

    @Autowired
    MediaUpload mediaUpload;
    private static final Logger logger = LoggerFactory.getLogger(MediaUploadController.class);

    @PostMapping("/api/upload")
    public Map<String, String> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        try {
            logger.info("Received file: {}", file.getOriginalFilename());
            String fileUrl = mediaUpload.uploadImage(file);
            response.put("url", fileUrl);
            logger.info("File uploaded to: {}", fileUrl);
        } catch (IOException e) {
            logger.error("File upload failed", e);
            response.put("error", e.getMessage());
        }
        return response;
    }
}

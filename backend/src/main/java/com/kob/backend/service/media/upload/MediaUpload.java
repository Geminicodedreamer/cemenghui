package com.kob.backend.service.media.upload;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MediaUpload {
    String uploadImage(MultipartFile file) throws IOException;
}

package com.kob.backend.service.impl.media.upload;

import com.kob.backend.service.media.upload.MediaUpload;
import com.kob.backend.utils.UploadUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class MediaUploadImpl implements MediaUpload {
    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        System.err.println(file.getName());
        return UploadUtil.uploadImage(file);
    }
}

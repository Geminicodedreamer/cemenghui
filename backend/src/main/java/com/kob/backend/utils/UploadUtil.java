package com.kob.backend.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public class UploadUtil {

    private static final Logger logger = LoggerFactory.getLogger(UploadUtil.class);

    // 阿里域名
    public static final String ALI_DOMAIN = "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/";

    public static String uploadImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            logger.error("No file uploaded");
            return "No file uploaded"; // 如果文件为空，则返回提示信息"未上传文件"
        }

        String originalFilename = file.getOriginalFilename(); // 获取原始文件名
        String ext = originalFilename.substring(originalFilename.lastIndexOf(".")); // 获取文件扩展名
        String uuid = UUID.randomUUID().toString().replace("-", ""); // 生成随机UUID
        String newFileName = uuid + ext; // 新文件名

        // 地域节点
        String endpoint = "https://oss-cn-beijing.aliyuncs.com/";
        String accessKeyID = "LTAI5t6AqN8CzbDTqbFKdzcb";
        String accessKeySecret = "2VBIiOkt3nWk3OeRjC6loLKuIiQdwu";

        OSS ossClient = null;
        try {
            // 创建OSS客户端实例
            ossClient = new OSSClientBuilder().build(endpoint, accessKeyID, accessKeySecret);

            // 创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());

            // 上传文件
            ossClient.putObject("wybcemenghui", newFileName, file.getInputStream(), metadata);

            logger.info("File uploaded successfully to {}", ALI_DOMAIN + newFileName);
            return ALI_DOMAIN + newFileName;

        } catch (Exception e) {
            logger.error("File upload failed", e);
            throw new IOException("File upload failed", e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}

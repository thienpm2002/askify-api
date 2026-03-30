package com.thienpm.askify.api.service.user;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.thienpm.askify.api.config.UploadProperties;
import com.thienpm.askify.api.enums.ErrorCode;
import com.thienpm.askify.api.exception.AppException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileStorageService {
    private final UploadProperties uploadProperties;

    public String saveAvatar(MultipartFile avatarFile, Integer userId) {
        // Kiểm tra file rỗng
        if (avatarFile.isEmpty()) {
            throw new AppException(ErrorCode.FILE_EMPTY);
        }
        // Kiểm tra content type
        String contentType = avatarFile.getContentType();
        if (!uploadProperties.getAllowedTypes().contains(contentType)) {
            throw new AppException(ErrorCode.INVALID_FILE_TYPE);
        }

        // Kiểm tra kích thước file
        if (avatarFile.getSize() > uploadProperties.getMaxSize()) {
            throw new AppException(ErrorCode.FILE_TOO_LARGE);
        }

        // Tạo avatarUrl
        String extension = getExtension(avatarFile.getOriginalFilename());
        String fileName = "user_" + userId + "." + extension;

        // Luu file
        try {
            Path uploadDir = Paths.get(uploadProperties.getDir()); // Tạo object đại diện cho đường dẫn thư mục
                                                                   // Path("uploads/avatars")
            Files.createDirectories(uploadDir); // tạo folder nếu chưa có
            Path filePath = uploadDir.resolve(fileName); // Ghép đường dẫn filePath = folder + tên file
            Files.copy(avatarFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING); // ghi nội dung
                                                                                                    // binary của
            // file upload vào
            // filePath(nếu file tồn
            // tại ghi dé luôn)
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }

        return "/avatars/" + fileName + "?v=" + System.currentTimeMillis();
    }

    private String getExtension(String fileName) {
        if (fileName == null || !fileName.contains("."))
            return "jpg";
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}

package com.example.startingclubbackend.service.file;

import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    ResponseEntity<Object> uploadFile(@NotNull final MultipartFile file) throws IOException;

    ResponseEntity<Object> uploadMultipleFile(final List<MultipartFile> files) throws IOException;
}


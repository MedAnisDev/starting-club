package com.example.startingclubbackend.service.file;

import com.example.startingclubbackend.model.file.FileRecord;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    ResponseEntity<Object> uploadFile(@NotNull final MultipartFile file) throws IOException;

    ResponseEntity<Object> uploadMultipleFiles(final List<MultipartFile> files) throws IOException;

    ResponseEntity<Object> fetchAllFilesByEventId(final Long eventId);

    ResponseEntity<Object> addFileToEventId(final Long eventId,final Long fileId);
    FileRecord getFileById(final Long fileId) ;

    ResponseEntity<Object> downloadFile(final String files) throws IOException;

    ResponseEntity<String> deleteFileByIds(final Long fileId) throws IOException;
}


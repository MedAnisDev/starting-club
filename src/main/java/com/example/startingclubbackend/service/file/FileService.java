package com.example.startingclubbackend.service.file;

import com.example.startingclubbackend.model.file.FileRecord;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    ResponseEntity<Object> uploadFile(@NotNull MultipartFile file) throws IOException;

    ResponseEntity<Object> uploadMultipleFiles(List<MultipartFile> files) throws IOException;

    FileRecord getFileById(Long fileId);

    ResponseEntity<Object> downloadFile(String files) throws IOException;
    ResponseEntity<Object> getAllFilesByAthlete(Long athleteId);
    ResponseEntity<Object> getAllFilesByEvent(Long eventId);

    ResponseEntity<Object> getAllDocumentFiles();

    void deleteFileById(Long fileId) throws IOException;
    FileRecord handleFile(MultipartFile file) throws IOException ;
    void saveFile(FileRecord fileRecord);


}


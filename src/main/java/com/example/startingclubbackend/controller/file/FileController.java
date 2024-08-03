package com.example.startingclubbackend.controller.file;

import com.example.startingclubbackend.service.file.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/files")
public class FileController {

    private final FileService fileService ;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload_file" )
    public ResponseEntity<Object> uploadFile(@RequestParam(value = "file") final MultipartFile file) throws IOException {
        return fileService.uploadFile(file) ;
    }

    @PostMapping("/upload_files" )
    public ResponseEntity<Object> uploadMultipleFile(@RequestParam(value = "files") final List<MultipartFile> files) throws IOException {
        return fileService.uploadMultipleFile(files) ;
    }
}

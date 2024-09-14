package com.example.startingclubbackend.controller.file;

import com.example.startingclubbackend.service.file.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/files")
@Slf4j
public class FileController {

    private final FileService fileService ;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload_file" )
    public ResponseEntity<Object> uploadFile(@RequestParam(value = "file") final MultipartFile file) throws IOException {
        log.info("uploadFile endpoint called: ");
        log.info("Received file: " + file.getOriginalFilename());
        return fileService.uploadFile(file) ;
    }

    @PostMapping("/upload_files" )
    public ResponseEntity<Object> uploadMultipleFiles(@RequestParam(value = "files") final List<MultipartFile> files) throws IOException {
        return fileService.uploadMultipleFiles(files) ;
    }

    @GetMapping("/download_file" )
    public ResponseEntity<Object> downloadFile(@RequestParam(value = "fileName") final String fileName) throws IOException {
            return fileService.downloadFile(fileName) ;
    }
    @DeleteMapping("/{fileId}")
    public ResponseEntity<String> deleteFileById(@PathVariable final Long fileId) {
        try{
            fileService.deleteFileById(fileId) ;
            return new ResponseEntity<>("File deleted successfully" , HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("File not found or could not be deleted", HttpStatus.NOT_FOUND) ;
        }
    }
}

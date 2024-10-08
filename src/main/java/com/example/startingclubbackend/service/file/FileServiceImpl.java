package com.example.startingclubbackend.service.file;

import com.example.startingclubbackend.DTO.file.FileRecordDTO;
import com.example.startingclubbackend.DTO.file.FileRecordDTOMapper;
import com.example.startingclubbackend.exceptions.custom.DatabaseCustomException;
import com.example.startingclubbackend.exceptions.custom.FileValidationCustomException;
import com.example.startingclubbackend.exceptions.custom.ResourceNotFoundCustomException;
import com.example.startingclubbackend.model.file.FileRecord;
import com.example.startingclubbackend.model.user.admin.Admin;
import com.example.startingclubbackend.repository.FileRepository;
import org.springframework.core.io.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FileServiceImpl implements FileService{
    private final FileRepository fileRepository ;
    private final FileRecordDTOMapper fileRecordDTOMapper ;

    public FileServiceImpl(FileRepository fileRepository, FileRecordDTOMapper fileRecordDTOMapper) {
        this.fileRepository = fileRepository;
        this.fileRecordDTOMapper = fileRecordDTOMapper;
    }

    private final String  FILE_PATH= Paths.get("").toAbsolutePath().resolve("src").resolve("main").resolve("resources").resolve("files") +"/";
    @Override
    public ResponseEntity<Object> uploadFile(@NotNull final MultipartFile file) throws IOException {
        log.info("file name : "+file.getName());
        validateUploadFile(file) ;
        final FileRecord fileRecord = handleFile(file);

        final FileRecordDTO fileRecordDTOResponse = fileRecordDTOMapper.apply(fileRecord); //transforming file to DTO
        return new ResponseEntity<>(fileRecordDTOResponse, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> uploadMultipleFiles(List<MultipartFile> files) throws IOException {
        List<FileRecordDTO> fileRecordDTOList = new ArrayList<>() ;

        for (MultipartFile file : files){

            final FileRecord fileRecord = handleFile(file) ;
            fileRecordDTOList.add(fileRecordDTOMapper.apply(fileRecord)) ;
        }
        return new ResponseEntity<>(fileRecordDTOList , HttpStatus.OK) ;
    }


    @Override
    public FileRecord getFileById(final Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(()-> new ResourceNotFoundCustomException("File not found "));
    }

    @Override
    public ResponseEntity<Object> downloadFile(final String fileName) throws IOException {
        // Encode the filename for safe use in HTTP headers
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        Path path = Paths.get(FILE_PATH + fileName);

        // Validate the file and check if it's available
        Resource resource = new UrlResource(path.toUri());
        validateDownloadFile(resource);

        // Read the file data into byte array
        byte[] fileDataBytes = Files.readAllBytes(path);

        // Setting headers for the file download
        HttpHeaders headers = new HttpHeaders();
        // Setting Content-Disposition with encoded filename
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename*=UTF-8''" + encodedFileName);
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileDataBytes.length));
        headers.add(HttpHeaders.CONTENT_TYPE, Files.probeContentType(path));

        return new ResponseEntity<>(fileDataBytes, headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getAllFilesByAthlete(final Long athleteId) {
        final List<FileRecordDTO> fileRecordDTOList = fileRepository.findByAthleteId(athleteId)
                .stream()
                .map(fileRecordDTOMapper)
                .toList();
        return new ResponseEntity<>(fileRecordDTOList , HttpStatus.OK) ;
    }

    @Override
    public ResponseEntity<Object> getAllFilesByEvent(final Long eventId) {
        final List<FileRecordDTO> fileRecordDTOList = fileRepository.findByEventId(eventId)
                .stream()
                .map(fileRecordDTOMapper)
                .toList();
        return new ResponseEntity<>(fileRecordDTOList , HttpStatus.OK) ;

    }

    @Override
    public ResponseEntity<Object> getAllDocumentFiles() {
        final List<FileRecordDTO> fileRecordDTOList = fileRepository.fetchAllDocumentFiles()
                .stream()
                .map(fileRecordDTOMapper)
                .toList();
        return new ResponseEntity<>(fileRecordDTOList , HttpStatus.OK) ;

    }

    @Override
    public void deleteFileById(final Long fileId) throws IOException{
        FileRecord fileToDelete = getFileById(fileId) ;
        // delete file from database
        fileRepository.deleteFileRecordByById(fileId);

        //delete file from server using NIO
        Path filePath = Paths.get(fileToDelete.getPath());
        Files.delete(filePath);

    }

    private void validateUploadFile(final MultipartFile file) {
        var originalFileName = file.getOriginalFilename() ;
        if (originalFileName == null) {
            throw new FileValidationCustomException("Original file name is null");
        }
        if (file.isEmpty()) {
            throw new FileValidationCustomException("File is empty");
        }
    }

    private void validateDownloadFile(Resource resource) {
        if (!resource.exists() || !resource.isFile()) {
            throw new ResourceNotFoundCustomException("File Requested To Download Is Not Found" );
        }
    }

    @Override
    public FileRecord handleFile(final MultipartFile file) throws IOException {
        //getting current authenticated Admin
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = (Admin) authentication.getPrincipal();

        String originalFileName = file.getOriginalFilename();
        String fileName = originalFileName.substring(0, originalFileName.indexOf('.'));
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        String fullName = fileName + System.currentTimeMillis() + extension ;
        String path = FILE_PATH + fileName + System.currentTimeMillis() + extension;

        //saving file
        final FileRecord fileRecord = FileRecord.builder()
                .name(fullName)
                .type(file.getContentType())
                .path(path)
                .uploadedAt(LocalDateTime.now())
                .uploadedBy(admin)
                .build();
        fileRepository.save(fileRecord);
        file.transferTo(new File(path));

        return fileRecord;
    }

    @Override
    public void saveFile(FileRecord fileRecord) {
        try{
            fileRepository.save(fileRecord);
        }catch (DataIntegrityViolationException e) {
            throw new DatabaseCustomException("error with saving this file");
        }
    }
}

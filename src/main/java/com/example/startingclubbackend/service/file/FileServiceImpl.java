package com.example.startingclubbackend.service.file;

import com.example.startingclubbackend.DTO.file.FileRecordDTO;
import com.example.startingclubbackend.DTO.file.FileRecordDTOMapper;
import com.example.startingclubbackend.model.file.FileRecord;
import com.example.startingclubbackend.model.user.Admin;
import com.example.startingclubbackend.repository.FileRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Paths;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileServiceImpl implements FileService{
    private final FileRepository fileRepository ;
    private final FileRecordDTOMapper fileRecordDTOMapper ;


    public FileServiceImpl(FileRepository fileRepository, FileRecordDTOMapper fileRecordDTOMapper) {
        this.fileRepository = fileRepository;
        this.fileRecordDTOMapper = fileRecordDTOMapper;
    }

    private final String  FILE_PATH= Paths.get("").toAbsolutePath().resolve("src").resolve("main").resolve("resources").resolve("files") +"/";
    @Override
    public ResponseEntity<Object> uploadFile(@NotNull final MultipartFile file)throws IOException {
        if(validateResponse(file) != null){
            return validateResponse(file) ;  //checking conditions
        }

        final FileRecord fileRecord = handleFile(file) ;

        final FileRecordDTO fileRecordDTOResponse = fileRecordDTOMapper.apply(fileRecord); //transforming file to DTO
        return new ResponseEntity<>(fileRecordDTOResponse, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> uploadMultipleFile(List<MultipartFile> files) throws IOException {
        List<FileRecordDTO> fileRecordDTOList = new ArrayList<>() ;

        for (MultipartFile file : files){
            if(validateResponse(file) != null){
                return validateResponse(file) ;  //checking conditions
            }
            final FileRecord fileRecord = handleFile(file) ;
            fileRecordDTOList.add(fileRecordDTOMapper.apply(fileRecord)) ;
        }
        return new ResponseEntity<>(fileRecordDTOList , HttpStatus.OK) ;
    }

    private ResponseEntity<Object> validateResponse(MultipartFile file) {
        var originalFileName = file.getOriginalFilename() ;
        if (originalFileName == null) {
            return new ResponseEntity<>("Original file name is null", HttpStatus.BAD_REQUEST);
        }

        if (file.isEmpty()) {
            return new ResponseEntity<>("File is empty", HttpStatus.BAD_REQUEST);
        }
        return null ;
    }

    private FileRecord handleFile(final MultipartFile file) throws IOException {
        //getting current authenticated Admin
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = (Admin) authentication.getPrincipal();

        String originalFileName = file.getOriginalFilename();
        String fileName = originalFileName.substring(0, originalFileName.indexOf('.'));
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        String path = FILE_PATH + fileName + System.currentTimeMillis() + extension;

        //saving file
        final FileRecord fileRecord = FileRecord.builder()
                .name(fileName)
                .type(file.getContentType())
                .path(path)
                .uploadedAt(LocalDateTime.now())
                .uploadedBy(admin)
                .build();
        fileRepository.save(fileRecord);
        file.transferTo(new File(path));

        return fileRecord;
    }
}

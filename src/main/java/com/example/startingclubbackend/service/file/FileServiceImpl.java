package com.example.startingclubbackend.service.file;

import com.example.startingclubbackend.DTO.file.FileRecordDTO;
import com.example.startingclubbackend.DTO.file.FileRecordDTOMapper;
import com.example.startingclubbackend.model.event.Event;
import com.example.startingclubbackend.model.file.FileRecord;
import com.example.startingclubbackend.model.user.Admin;
import com.example.startingclubbackend.repository.FileRepository;
import com.example.startingclubbackend.service.event.EventService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.plaf.ColorUIResource;
import java.io.File;
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
    private final EventService eventService;

    public FileServiceImpl(FileRepository fileRepository, FileRecordDTOMapper fileRecordDTOMapper, EventService eventService) {
        this.fileRepository = fileRepository;
        this.fileRecordDTOMapper = fileRecordDTOMapper;
        this.eventService = eventService;
    }

    private final String  FILE_PATH= Paths.get("").toAbsolutePath().resolve("src").resolve("main").resolve("resources").resolve("files") +"/";
    @Override
    public ResponseEntity<Object> uploadFile(@NotNull final MultipartFile file) throws IOException {
        if (validateResponse(file) != null) {
            return validateResponse(file);  //checking conditions
        }
        final FileRecord fileRecord = handleFile(file);

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

    @Override
    public ResponseEntity<Object> fetchAllFilesByEventId(final Long eventId) {
        List<FileRecord> currentFiles = fileRepository.fetchAllPhotosByEventId(eventId);
         if (currentFiles.isEmpty()){
             return new ResponseEntity<>("Sorry, there are no files for the specified event ",HttpStatus.NOT_FOUND);
         }
         //handling response
        List<FileRecordDTO> currentFilesResponse = currentFiles.stream()
                .map(fileRecordDTOMapper)
                .toList() ;
         return new ResponseEntity<>(currentFilesResponse , HttpStatus.OK) ;
    }

    @Override
    public ResponseEntity<Object> addFileToEventId(Long eventId, Long fileId) {
        Event currEvent = eventService.getEventById(eventId);
        FileRecord fileRecord = getFileById(fileId);
        if (fileRecord.getEvent().getId() == eventId){ //check if the event has already registered
            return new ResponseEntity<>("sorry , file already added to this event",HttpStatus.CONFLICT) ;
        }

        fileRecord.setEvent(currEvent);//adding event to file
        fileRepository.save(fileRecord);
        return new ResponseEntity<>("File added to event successfully", HttpStatus.OK);
    }

    @Override
    public FileRecord getFileById(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(()-> new IllegalArgumentException("File not found "));
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

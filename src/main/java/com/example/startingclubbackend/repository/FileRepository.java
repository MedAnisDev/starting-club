package com.example.startingclubbackend.repository;

import com.example.startingclubbackend.model.file.FileRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface FileRepository extends JpaRepository<FileRecord,Long> {

    @Query("select fe from FileRecord fe where fe.event.id = :eventId")
    List<FileRecord> fetchAllPhotosByEventId(@Param("eventId") final Long eventId);
}

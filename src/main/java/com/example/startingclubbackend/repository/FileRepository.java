package com.example.startingclubbackend.repository;

import com.example.startingclubbackend.model.file.FileRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface FileRepository extends JpaRepository<FileRecord,Long> {

    List<FileRecord> findByAthleteId(Long athleteId);
    @Modifying
    @Transactional
    @Query("DELETE FROM FileRecord f where f.id = :fileId")
    void deleteFileRecordByById(@Param("fileId") Long fileId) ;
}
package com.example.startingclubbackend.repository;

import com.example.startingclubbackend.model.file.FileRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface FileRepository extends JpaRepository<FileRecord,Long> {

}

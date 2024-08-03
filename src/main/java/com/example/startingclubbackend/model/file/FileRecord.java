package com.example.startingclubbackend.model.file;

import com.example.startingclubbackend.model.user.Admin;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Builder
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "files")
public class FileRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name" , nullable = false)
    private String name;

    @Column(name = "type" , nullable = false)
    private String type;

    @Column(name = "path" , nullable = false , unique = true)
    private String path;

    @Column(name = "uploaded_at" , nullable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "uploaded_by")
    private Admin uploadedBy ;

    public FileRecord() {

    }
}

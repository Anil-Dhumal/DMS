package com.dms.model;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "documents")
@Data
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Title is mandatory")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotBlank(message = "File path is required")
    private String filePath;

    @NotBlank(message = "File type is required")
    private String fileType;

    @Min(value = 10, message = "File size must be greater than 10")
    private long fileSize;

    @NotBlank(message = "Status is required")
    private String status;

    @ElementCollection
    @CollectionTable(name = "document_tags", joinColumns = @JoinColumn(name = "document_id"))
    private List<@NotBlank(message = "Tags must not be blank") String> tags;

    @Min(value = 1, message = "Version must be at least 1")
    private int version;
    
    private LocalDateTime uploadedAt;
    
    private String uploadedBy;

    @Column(nullable = true)
    private LocalDateTime lastModifiedAt;

    @Column(nullable = true)
    private String lastModifiedBy;

    private boolean isPublic;
}

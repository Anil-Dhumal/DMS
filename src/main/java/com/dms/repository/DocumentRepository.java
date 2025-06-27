package com.dms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dms.model.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {
	
	// Pagination + Filtering by title and uploadedBy (both optional)
    Page<Document> findByTitleContainingAndUploadedByContaining(String title, String uploadedBy, Pageable pageable);


	
}

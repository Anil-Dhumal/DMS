package com.dms.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.dms.fileUpload.DocumentUploadRequest;
import com.dms.model.Document;

public interface DocumentService {
	
	Document createDocument(Document document);
	Document updateDocument(Long id, Document document);
	void deleteDocument(Long id);
    Document getDocumentById(Long id);
    List<Document> getAllDocuments();
	Document saveFileWithMetadata(MultipartFile file, DocumentUploadRequest metadata) throws IOException;
	Page<Document> getDocumentsWithFilters(String title, String uploadedBy, int page, int size, String sortBy, String sortDir);

	
	Page<Document> findByTitleContaining(String title, Pageable pageable);
	Page<Document> findByUploadedByContaining(String uploadedBy, Pageable pageable);
	Page<Document> findByTitleContainingAndUploadedByContaining(String title, String uploadedBy, Pageable pageable);
	


    

}

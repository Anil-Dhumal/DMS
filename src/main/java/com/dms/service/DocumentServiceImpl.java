package com.dms.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dms.exceptionHandle.ResourceNotFoundException;
import com.dms.fileUpload.DocumentUploadRequest;
import com.dms.model.Document;
import com.dms.repository.DocumentRepository;
import com.dms.securityInfo.SecurityUtil;

@Service
public class DocumentServiceImpl implements DocumentService {

	private static final Logger logger = LoggerFactory.getLogger(DocumentServiceImpl.class);

	@Autowired
	private DocumentRepository documentRepository;

	@Autowired
	private MailService mailService;

	@Override
	public Document createDocument(Document document) {
		String currentUser = SecurityUtil.getCurrentUsername();

		// document.setUploadedAt(LocalDateTime.now());
		// logger.info(" Document created: {}", document.getTitle());

		document.setUploadedBy(currentUser);
		document.setUploadedAt(LocalDateTime.now());
		document.setStatus("ACTIVE"); // if using enum, adjust accordingly
		logger.info("📄 Document created by: {}", currentUser);

		return documentRepository.save(document);
	}

	@Override
	public Document updateDocument(Long id, Document document) {
		Document existing = documentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + id));

		// Only update allowed fields (not audit fields)
		existing.setTitle(document.getTitle());
		existing.setDescription(document.getDescription());
		existing.setFilePath(document.getFilePath());
		existing.setFileType(document.getFileType());
		existing.setFileSize(document.getFileSize());
		existing.setStatus(document.getStatus());
		existing.setTags(document.getTags());
		existing.setVersion(document.getVersion());
		existing.setPublic(document.isPublic());

		// Set audit fields from system (not from request body)
		existing.setLastModifiedBy(SecurityUtil.getCurrentUsername());
		existing.setLastModifiedAt(LocalDateTime.now());

		logger.info(" Document with ID {} updated by {}", id, existing.getLastModifiedBy());

		return documentRepository.save(existing);
	}

	@Override
	public void deleteDocument(Long id) {
		logger.info(" Document deleted with ID: {}", id);
		documentRepository.deleteById(id);

	}

	@Override
	public Document getDocumentById(Long id) {
		logger.info(" Fetching document with ID: {}", id);
		return documentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + id));
	}

	@Override
	public List<Document> getAllDocuments() {
		logger.info(" Fetching All document");
		return documentRepository.findAll();
	}

	// Implement Business Logic in Service for file upload & Send Email

	@Override
	public Document saveFileWithMetadata(MultipartFile file, DocumentUploadRequest metadata) throws IOException {

		// Store file locally
		String fileName = UUID.randomUUID() + " " + file.getOriginalFilename();
		Path uploadPath = Paths.get("uploads");
		Files.createDirectories(uploadPath);
		Path filePath = uploadPath.resolve(fileName);
		Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

		// Prepare document entity
		Document doc = new Document();
		doc.setTitle(metadata.getTitle());
		doc.setDescription(metadata.getDescription());
		doc.setTags(metadata.getTags());
		doc.setVersion(metadata.getVersion());
		doc.setPublic(metadata.isPublic());

		// Auto-generated metadata
		doc.setFilePath(filePath.toString());
		doc.setFileSize(file.getSize());

		String contentType = file.getContentType();
		if (contentType == null || !contentType.contains("/")) {
			contentType = Files.probeContentType(filePath);
			if (contentType == null) {
				contentType = "application/octet-stream";
			}
		}
		doc.setFileType(contentType);

		// Set audit fields BEFORE email
		doc.setUploadedAt(LocalDateTime.now());
		doc.setUploadedBy(SecurityUtil.getCurrentUsername());
		doc.setStatus("ACTIVE");

		// Save document to DB
		Document savedDoc = documentRepository.save(doc);

		// Send email notification AFTER setting correct fields
		mailService.sendEmail("dhumalanil9@gmail.com", " New Document Uploaded",
				"Document '" + savedDoc.getTitle() + "' was uploaded by " + savedDoc.getUploadedBy());

		logger.info("Email sent for document upload by {}", savedDoc.getUploadedBy());

		return savedDoc;
	}

	

	public Page<Document> getDocumentsWithPaginationAndFiltering(String title, String uploadedBy, int page, int size, String sortBy) {
	    Pageable pageable = (Pageable) PageRequest.of(page, size, Sort.by(sortBy));
	    return documentRepository.findByTitleContainingAndUploadedByContaining(title, uploadedBy, (Pageable) pageable);
	}

	@Override
	public Page<Document> findByTitleContaining(String title, org.springframework.data.domain.Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Document> findByUploadedByContaining(String uploadedBy,
			org.springframework.data.domain.Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Document> getDocumentsWithFilters(String title, String uploadedBy, int page, int size, String sortBy,
			String sortDir) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Document> findByTitleContainingAndUploadedByContaining(String title, String uploadedBy,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	
}

package com.dms.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dms.fileUpload.DocumentUploadRequest;
import com.dms.model.Document;
import com.dms.service.DocumentService;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/documents")
@Validated
public class DocumentController {

	@Autowired
	private DocumentService documentService;

	// Create Document
	@PostMapping
	@Operation(summary = "Create/upload a new Document")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Document Uploaded Successfully !!!"),
			@ApiResponse(responseCode = "400", description = "Invalid input Provided !!!"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error !!!"),
	})
	public ResponseEntity<Document> createDocument(@Valid @RequestBody Document document) {
		Document created = documentService.createDocument(document);
		return ResponseEntity.ok(created);
	}

	// get all document
	@GetMapping
	public ResponseEntity<List<Document>> getAllDocument() {
		
		return ResponseEntity.ok(documentService.getAllDocuments());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Document> getDocumentById(@PathVariable Long id) {
		return ResponseEntity.ok(documentService.getDocumentById(id));
	}

	// Update
	@PutMapping("/{id}")
	public ResponseEntity<Document> updateDocument(@PathVariable Long id, @RequestBody Document document) {
		return ResponseEntity.ok(documentService.updateDocument(id, document));
	}

	// Delete
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
		documentService.deleteDocument(id);
		return ResponseEntity.noContent().build();
	}
	
	//  Update Controller for Multipart Support
	@PostMapping(value= "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Document> uploadDocument(
			                      @RequestPart("file") MultipartFile file,
			                      @RequestPart("metadata") @Valid DocumentUploadRequest metadata) throws IOException
	{		
			Document saveDoc = documentService.saveFileWithMetadata(file, metadata);
		
			return ResponseEntity.ok(saveDoc);
		
	}
	
	//Implement File Download
	
	@GetMapping("/{id}/download")
	public ResponseEntity<Resource> downloadDocument(@PathVariable Long id) throws IOException {
	    Document doc = documentService.getDocumentById(id);

	    Path path = Paths.get(doc.getFilePath());
	    Resource resource = new UrlResource(path.toUri());

	    if (!resource.exists()) {
	        throw new FileNotFoundException("File not found: " + doc.getFilePath());
	    }
        System.out.println("file download");

	    return ResponseEntity.ok()
	            .contentType(MediaType.parseMediaType(doc.getFileType()))
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + path.getFileName() + "\"")
	            .body(resource);
	}
	
	// Pagination 
	
	@GetMapping("/paginated")
	public ResponseEntity<Page<Document>> getAllDocuments(
	        @RequestParam(required = false) String title,
	        @RequestParam(required = false) String uploadedBy,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "5") int size,
	        @RequestParam(defaultValue = "id") String sortBy,
	        @RequestParam(defaultValue = "asc") String sortDir) {

	    Page<Document> result = documentService.getDocumentsWithFilters(title, uploadedBy, page, size, sortBy, sortDir);
	    return ResponseEntity.ok(result);
	}

}

package com.dms.service;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.dms.exceptionHandle.ResourceNotFoundException;
import com.dms.model.Document;
import com.dms.repository.DocumentRepository;

@ExtendWith(MockitoExtension.class)
public class DocumentServiceImplTest {

    @InjectMocks
    private DocumentServiceImpl documentService;

    @Mock
    private DocumentRepository documentRepository;

    @Test
    public void testGetDocumentById_Success() {
        Document doc = new Document();
        doc.setId(1L);
        doc.setTitle("Test Document");

        Mockito.when(documentRepository.findById(1L)).thenReturn(Optional.of(doc));

        Document result = documentService.getDocumentById(1L);

        assertEquals("Test Document", result.getTitle());
    }

	@Test
    public void testGetDocumentById_NotFound() {
        Mockito.when(documentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            documentService.getDocumentById(1L);
        });
    }


}

	


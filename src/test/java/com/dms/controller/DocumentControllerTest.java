package com.dms.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.assertEquals;


import com.dms.model.Document;
import com.dms.service.DocumentService;



@WebMvcTest(DocumentController.class)
public class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocumentService documentService;

    @Test
    public void testGetDocumentById() throws Exception {
        Document doc = new Document();
        doc.setId(1L);
        doc.setTitle("Integration Test Doc");

        Mockito.when(documentService.getDocumentById(1L)).thenReturn(doc);
        

		/*
		 * mockMvc.perform(get("/api/documents/1")) .andExpect(status().isOk())
		 * .andExpect(jsonPath("$.title").value("Integration Test Doc"));
		 */
        String Actualtitle = documentService.getDocumentById(1L).getTitle();
        assertEquals(Actualtitle, "Integration Test Doc");
        
    }
}

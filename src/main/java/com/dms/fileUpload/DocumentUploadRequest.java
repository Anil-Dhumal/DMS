package com.dms.fileUpload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentUploadRequest {
	
	    private String title;
	    private String description;
	    private List<String> tags;
	    private boolean isPublic;
	    private int version;
	    
	    // Add other metadata fields as needed

}

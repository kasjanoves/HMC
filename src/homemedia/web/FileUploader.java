package homemedia.web;

import java.io.File;

public interface FileUploader {
	
	String getDescription();
	
	String getContentType();
	
	String getRelativePath();
	
	String getAbsolutePath();

	File upload() throws Exception;
}

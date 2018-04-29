package homemedia.web;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import homemedia.model.FileUtils;

public class ApacheFileUploadImpl implements FileUploader {
	
	private Random random = new Random();
	private String relPath = "";
	private String filePath = "";
	private String description = "";
	private String contentType = "";
	private ServletContext ctx;
	private HttpServletRequest request;
	
	public ApacheFileUploadImpl(ServletContext ctx, HttpServletRequest request) {
		this.ctx = ctx;
		this.request = request;
	}
	
	public String getDescription() {
		return description;
	}

	public String getContentType() {
		return contentType;
	}
	
	public String getRelativePath() {
		return relPath;
	}

	public String getAbsolutePath() {
		return filePath;
	}

	@Override
	public File upload() throws Exception {
		
		File UploadedFile = null;
		// ������ ����� ������� 
		DiskFileItemFactory factory = new DiskFileItemFactory();
 
		// ������������ ������ ������ � ������,
		// ��� ��� ���������� ������ ������ ������������ �� ���� �� ��������� ����������
		// ������������� ���� ��������
		factory.setSizeThreshold(1024*1024);
		
		// ������������� ��������� ����������
		File tempDir = (File)ctx.getAttribute("javax.servlet.context.tempdir");
		factory.setRepository(tempDir);
 
		//������ ��� ���������
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		//������������ ������ ������ ������� ��������� ��������� � ������
		//�� ��������� -1, ��� �����������. ������������� 10 ��������. 
		//upload.setSizeMax(1024 * 1024 * 10);
 
		List<FileItem> items = upload.parseRequest(request);
		Iterator<FileItem> iter = items.iterator();
			
		while (iter.hasNext()) {
			FileItem item = (FileItem) iter.next();
 
			if (item.isFormField()) {
		    	//���� ����������� ����� ������ �������� ����� �����			    	
		    	processFormField(item);
		    } else {
		    	//� ��������� ������ ������������� ��� ����
		    	UploadedFile = processUploadedFile(item);
		    	this.contentType = item.getContentType();
		    	//System.out.println(contentType);
		    }
		}
			
		return UploadedFile;
	}
	
	/**
	 * ��������� ���� �� �������, � ����� upload.
	 * ���� ����� ������ ���� ��� �������. 
	 * 
	 * @param item
	 * @throws Exception
	 */
	private File processUploadedFile(FileItem item) throws Exception{
		File uploadedFile = null;
		//�������� ����� ��� ���� �� ����� ���������
		do{
			String fileName = FileUtils.ExctractFileName(item.getName());
			relPath = "/upload/"+random.nextInt() + fileName;
			filePath = ctx.getRealPath(relPath);
			System.out.println(filePath);
			uploadedFile = new File(filePath);		
		}while(uploadedFile.exists());
		
		//������ ����
		uploadedFile.createNewFile();
		//���������� � ���� ������
		item.write(uploadedFile);
		return uploadedFile;
	}
 
	/**
	 * ������� �� ������� ��� ��������� � ��������
	 * @param item
	 */
	private void processFormField(FileItem item) {
		System.out.println(item.getFieldName()+"="+item.getString());
		this.description = item.getString();
	}

}

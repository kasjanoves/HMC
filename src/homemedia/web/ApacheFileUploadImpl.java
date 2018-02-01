package homemedia.web;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
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
		// Создаём класс фабрику 
		DiskFileItemFactory factory = new DiskFileItemFactory();
 
		// Максимальный буфера данных в байтах,
		// при его привышении данные начнут записываться на диск во временную директорию
		// устанавливаем один мегабайт
		factory.setSizeThreshold(1024*1024);
		
		// устанавливаем временную директорию
		File tempDir = (File)ctx.getAttribute("javax.servlet.context.tempdir");
		factory.setRepository(tempDir);
 
		//Создаём сам загрузчик
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		//максимальный размер данных который разрешено загружать в байтах
		//по умолчанию -1, без ограничений. Устанавливаем 10 мегабайт. 
		//upload.setSizeMax(1024 * 1024 * 10);
 
		List<FileItem> items = upload.parseRequest(request);
		Iterator<FileItem> iter = items.iterator();
			
		while (iter.hasNext()) {
			FileItem item = (FileItem) iter.next();
 
			if (item.isFormField()) {
		    	//если принимаемая часть данных является полем формы			    	
		    	processFormField(item);
		    } else {
		    	//в противном случае рассматриваем как файл
		    	UploadedFile = processUploadedFile(item);
		    	this.contentType = item.getContentType();
		    	//System.out.println(contentType);
		    }
		}
			
		return UploadedFile;
	}
	
	/**
	 * Сохраняет файл на сервере, в папке upload.
	 * Сама папка должна быть уже создана. 
	 * 
	 * @param item
	 * @throws Exception
	 */
	private File processUploadedFile(FileItem item) throws Exception{
		File uploadedFile = null;
		//выбираем файлу имя пока не найдём свободное
		do{
			String fileName = FileUtils.ExctractFileName(item.getName());
			relPath = "/upload/"+random.nextInt() + fileName;
			filePath = ctx.getRealPath(relPath);
			System.out.println(filePath);
			uploadedFile = new File(filePath);		
		}while(uploadedFile.exists());
		
		//создаём файл
		uploadedFile.createNewFile();
		//записываем в него данные
		item.write(uploadedFile);
		return uploadedFile;
	}
 
	/**
	 * Выводит на консоль имя параметра и значение
	 * @param item
	 */
	private void processFormField(FileItem item) {
		System.out.println(item.getFieldName()+"="+item.getString());
		this.description = item.getString();
	}

}

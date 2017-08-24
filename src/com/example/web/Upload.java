package com.example.web;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.example.model.DBTables;
import com.example.model.FileUtils;
import com.example.model.ImageMetadataReaderImpl;
import com.example.model.JDBCUtilities;
import com.example.model.MediaMetadataReader;
import com.example.model.MediaRow;
import com.example.model.MetadataRows;
import com.example.model.MetadataTagRow;
import com.example.web.WebUtils;


public class Upload extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Random random = new Random();
	private String relPath;
	private String filePath;
	private String description;
	private String contentType = "";
	        
    public Upload() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//проверяем является ли полученный запрос multipart/form-data
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (!isMultipart) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
 
		File UploadedFile = null;
		// Создаём класс фабрику 
		DiskFileItemFactory factory = new DiskFileItemFactory();
 
		// Максимальный буфера данных в байтах,
		// при его привышении данные начнут записываться на диск во временную директорию
		// устанавливаем один мегабайт
		factory.setSizeThreshold(1024*1024);
		
		// устанавливаем временную директорию
		File tempDir = (File)getServletContext().getAttribute("javax.servlet.context.tempdir");
		factory.setRepository(tempDir);
 
		//Создаём сам загрузчик
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		//максимальный размер данных который разрешено загружать в байтах
		//по умолчанию -1, без ограничений. Устанавливаем 10 мегабайт. 
		//upload.setSizeMax(1024 * 1024 * 10);
 
		try {
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
			    	contentType = item.getContentType();
			    	//System.out.println(contentType);
			    }
			}
			
			if(UploadedFile != null) {
				
				String mediaType = WebUtils.ExtractHeader(contentType);
				Map<String, Map<String, String>> MetadataMap = null;
				if(mediaType.equals("image")) {
					MediaMetadataReader imr = new ImageMetadataReaderImpl();
					MetadataMap = imr.getMetadata(UploadedFile);
				}
				
				MediaRow mRow = new MediaRow();
				mRow.setMediaType(mediaType);
				mRow.setDescription(description);
				mRow.setRelativePath(relPath);
				mRow.setSize(UploadedFile.length());
				mRow.setCreationDate(new Date(UploadedFile.lastModified()));
				
				JDBCUtilities util = (JDBCUtilities) getServletContext().getAttribute("DBUtils");
		    	Connection conn = util.getConnection();
		    	int MediaRowID;
		    	MediaRowID = DBTables.insertMediaRow(conn, mRow);
		    	MetadataRows mdataRows = new MetadataRows(MediaRowID, mediaType);
		    	mdataRows.fillItems(MetadataMap);
		    	DBTables.insertMetadataRows(conn, mdataRows);
				util.closeConnection(conn);
					
				request.setAttribute("mediaType", mediaType);
				request.setAttribute("filePath", filePath);
				request.setAttribute("description", description);
		        RequestDispatcher view = request.getRequestDispatcher("Result.jsp");
				view.forward(request, response);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}		
	}
	
	/**
	 * Сохраняет файл на сервере, в папке upload.
	 * Сама папка должна быть уже создана. 
	 * 
	 * @param item
	 * @throws Exception
	 */
	private File processUploadedFile(FileItem item) throws Exception {
		File uploadedFile = null;
		//выбираем файлу имя пока не найдём свободное
		do{
			String fileName = FileUtils.ExctractFileName(item.getName());
			relPath = "upload/"+random.nextInt() + fileName;
			filePath = getServletContext().getRealPath("/"+relPath);
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

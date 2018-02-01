package homemedia.controllers;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.drew.imaging.ImageProcessingException;

import homemedia.data.DBTables;
import homemedia.data.JDBCUtilities;
import homemedia.model.MediaMetadataReader;
import homemedia.model.Media;
import homemedia.model.MediaFactoriesSupplier;
import homemedia.model.MediaFactory;
import homemedia.model.MediaThumbnailCreator;
import homemedia.model.MetadataReadersFactoriesSupplier;
import homemedia.model.MetadataRows;
import homemedia.model.MetadataTag;
import homemedia.model.VideoThumbCreatorImpl;
import homemedia.web.ApacheFileUploadImpl;
import homemedia.web.FileUploader;
import homemedia.web.WebUtils;


public class UploadController extends HttpServlet {
	
	private static final long serialVersionUID = -5698239931406124986L;
		
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
 
		FileUploader uploader = new ApacheFileUploadImpl(getServletContext(), request);
		File UploadedFile = null;
		try {
			UploadedFile = uploader.upload();
		} catch (Exception e2) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
			
		if(UploadedFile != null) {
			
			String mediaType = WebUtils.ExtractHeader(uploader.getContentType());
			String thumbPath = "";
			MediaMetadataReader mreader = null;
			MetadataReadersFactoriesSupplier mReadersFactories = (MetadataReadersFactoriesSupplier) 
					getServletContext().getAttribute("metadataReadersFactories");
			MediaFactoriesSupplier mfactories = (MediaFactoriesSupplier)
					getServletContext().getAttribute("mediaFactories");
			
			Media media = mfactories.getFactory(mediaType).getInstance();
			media.setMediaType(mediaType);
			media.setDescription(uploader.getDescription());
			media.setPath(uploader.getRelativePath());
			if(media.isThumbnailed()){
				MediaThumbnailCreator tmbCreator = new VideoThumbCreatorImpl();
				thumbPath = tmbCreator.getThumbnail(UploadedFile, uploader.getRelativePath());
				media.setThumbnailPath(thumbPath);
			}
			media.setSize(UploadedFile.length());
			media.setCreationDate(new Date(UploadedFile.lastModified()));
			
			JDBCUtilities util = (JDBCUtilities) getServletContext().getAttribute("DBUtils");
			
	    	Connection conn = null;
	    	int MediaRowID = -1;
			try {
				conn = util.getConnection();
				MediaRowID = DBTables.insertMediaRow(conn, media);
			} catch (SQLException e1) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} finally {
				util.closeConnection(conn);
			}
	    	
	    	MetadataRows mdataRows = new MetadataRows(util, MediaRowID);
	    	mreader = mReadersFactories.getFactory(mediaType).getInstance();
	    		    	
	    	try {
	    		Map<MetadataTag, String> metadata = mreader.extractMetadata(UploadedFile);
				mdataRows.fillItems(metadata);
				DBTables.insertMetadataRows(conn, mdataRows);
			} catch (SQLException | ImageProcessingException e) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} finally {
				util.closeConnection(conn);
			}
	    					
			request.setAttribute("mediaType", mediaType);
			request.setAttribute("filePath", uploader.getAbsolutePath());
			request.setAttribute("description", uploader.getDescription());
	        RequestDispatcher view = request.getRequestDispatcher("Result.jsp");
			view.forward(request, response);
		}
			
			
	}


}

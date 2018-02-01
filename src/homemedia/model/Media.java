package homemedia.model;

import java.sql.SQLException;
import java.util.Date;

import javax.sql.RowSet;

public abstract class Media {
	
	protected int id;
	protected String MediaType;
	protected String Description;
	protected String Path;
	protected String ThumbnailPath;
	protected boolean isThumbnailed;
	protected long Size;
	protected Date CreationDate;
	public final static String TABLE_NAME = "MEDIA";
		
	public Media() {}
	
	public Media(RowSet rs) throws SQLException {
		id = rs.getInt("ID");
		Path = rs.getString("PATH");
		Description = rs.getString("DESCRIPTION");
		MediaType = rs.getString("TYPE");
				
	}

	public String getMediaType() {
		return MediaType;
	}

	public void setMediaType(String mediaType) {
		MediaType = mediaType;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getPath() {
		return Path;
	}

	public void setPath(String relativePath) {
		Path = relativePath;
	}

	public long getSize() {
		return Size;
	}

	public void setSize(long size) {
		Size = size;
	}

	public Date getCreationDate() {
		return CreationDate;
	}

	public void setCreationDate(Date creationDate) {
		CreationDate = creationDate;
	}
	
	public String getThumbnailPath() {
		return ThumbnailPath;
	}

	public void setThumbnailPath(String thumbnailPath) {
		ThumbnailPath = thumbnailPath;
	}

	public abstract String getPreviewTemplate(String ContextPath);

	public abstract String getViewTemplate(String ContextPath);
	
	public abstract String getDescriptionTemplate();

	public boolean isThumbnailed() {
		return isThumbnailed;
	}
		
}

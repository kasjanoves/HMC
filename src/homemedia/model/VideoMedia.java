package homemedia.model;

import java.sql.SQLException;

import javax.sql.RowSet;

public class VideoMedia extends Media {
	
	protected boolean isThumbnailed=true;
	private static final String PREVIEW_TEMPLATE = "<video src='%1$s' width='250' controls='controls'>"
			+ "Video not supported...<a href='%1$s'>Download video</a>.</video>";
	private static final String VIEW_TEMPLATE = "<video src='%1$s' controls='controls'>"
			+ "Video not supported...<a href='%1$s'>Download video</a>.</video>";
	
	public VideoMedia(RowSet rs) throws SQLException {
		super(rs);
		ThumbnailPath = rs.getString("THUMB_PATH");
	}

	public VideoMedia() {}

	@Override
	public String getPreviewTemplate(String ContextPath) {
		return String.format(PREVIEW_TEMPLATE, ContextPath+ThumbnailPath, Description, id);
	}

	@Override
	public String getViewTemplate(String ContextPath) {
		return String.format(VIEW_TEMPLATE, ContextPath+ThumbnailPath);
	}

	@Override
	public String getDescriptionTemplate() {
		return Description+" [video]";
	}

}

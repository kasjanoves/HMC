package homemedia.model;

import java.sql.SQLException;

import javax.sql.RowSet;

public class ImageMedia extends Media {
	
	private static final String PREVIEW_TEMPLATE = "<a href='View.do?id=%3$d' >"
			+ "<img src='%1$s' width='250' alt='%2$s'></a>";
	private static final String VIEW_TEMPLATE = "<img src='%1$s' alt='%2$s'>";
		
	public ImageMedia() {
		super();
	}
	
	public ImageMedia(RowSet rs) throws SQLException {
	    super(rs);
	}

	@Override
	public String getPreviewTemplate(String ContextPath) {
		return String.format(PREVIEW_TEMPLATE, ContextPath+Path, Description, id);
	}

	@Override
	public String getViewTemplate(String ContextPath) {
		return  String.format(VIEW_TEMPLATE, ContextPath+Path, Description);
	}

	@Override
	public String getDescriptionTemplate() {
		return Description;
	}

	
}

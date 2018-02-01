package homemedia.view;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.sql.RowSet;

import homemedia.data.JDBCUtilities;

public class MediaTagsTagHandler extends SimpleTagSupport{

	private RowSet mediaTags;
	private String mode="";
		
	public void setMediaTags(RowSet mediaTags) {
		this.mediaTags = mediaTags;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public void doTag() throws JspException, IOException {
		
		PageContext pageContext = (PageContext) getJspContext();
		JspWriter out = pageContext.getOut();
		    	
    	try {
			while(mediaTags.next()){
				String tagName = mediaTags.getString("NAME");
				int tagID = mediaTags.getInt("TAG_ID");
				if(mode.equals("selected"))
					out.print(String.format(TAG_SELECTED_TEMPLATE, tagName, tagID));
				else if(mode.equals("byMedia")) {
					int mediaID = mediaTags.getInt("MEDIA_ID");
					out.print(String.format(TAG_VIEW_TEMPLATE, tagName, mediaID, tagID));
				}
				else
					out.print(String.format(TAG_SELECT_TEMPLATE, tagName, tagID));
			}
		} catch (SQLException e) {
			JDBCUtilities.printSQLException(e);
		}finally {
			if (mediaTags != null)
				try {
					mediaTags.close();
				} catch (SQLException e) {
					JDBCUtilities.printSQLException(e);
				}
		}
    			
	}
	
	private static final String TAG_VIEW_TEMPLATE = "[%1$s <a href='RemoveTag.do?id=%2$d&tag_id=%3$d'>X</a>] ";
	private static final String TAG_SELECTED_TEMPLATE = "[%1$s <a href='SearchRemoveTag.do?tag_id=%2$d'>X</a>] ";
	private static final String TAG_SELECT_TEMPLATE = "[<a href='SearchAddTag.do?tag_id=%2$d'>%1$s</a>] ";
}

package homemedia.view;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.sql.RowSet;

public class TagSelectTagHandler extends SimpleTagSupport{
	
	private RowSet mediaTags;
	
	public void setMediaTags(RowSet mediaTags) {
		this.mediaTags = mediaTags;
	}

	public void doTag() throws JspException, IOException {
		PageContext pageContext = (PageContext) getJspContext();
		JspWriter out = pageContext.getOut();
		    	
    	out.print("<select name='tagSelect'>");
    	try {
			while(mediaTags.next()){
				String tagName = mediaTags.getString("NAME");
				int tagID = mediaTags.getInt("ID");
				out.print(String.format(OPTION_TEMPLATE, tagID, tagName));
			}
			out.print("</select>");
		} catch (SQLException e) {
			out.println(e);
		}finally {
			if (mediaTags != null)
				try {
					mediaTags.close();
				} catch (SQLException e) {
					out.println(e);
				}
		}
    			
	}
	
	private static final String OPTION_TEMPLATE = " <option value=%1$d>%2$s</option>";
}

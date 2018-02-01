package homemedia.view;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.sql.RowSet;

import homemedia.data.JDBCUtilities;
import homemedia.model.Media;
import homemedia.model.MediaFactoriesSupplier;

public class MediaViewTagHandler extends SimpleTagSupport {
	
	private RowSet mediaInfo;
	
	public void setMediaInfo(RowSet mediaInfo) {
		this.mediaInfo = mediaInfo;
	}
	public void doTag() throws JspException, IOException {
		PageContext pageContext = (PageContext) getJspContext();
		JspWriter out = pageContext.getOut();
		MediaFactoriesSupplier mfactories = (MediaFactoriesSupplier)
				pageContext.getServletContext().getAttribute("mediaFactories");
		 
		int id = -1;
    	String path = "";
    	String descr = "";
    	String type = "";
    	String table = "<table>";
    	try {
			while(mediaInfo.next()){
				String directory = mediaInfo.getString("DIRECTORY");
				String tag = mediaInfo.getString("TAG");
				String value = mediaInfo.getString("VALUE");
				table = table + String.format(ROW_TEMPLATE, directory, tag, value);
			}
			Media media = mfactories.getFactory(mediaInfo).getInstance();
			table = table + "</table>";
			out.print(media.getViewTemplate(pageContext.getServletContext().getContextPath()));
			out.print("<form action='Update.do' method='post'>");
			out.print(String.format("<input name='descr' type='text' value='%1$s'>", descr));
			out.print(String.format("<input name='id' type='hidden' value='%1$d'>", id));
			out.print("<input type='submit' value='apply'>");
			out.print("</form>");
			out.print(table);
			
					
		} catch (SQLException e) {
			out.println(e);
		} finally {
			if (mediaInfo != null)
				try {
					mediaInfo.close();
				} catch (SQLException e) {
					out.println(e);
				}
		}
        	
	}
	
	private static final String ROW_TEMPLATE = "<tr><td>%1$s</td><td>%2$s</td><td>%3$s</td></tr>";
	
}

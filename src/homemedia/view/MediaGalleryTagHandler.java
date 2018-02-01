package homemedia.view;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.sql.RowSet;

import homemedia.model.Media;
import homemedia.model.MediaFactoriesSupplier;

public class MediaGalleryTagHandler extends SimpleTagSupport{
	
	private RowSet MediaSet;
	
	public void setMediaSet(RowSet mediaSet) {
		this.MediaSet = mediaSet;
	}

	public void doTag() throws JspException, IOException {
		
		if(MediaSet==null) return;
		PageContext pageContext = (PageContext) getJspContext();
		JspWriter out = pageContext.getOut();
		MediaFactoriesSupplier mfactories = (MediaFactoriesSupplier)
				pageContext.getServletContext().getAttribute("mediaFactories");
				    	
    	int rowSize = 3;
    	int itemsCount = 0;
    	String descrRow = "<tr>";
    	
    	out.print("<table>");
    	out.print("<tr>");
    	try {
			while(MediaSet.next()){
				
				Media media = mfactories.getFactory(MediaSet).getInstance();
								
				out.print("<td align='center'>");
				out.print(media.getPreviewTemplate(pageContext.getServletContext().getContextPath()));
				out.print("</td>");
				descrRow = descrRow + "<td align='center'>" + media.getDescriptionTemplate() + "</td>";
				
				if(++itemsCount == rowSize) {
					out.print("</tr>");
					descrRow = descrRow + "</tr>";
					out.print(descrRow);
					descrRow = "<tr>";
					if(!MediaSet.isLast())
						out.print("<tr>");
					itemsCount = 0;
				}
				
			}
		} catch (SQLException e) {
			out.println(e);
			return;
		} finally {
			if (MediaSet != null)
				try {
					MediaSet.close();
				} catch (SQLException e) {
					out.println(e);
					return;
				}
		}
    	if(itemsCount>0) {
	    	descrRow = descrRow + "</tr>";
			out.print(descrRow);
	    	out.print("</tr>");
    	}
    	out.print("</table>");
        	
	}
		
}

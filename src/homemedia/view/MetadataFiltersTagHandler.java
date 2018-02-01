package homemedia.view;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.sql.RowSet;

import homemedia.model.MetadataTypes;

public class MetadataFiltersTagHandler extends SimpleTagSupport{
	
	private RowSet metadataTypes;
	
	public void setMetadataTypes(RowSet metadataTypes) {
		this.metadataTypes = metadataTypes;
	}
	
	public void doTag() throws JspException, IOException {
		if(metadataTypes==null) return;
		PageContext pageContext = (PageContext) getJspContext();
		JspWriter out = pageContext.getOut();
		
		String CurrentDest = "";
		try {
			while(metadataTypes.next()) {
				String Dest = metadataTypes.getString("DESTINATION");
				String Tag = metadataTypes.getString("TAG");
				String TagID = metadataTypes.getString("ID");
				String Type = metadataTypes.getString("TYPE");
				if(!Dest.equals(CurrentDest)) {
					CurrentDest = new String(Dest);
					out.print("<br>"+CurrentDest+"<br>");
				}
				//out.print(Tag);
				StringBuilder sb = new StringBuilder();
				MetadataTypes type = MetadataTypes.valueOf(Type.toUpperCase());
				for(String comp : type.Comparators())
					sb.append(String.format(OPTION_TEMPLATE, comp));
				out.print(String.format(FILTER_TEMPLATE, Tag, "Select"+TagID,
						sb.toString(), "Input"+TagID, type.getInputType()));
				out.print("<br>");
				
			}
			
    	} catch (SQLException e) {
    		out.println(e);
		}
    	    	
	}
	
	private static final String FILTER_TEMPLATE = "%1$s <select name='%2$s'>%3$s</select> "
			+ "<input name='%4$s' type='%5$s'>"
			+ " <input name='%4$s' type='%5$s'>";
	private static final String OPTION_TEMPLATE = " <option value=%1$s>%1$s</option>";

}


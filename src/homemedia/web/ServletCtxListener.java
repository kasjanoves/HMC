package homemedia.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import homemedia.data.DBTables;
import homemedia.data.JDBCUtilities;
import homemedia.model.MediaFactoriesSupplier;
import homemedia.model.MetadataReadersFactoriesSupplier;
import homemedia.model.MetadataTag;
import homemedia.model.ReqMetadataParser;

public class ServletCtxListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce)  { 
        
    	ServletContext servlctx = sce.getServletContext();
    	
    	JDBCUtilities util = null; 
    	Connection conn = null;
    	    	
    	try {
    		util = new JDBCUtilities(servlctx.getInitParameter("dbServerName"),
    				servlctx.getInitParameter("dbPortNumber"),
    				servlctx.getInitParameter("dbUserName"),
    				servlctx.getInitParameter("dbPassword"));
			conn = util.getConnection();
			DBTables.createAllTables(conn);
					
		} catch (SQLException e) {
			JDBCUtilities.printSQLException(e);
			e.printStackTrace();
			return;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}finally {
			if(conn != null)
				util.closeConnection(conn);
		}
    
    	ReqMetadataParser rmp = new ReqMetadataParser();
    	Set<MetadataTag> requiredMetadata;
		try {
			requiredMetadata = rmp.parse(servlctx.getRealPath("/WEB-INF/RequiredMetadata.xml"));
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
			return;
		}
		
		servlctx.setAttribute("metadataReadersFactories", new MetadataReadersFactoriesSupplier(requiredMetadata));
    	servlctx.setAttribute("mediaFactories", new MediaFactoriesSupplier());    	
		
		servlctx.setAttribute("DBUtils", util);
    	
    }
	
}

package com.example.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.example.model.DBTables;
import com.example.model.JDBCUtilities;
import com.example.model.MetadataTag;
import com.example.model.ReqMetadataParser;

public class ServletCtxListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce)  { 
        
    	ServletContext sc = sce.getServletContext();
    	JDBCUtilities util = new JDBCUtilities("root","root");
    	sc.setAttribute("DBUtils", util);
    	ReqMetadataParser rmp = new ReqMetadataParser();
    	
    	Connection conn = null;
    	    	
    	try {
			conn = util.getConnection();
			DBTables.createMediaTable(conn);
			DBTables.createMetadaTypesTable(conn);
			DBTables.createMetadataTable(conn);
			DBTables.createTagsTable(conn);
			DBTables.createMediaTagsTable(conn);
			Set<MetadataTag> requiredMetadata =
					rmp.parse(sc.getRealPath("/WEB-INF/RequiredMetadata.xml"));
			sc.setAttribute("requiredMetadata", requiredMetadata);
			
			//System.out.println(requiredMetadata);
		} catch (SQLException e) {
			JDBCUtilities.printSQLException(e);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(conn != null)
				util.closeConnection(conn);
		}
    	
    	
    }
	
}

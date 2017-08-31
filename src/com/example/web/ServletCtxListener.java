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
import com.example.model.ReqMetadataParser;

/**
 * Application Lifecycle Listener implementation class ServletCtxListener
 *
 */
public class ServletCtxListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce)  { 
        
    	ServletContext sc = sce.getServletContext();
    	JDBCUtilities util = new JDBCUtilities("root","root");
    	sc.setAttribute("DBUtils", util);
    	ReqMetadataParser rmp = new ReqMetadataParser();
    	    	
    	try {
			Connection conn = util.getConnection();
			DBTables.createMediaTable(conn);
			DBTables.createMetadaTypesTable(conn);
			DBTables.createMetadataTable(conn);
			Map<String,Map<String,Set<String>>> requiredMetadata =
					rmp.parse(sc.getRealPath("/WEB-INF/RequiredMetadata.xml"));
			sc.setAttribute("requiredMetadata", requiredMetadata);
			System.out.println(requiredMetadata);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	
    }
	
}

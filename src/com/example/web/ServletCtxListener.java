package com.example.web;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import com.example.model.DBTables;
import com.example.model.JDBCUtilities;

/**
 * Application Lifecycle Listener implementation class ServletCtxListener
 *
 */
public class ServletCtxListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce)  { 
        
    	ServletContext sc = sce.getServletContext();
    	JDBCUtilities util = new JDBCUtilities("root","root");
    	sc.setAttribute("DBUtils", util);
    	
    	try {
			Connection conn = util.getConnection();
			DBTables.createMediaTable(conn, "hmcatalog");
			DBTables.createMetadataTable(conn, "hmcatalog");
			DBTables.createMetadaTypesTable(conn, "hmcatalog");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    	
    	
    }
	
}

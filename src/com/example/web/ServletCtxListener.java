package com.example.web;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import com.example.model.DBTables;
import com.example.model.JDBCUtilities;

/**
 * Application Lifecycle Listener implementation class ServletCtxListener
 *
 */
public class ServletCtxListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public ServletCtxListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce)  { 
    	// nothing to do here
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce)  { 
        
    	JDBCUtilities util = new JDBCUtilities("root","root");
    	try {
			Connection conn = util.getConnection();
			DBTables.createMediaTable(conn, "hmcatalog");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }
	
}

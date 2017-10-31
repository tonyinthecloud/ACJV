package io.myutility.autocomplete;


import com.google.apphosting.api.ApiProxy;
import java.util.Map;
import javax.servlet.annotation.WebServlet;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.servlet.ServletException;
import java.sql.SQLException;
import java.util.logging.Logger;

import java.sql.Timestamp;
import java.util.Date;

@SuppressWarnings("serial")
@WebServlet(name = "CloudSQL", description = "CloudSQL: Write low order IP address to Cloud SQL",
urlPatterns = "/cloudsql")

public class DbUtility {
    private static Connection connection = null;

    public static Connection getConnection() throws Exception {
        if (connection != null)
            return connection;
        else {

            String instanceConnectionName = "modern-water-183115:us-central1:bestbuy-product-db";
            String databaseName = "productdb";
            String username = "tomcatdb";
            String mypassword = "tomcatdb";
            Connection conn = null;

            String url = String.format(
                "jdbc:google:mysql://%s/%s?user=%s&password=%s&useSSL=false", instanceConnectionName, databaseName, username,mypassword);

                try {
                    connection = DriverManager.getConnection(url);
                    return connection;
                    } catch (SQLException e) {
                        throw new ServletException(url + " Unable to connect to Cloud SQL 1" + url + e.getMessage());
                    } 
        }
    }
}
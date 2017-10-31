package io.myutility.autocomplete;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import io.myutility.autocomplete.DbUtility;

public class DataDao {
    private Connection connection;

    public DataDao() throws Exception {
        connection = DbUtility.getConnection();
    }

    public ArrayList<String> getFrameWork(String frameWork) {
        ArrayList<String> list = new ArrayList<String>();
        PreparedStatement ps = null;
        String data;
        
        try {
            ps = connection
                    .prepareStatement("SELECT * FROM product_table  WHERE name  LIKE ?");

            ps.setString(1, "%" + frameWork + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                data = rs.getString("name");
                System.out.println(data);                
                list.add(data);                                      
            }

            System.out.println("Good 2");  

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Bad 2");                                        
            
        }
        return list;
    }
}

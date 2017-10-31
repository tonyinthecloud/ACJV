package io.myutility.autocomplete;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.Gson;


public class ESJsonReader extends HttpServlet {
        private static final long serialVersionUID = 1L;

        protected void doGet(HttpServletRequest request,
                HttpServletResponse response) throws ServletException, IOException {

                response.setContentType("application/json");
                String term = request.getParameter("term");

                String urlHost = "http://35.202.98.31:9200";
                String esParam = "/catalogue/product/_search?pretty&filter_path=hits.hits._source&_source_include=name&q=name:";
                BufferedReader reader = null;
                StringBuffer buffer = new StringBuffer();
                String urlString = urlHost + esParam + "*" + term + "*";
                URL url = new URL(urlString);
                ArrayList<String> list = new ArrayList<String>();
                String searchList = null;

                
                        try {   
                                reader = new BufferedReader(new InputStreamReader(url.openStream()));     
                                                                        
                                JsonParser jsonParser = new JsonParser();                 
                                JsonElement product = jsonParser.parse(reader);            
                                JsonObject hits = product.getAsJsonObject();       
                                JsonObject hits2 = hits.getAsJsonObject("hits");  
                
                                if (hits2 != null)  {
                                        JsonArray hits2array = hits2.getAsJsonArray("hits");    
                                        for (JsonElement pa : hits2array) {
                                                JsonObject h = pa.getAsJsonObject();
                                                JsonObject source = h.getAsJsonObject("_source");
                                                String myobject = source.get("name").getAsString();
                                                list.add(myobject);
                                                searchList = new Gson().toJson(list);                      
                                        }
                                } else {
                                        searchList = "{ }";
                                }
               
                        } catch (Exception e) {
                                throw new ServletException( " Parser error"  + e.getMessage());
                        } finally {
                                response.getWriter().write(searchList);                                
                                reader.close();                                
                        }

        }
}
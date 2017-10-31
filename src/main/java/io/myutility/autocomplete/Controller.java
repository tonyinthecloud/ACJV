package io.myutility.autocomplete;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.myutility.autocomplete.DataDao;
import com.google.gson.Gson;

public class Controller extends HttpServlet {
        private static final long serialVersionUID = 1L;

        protected void doGet(HttpServletRequest request,
                HttpServletResponse response) throws ServletException, IOException {

                response.setContentType("application/json");

                try {
                        String term = request.getParameter("term");
                        System.out.println("Data from ajax call " + term);
                        
                        DataDao dataDao = new DataDao();
                        ArrayList<String> list = dataDao.getFrameWork(term);

                        String searchList = new Gson().toJson(list);                      
                        response.getWriter().write(searchList);
                        
                } catch (Exception e) {
                        System.err.println(e.getMessage());
                }
        }
}
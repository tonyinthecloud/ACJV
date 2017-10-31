package io.myutility.esloader;

import com.google.gson.*;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.Reader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.common.xcontent.XContentType;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.apache.http.HttpHost;

import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import java.nio.channels.Channels;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.cmd.Query;

import com.google.appengine.tools.remoteapi.RemoteApiInstaller;
import com.google.appengine.tools.remoteapi.RemoteApiOptions;

import static com.googlecode.objectify.ObjectifyService.ofy;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

public class ESLoader extends HttpServlet {

    static long serialVersionUID = -687981492884005033L;    

    private final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
    .initialRetryDelayMillis(10)
    .retryMaxAttempts(10)
    .totalRetryPeriodMillis(15000)
    .build());      

    protected void doGet(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {

        try {
 
            RemoteApiOptions options = new RemoteApiOptions()
            .server("cloudsql-dot-modern-water-183115.appspot.com", 443)
            .useApplicationDefaultCredential();

            RemoteApiInstaller installer = new RemoteApiInstaller();
            installer.install(options);

            ObjectifyService.begin();
            ObjectifyService.register(Product.class);

            GCSLoader gs = new GCSLoader();
            Product[] product = gs.loader();

            RestClient restClient = RestClient.builder(
                new HttpHost("35.202.98.31", 9200, "http"))
                .build();
            RestHighLevelClient client =
                new RestHighLevelClient(restClient);
                
            IndexRequest indexRequest = null;
            IndexResponse indexResponse = null;
            for (int i = 0; i < product.length; i++) {
                indexRequest = new IndexRequest("catalogue","product", Integer.toString(i));
                indexRequest.source(new Gson().toJson(product[i]), XContentType.JSON);
                indexResponse = client.index(indexRequest);
                ofy().save().entity(product[i]).now();

                }

            installer.uninstall();

        } catch (IOException e) {
            throw new ServletException(e.getMessage()); 
         } 

     }
}
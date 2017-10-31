package io.myutility.esloader;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.Reader;

import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import java.nio.channels.Channels;

 public class GCSLoader { 

        private static final int BUFFER_SIZE = 2 * 1024 * 1024;

        private final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
        .initialRetryDelayMillis(10)
        .retryMaxAttempts(10)
        .totalRetryPeriodMillis(15000)
        .build());  

        public Product[] loader() {

            GcsFilename fileName = new GcsFilename("gs-autocomp-bestbuy", "bby_products.json");
            GcsInputChannel readChannel = null;
            Reader reader = null;
            Product[] product = null;
            Gson gson = new Gson();

            try {
//                    readChannel = gcsService.openReadChannel(fileName, 0);
                    readChannel = gcsService.openPrefetchingReadChannel(fileName, 0, BUFFER_SIZE);
                    reader = new BufferedReader(Channels.newReader(readChannel, "UTF8"));
                    product = gson.fromJson(reader, Product[].class);
                    reader.close();                    

            } catch (Exception e) {
                    e.printStackTrace();
        }

        return product;
        }
    

}


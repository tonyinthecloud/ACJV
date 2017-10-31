package io.myutility.esloader;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Product {       
        @Id private String sku;
        @Index private String name; 
        private String Software;
        private Float price;
        private String upc;
        private String shipping;
        private String description;
        private String manufacturer;
        private String model;
        private String url;
        private String image;

}


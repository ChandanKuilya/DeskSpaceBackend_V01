package com.ck.deskspace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching // <--- ACTIVATES THE CACHE MANAGER
public class DeskSpaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeskSpaceApplication.class, args);
    }

}

package com.example.demo.lemoncash;

import com.example.demo.lemoncash.config.SpringConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder(SpringConfig.class);
        springApplicationBuilder.registerShutdownHook(true)
                .run(args);
    }

}
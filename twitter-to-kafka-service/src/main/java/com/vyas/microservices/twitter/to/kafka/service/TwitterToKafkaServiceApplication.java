package com.vyas.microservices.twitter.to.kafka.service;

import com.vyas.microservices.twitter.to.kafka.service.init.StreamInitializer;
import com.vyas.microservices.twitter.to.kafka.service.runner.StreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.vyas.microservices")
public class TwitterToKafkaServiceApplication implements CommandLineRunner{
    private static final Logger LOG = LoggerFactory.getLogger(TwitterToKafkaServiceApplication.class);
    
    private final StreamRunner streamRunner;
    private final StreamInitializer streamInitializer;
    
    public TwitterToKafkaServiceApplication(StreamRunner streamRunner, StreamInitializer initializer){
        this.streamInitializer = initializer;
        this.streamRunner = streamRunner;
    }
    
    public static void main(String[] arg){
        SpringApplication.run(TwitterToKafkaServiceApplication.class, arg);
    }
    
    @Override
    public void run(String... args) throws Exception{
        LOG.info("App starts");
        streamInitializer.init();
        streamRunner.start();
    }
}

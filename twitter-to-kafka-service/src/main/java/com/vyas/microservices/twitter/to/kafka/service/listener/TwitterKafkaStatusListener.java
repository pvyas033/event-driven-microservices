package com.vyas.microservices.twitter.to.kafka.service.listener;

import com.vyas.microservices.kafka.avro.model.TwitterAvroModel;
import com.vyas.microservices.config.KafkaConfigData;
import com.vyas.microservices.kafka.producer.service.KafkaProducer;
import com.vyas.microservices.twitter.to.kafka.service.transformer.TwitterStatusToAvroTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.StatusAdapter;

@Component
public class TwitterKafkaStatusListener extends StatusAdapter{
    private static final Logger LOG = LoggerFactory.getLogger(TwitterKafkaStatusListener.class);
    
    private final KafkaConfigData kafkaConfigData;
    private final KafkaProducer<Long,TwitterAvroModel> kafkaProducer;
    private final TwitterStatusToAvroTransformer twitterStatusToAvroTransformer;
    
    public TwitterKafkaStatusListener(KafkaConfigData kafkaConfigData, KafkaProducer<Long,TwitterAvroModel> kafkaProducer, TwitterStatusToAvroTransformer twitterStatusToAvroTransformer){
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaProducer = kafkaProducer;
        this.twitterStatusToAvroTransformer = twitterStatusToAvroTransformer;
    }
    
    @Override
    public void onStatus(Status status){
        
        LOG.info("Received status text {} sending to kafka topic {}", status.getText(), kafkaConfigData.getTopicName());
        TwitterAvroModel twitterAvroModel = twitterStatusToAvroTransformer.getTwitterAvroModelFromStatus(status);
        kafkaProducer.send(kafkaConfigData.getTopicName(), twitterAvroModel.getUserId(), twitterAvroModel);
        /*User user = status.getUser();
        
        // gets Username
        String username = status.getUser().getScreenName();
        System.out.println(username);
        String profileLocation = user.getLocation();
        System.out.println(profileLocation);
        long tweetId = status.getId();
        System.out.println(tweetId);
        String content = status.getText();
        System.out.println(content+"\n");
        LOG.info("Twitter status with text {}", status.getText());*/
    }
}

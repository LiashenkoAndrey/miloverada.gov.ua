package gov.milove;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Runner {
    public static void main(String[] args) {
        SpringApplication.run(Runner.class, args);
    }

    @Bean
    public MongoDatabase connect() {
        ConnectionString connectionString = new ConnectionString("mongodb://eu-life-rootinfo:uSVtE7VF7VGbQv5@ac-u9efhpg-shard-00-00.jhfxdna.mongodb.net:27017,ac-u9efhpg-shard-00-01.jhfxdna.mongodb.net:27017,ac-u9efhpg-shard-00-02.jhfxdna.mongodb.net:27017/?ssl=true&replicaSet=atlas-14ato7-shard-0&authSource=admin&retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        return mongoClient.getDatabase("test");
    }
}

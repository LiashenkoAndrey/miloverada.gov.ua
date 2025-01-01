package gov.milove.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Deprecated
@Configuration
//@EnableMongoRepositories(basePackages = "gov.milove.repositories.mongo")
public class MongoConfig {

//    @Bean
//    public MongoClient mongo() {
//        ConnectionString connectionString = new ConnectionString("mongodb://eu-life-rootinfo:uSVtE7VF7VGbQv5@ac-u9efhpg-shard-00-00.jhfxdna.mongodb.net:27017,ac-u9efhpg-shard-00-01.jhfxdna.mongodb.net:27017,ac-u9efhpg-shard-00-02.jhfxdna.mongodb.net:27017/?ssl=true&replicaSet=atlas-14ato7-shard-0&authSource=admin&retryWrites=true&w=majority");
//        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
//                .applyConnectionString(connectionString)
//                .build();
//
//        return MongoClients.create(mongoClientSettings);
//    }
//
//    @Bean
//    public MongoTemplate mongoTemplate() throws Exception {
//        return new MongoTemplate(mongo(), "test");
//    }
}

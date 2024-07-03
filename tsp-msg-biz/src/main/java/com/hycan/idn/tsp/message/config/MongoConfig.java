package com.hycan.idn.tsp.message.config;

import com.mongodb.ConnectionString;
import com.mongodb.ReadPreference;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

@Data
@ConfigurationProperties(prefix = "spring.data.mongodb")
@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    private String uri;

    private String database;

    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDatabaseFactory) {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDatabaseFactory);
        // 设置读从库优先
        mongoTemplate.setReadPreference(ReadPreference.secondaryPreferred());
        // 保存对象时，不添加 _class 字段
        MappingMongoConverter mongoMapping = (MappingMongoConverter) mongoTemplate.getConverter();
        mongoMapping.setTypeMapper(new DefaultMongoTypeMapper(null));
        mongoMapping.afterPropertiesSet();
        return mongoTemplate;
    }


    @Bean
    @RefreshScope
    public MongoDatabaseFactory mongoDbFactory(){
        return new SimpleMongoClientDatabaseFactory(new ConnectionString(this.getUri()));
    }

    @Override
    @Bean
    public MongoClient mongoClient(){
        return MongoClients.create(this.getUri());
    }

    @Override
    protected String getDatabaseName(){
        return "TEST";
    }
    @Override
    protected boolean autoIndexCreation() {
        return true;
    }
}

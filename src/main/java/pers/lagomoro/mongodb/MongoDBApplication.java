package pers.lagomoro.mongodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication(scanBasePackages="pers.lagomoro.mongodb")
public class MongoDBApplication {

    public static void main(String[] args) {
        SpringApplication.run(MongoDBApplication.class, args);
    }

}

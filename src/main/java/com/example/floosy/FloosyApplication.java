package com.example.floosy;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.annotation.RequestScope;
import java.util.UUID;


@SpringBootApplication
public class FloosyApplication {

    public static void main(String[] args) {
        SpringApplication.run(FloosyApplication.class, args);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    ExpenseDraft expenseDraft(){
        return new ExpenseDraft(shortId());
    }


    @Bean
    @RequestScope
    RequestInfo requestInfo() {
        return new RequestInfo(shortId());
    }

    private static String shortId(){
        return  UUID.randomUUID().toString().substring(0,8);
    }

record ExpenseDraft(String id){}

    static class RequestInfo{
        private final String id;
        RequestInfo(String id){
            this.id = id;
        }
        String id(){
            return id;
        }
    }
}

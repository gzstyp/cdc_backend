package com.fwtai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 Api启动器
*/
@EnableTransactionManagement
@SpringBootApplication
@EnableAsync
public class LaunchApi{

    public static void main(final String[] args){
        SpringApplication.run(LaunchApi.class,args);
    }
}
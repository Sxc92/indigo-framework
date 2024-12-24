package com.indigo.framework.cloud;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 史偕成
 * @title
 * @description
 * @create 2024-12-24 11:56
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@SpringBootApplication
@EnableDiscoveryClient
public @interface IndigoCloud {
}

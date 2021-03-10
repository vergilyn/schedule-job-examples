package com.vergilyn.examples.lts.client;

import com.github.ltsopensource.spring.boot.annotation.EnableJobClient;
import com.github.ltsopensource.spring.boot.annotation.EnableTaskTracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author vergilyn
 * @since 2021-03-10
 *
 * @see com.github.ltsopensource.spring.boot.JobClientAutoConfiguration
 */
@SpringBootApplication
@EnableJobClient
@EnableTaskTracker
public class LtsClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(LtsClientApplication.class, args);
	}
}

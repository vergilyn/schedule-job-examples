package com.vergilyn.examples.lts.client;

import com.github.ltsopensource.spring.boot.annotation.EnableJobClient;
import com.github.ltsopensource.spring.boot.annotation.EnableJobTracker;
import com.github.ltsopensource.spring.boot.annotation.EnableTaskTracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 为了方便测试，将 job-tracker(server)、job-client(client)、task-tracker(client) 整合到一个工程
 *
 * @author vergilyn
 * @since 2021-03-10
 *
 * @see com.github.ltsopensource.spring.boot.JobTrackerAutoConfiguration
 *
 * @see com.github.ltsopensource.spring.boot.JobClientAutoConfiguration
 * @see com.github.ltsopensource.spring.boot.TaskTrackerAutoConfiguration
 */
@SpringBootApplication
@EnableJobClient
@EnableTaskTracker
@EnableJobTracker
public class LtsClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(LtsClientApplication.class, args);
	}
}

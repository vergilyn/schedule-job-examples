package com.vergilyn.examples.lts.env;

import com.github.ltsopensource.spring.boot.annotation.EnableJobTracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 负责接收并分配任务，任务调度。
 *
 * @author vergilyn
 * @since 2021-03-08
 *
 * @see com.github.ltsopensource.spring.boot.JobTrackerAutoConfiguration
 */
@SpringBootApplication
@EnableJobTracker
public class LtsJobTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LtsJobTrackerApplication.class, args);
	}

}

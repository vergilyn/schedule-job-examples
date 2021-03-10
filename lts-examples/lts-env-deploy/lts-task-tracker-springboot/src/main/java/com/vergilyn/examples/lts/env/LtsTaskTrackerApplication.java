package com.vergilyn.examples.lts.env;

import com.github.ltsopensource.spring.boot.annotation.EnableTaskTracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 负责执行任务，执行完反馈给JobTracker。
 *
 * @author vergilyn
 * @since 2021-03-09
 *
 * @see com.github.ltsopensource.spring.boot.TaskTrackerAutoConfiguration
 */
@SpringBootApplication
@EnableTaskTracker
public class LtsTaskTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LtsTaskTrackerApplication.class, args);
	}
}

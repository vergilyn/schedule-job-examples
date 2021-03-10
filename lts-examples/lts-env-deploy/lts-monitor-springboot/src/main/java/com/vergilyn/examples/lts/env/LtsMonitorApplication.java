package com.vergilyn.examples.lts.env;

import com.github.ltsopensource.spring.boot.annotation.EnableMonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author vergilyn
 * @since 2021-03-05
 *
 * @see com.github.ltsopensource.spring.boot.MonitorAutoConfiguration
 */
@SpringBootApplication
@EnableMonitor
public class LtsMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(LtsMonitorApplication.class, args);
	}
}

package com.vergilyn.examples.lts.client;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = LtsClientApplication.class)
public abstract class AbstractLtsClientApplication {


	protected void preventExit(){
		try {
			new Semaphore(0).acquire();
		}catch (Exception e){

		}
	}

	protected void preventExit(long timeout, TimeUnit unit){
		try {
			new Semaphore(0).tryAcquire(timeout, unit);
		}catch (Exception e){

		}
	}
}

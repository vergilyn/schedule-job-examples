package com.vergilyn.examples.lts.client.schedule;

import com.github.ltsopensource.core.domain.Action;
import com.github.ltsopensource.core.domain.Job;
import com.github.ltsopensource.spring.boot.TaskTrackerAutoConfiguration;
import com.github.ltsopensource.spring.tasktracker.JobRunnerItem;
import com.github.ltsopensource.spring.tasktracker.LTS;
import com.github.ltsopensource.tasktracker.Result;
import com.github.ltsopensource.tasktracker.TaskTracker;
import com.github.ltsopensource.tasktracker.runner.RunnerPool;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * LTS 接收到 shutdown信号时：
 * <pre>
 *     - {@link TaskTrackerAutoConfiguration#destroy()}
 *     - {@link TaskTracker#stop()}
 *     - {@link RunnerPool#shutDown()}
 * </pre>
 *
 * 最终调用的是 {@link ExecutorService#shutdownNow()}，并未保证 正在执行的任务 会被正确执行完成！
 */
@Slf4j
@LTS
public class LtsShutdownTask extends AbstractLtsCronTask{
	public static final String LTS_ID = "test-lts-shutdown";

	AtomicInteger index = new AtomicInteger(0);

	@Override
	protected String getLtsTaskId() {
		return LTS_ID;
	}

	@Override
	protected String getLtsCronExpression() {
		return "0 0/1 * * * ?";
	}

	@JobRunnerItem(shardValue = LTS_ID)
	public Result handle(Job job) {
		String logPrefix = getLogPrefix();

		log.warn("{} BEGIN >>>> index: {}", logPrefix, index.get());

		Stopwatch stopwatch = Stopwatch.createStarted();

		boolean isContinue = true;
		while (isContinue){

			isContinue = stopwatch.elapsed(TimeUnit.SECONDS) <= 20;
		}

		log.warn("{} END >>>> index: {}", logPrefix, index.incrementAndGet());

		return new Result(Action.EXECUTE_SUCCESS, "开始执行任务:");
	}
}

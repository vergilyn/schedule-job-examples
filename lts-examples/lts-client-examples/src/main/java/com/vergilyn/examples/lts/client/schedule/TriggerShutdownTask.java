package com.vergilyn.examples.lts.client.schedule;

import com.github.ltsopensource.core.domain.Action;
import com.github.ltsopensource.core.domain.Job;
import com.github.ltsopensource.spring.tasktracker.JobRunnerItem;
import com.github.ltsopensource.spring.tasktracker.LTS;
import com.github.ltsopensource.tasktracker.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;

@Slf4j
@LTS
public class TriggerShutdownTask extends AbstractLtsCronTask{
	public static final String LTS_ID = "test-trigger-shutdown";

	@Autowired
	private GenericApplicationContext context;

	@Override
	protected String getLtsTaskId() {
		return LTS_ID;
	}

	@Override
	protected String getLtsCronExpression() {
		return "0 0 0 * * ? 2099";
	}

	@JobRunnerItem(shardValue = LTS_ID)
	public Result handle(Job job) {
		String logPrefix = getLogPrefix();

		log.warn("{} >>>> BEGIN", logPrefix);

		context.close();

		log.warn("{} >>>> END", logPrefix);

		return new Result(Action.EXECUTE_SUCCESS, "开始执行任务:");
	}
}

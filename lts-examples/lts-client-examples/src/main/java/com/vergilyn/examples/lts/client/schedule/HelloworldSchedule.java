package com.vergilyn.examples.lts.client.schedule;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.github.ltsopensource.core.domain.Action;
import com.github.ltsopensource.core.domain.Job;
import com.github.ltsopensource.jobclient.JobClient;
import com.github.ltsopensource.jobclient.domain.Response;
import com.github.ltsopensource.remoting.Channel;
import com.github.ltsopensource.remoting.protocol.RemotingCommand;
import com.github.ltsopensource.spring.tasktracker.JobDispatcher;
import com.github.ltsopensource.spring.tasktracker.JobRunnerItem;
import com.github.ltsopensource.spring.tasktracker.LTS;
import com.github.ltsopensource.tasktracker.Result;
import com.github.ltsopensource.tasktracker.processor.JobAskProcessor;
import com.github.ltsopensource.tasktracker.processor.JobPushProcessor;
import com.github.ltsopensource.tasktracker.processor.RemotingDispatcher;
import com.github.ltsopensource.tasktracker.runner.JobContext;
import com.github.ltsopensource.tasktracker.runner.JobRunnerDelegate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author vergilyn
 * @since 2021-03-10
 */
@LTS
@Slf4j
public class HelloworldSchedule implements InitializingBean {
	private static final String TASK_ID = "vergilyn-lts-helloworld";
	private static final String EXT_EXECUTE_RESULT = "execute_result";

	@Autowired
	private JobClient jobClient;
	@Value("${lts.tasktracker.node-group}")
	private String taskTrackerNodeGroup;

	/**
	 * `JobClient`: 负责提交任务给 JobTracker，再由JobTracker负责分发任务到 TaskTracker。
	 * @throws Exception
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		Job job = new Job();
		job.setTaskId(TASK_ID);
		job.setCronExpression("0/10 * * * * ?");
		job.setReplaceOnExist(true);

		// taskTrackerNodeGroup can not be empty!
		job.setTaskTrackerNodeGroup(taskTrackerNodeGroup);

		/**
		 * 完全等价
		 * @see Job#setParam(String, String)
		 * @see Job#setExtParams(Map)
		 */

		Response resp = jobClient.submitJob(job);

		log.info("job submit response: {}, msg: {}", resp.isSuccess(), resp.getMsg());

		List<Job> failedJobs = resp.getFailedJobs();
		if (failedJobs != null && !failedJobs.isEmpty()){
			log.error("submit failed Jobs: {}", JSON.toJSONString(failedJobs));
		}
	}

	/**
	 * `TaskTracker`: 接受来自JobTracker的任务。
	 *
	 * <p>
	 * exec sequence-diagram:
	 * <pre>
	 *   1. job-tracker command-request(netty): ?
	 *   2. task-tracker process-request: {@linkplain RemotingDispatcher#processRequest(Channel, RemotingCommand)}
	 *   3. request.code:
	 *      - PUSH_JOB(13): 分发任务，{@linkplain JobPushProcessor#processRequest(Channel, RemotingCommand)}
	 *      - JOB_ASK(15) : 询问任务，{@linkplain JobAskProcessor#processRequest(Channel, RemotingCommand)}
	 *   4. thread-pool-executor run:
	 *      {@linkplain JobRunnerDelegate#run()}
	 *        -> {@linkplain JobDispatcher#run(JobContext)}
	 *          -> {@linkplain HelloworldSchedule#execute(Job)}
	 * </pre>
	 */
	@JobRunnerItem(shardValue = TASK_ID)
	public Result execute(Job job) {
		LocalTime now = LocalTime.now();
		System.out.println("helloworld execute: " + now);

		/*
		 * EXECUTE_SUCCESS   : 执行成功,这种情况 直接反馈客户端
		 * EXECUTE_FAILED    : 执行失败,这种情况,直接反馈给客户端,不重新执行
		 * EXECUTE_LATER     : 稍后重新执行,这种情况, 不反馈客户端,稍后重新执行,不过有最大重试次数
		 * EXECUTE_EXCEPTION : 执行异常, 这中情况也会重试
		 */
		String executeResult = job.getParam(EXT_EXECUTE_RESULT);
		for (Action action : Action.values()){
			if (action.name().equalsIgnoreCase(executeResult)){
				return new Result(action, "helloworld-" + executeResult);
			}
		}

		return new Result(Action.EXECUTE_SUCCESS, "helloworld-success");
	}
}

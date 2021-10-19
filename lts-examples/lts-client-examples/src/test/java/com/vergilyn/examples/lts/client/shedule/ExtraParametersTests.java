package com.vergilyn.examples.lts.client.shedule;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.github.ltsopensource.core.domain.Action;
import com.github.ltsopensource.core.domain.Job;
import com.github.ltsopensource.jobclient.JobClient;
import com.github.ltsopensource.jobclient.domain.Response;
import com.github.ltsopensource.spring.tasktracker.JobRunnerItem;
import com.github.ltsopensource.spring.tasktracker.LTS;
import com.github.ltsopensource.tasktracker.Result;
import com.vergilyn.examples.lts.client.AbstractLtsClientApplication;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;

@Import(ExtraParametersTests.ExtraParametersSchedule.class)
public class ExtraParametersTests extends AbstractLtsClientApplication {

	private static final String TASK_ID = "vergilyn-lts-extraParameters";

	@Test
	public void test(){
		preventExit();
	}

	@LTS
	@Slf4j
	public static class ExtraParametersSchedule implements InitializingBean {
		@Autowired
		private JobClient jobClient;
		@Value("${lts.tasktracker.node-group}")
		private String taskTrackerNodeGroup;

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
			job.setParam("ep_begin_time", LocalDateTime.now().toString());


			Response resp = jobClient.submitJob(job);

			log.info("job submit response: {}, msg: {}", resp.isSuccess(), resp.getMsg());

			List<Job> failedJobs = resp.getFailedJobs();
			if (failedJobs != null && !failedJobs.isEmpty()){
				log.error("submit failed Jobs: {}", JSON.toJSONString(failedJobs));
			}
		}

		@JobRunnerItem(shardValue = TASK_ID)
		public Result execute(Job job) {
			System.out.printf("execute >>>> job: %s\n", JSON.toJSON(job));

			String epBeginTime = job.getParam("ep_begin_time");

			System.out.printf("ExtraParameters >>>> ep_begin_time: %s\n", epBeginTime);

			return new Result(Action.EXECUTE_SUCCESS, "success");
		}
	}
}

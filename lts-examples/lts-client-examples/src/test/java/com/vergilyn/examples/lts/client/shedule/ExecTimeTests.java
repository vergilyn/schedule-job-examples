package com.vergilyn.examples.lts.client.shedule;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
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

/**
 * 例如每天12点执行1次 `cron: 0 30 23 * * *`，查询数据范围是一天内`(2021-10-17 23:30:00, 2021-10-18 23:30:00]`。
 * 如果 某天(2021-10-15)未执行，现在(2021-10-18)期望通过 lts-admin 重新执行这一批数据。
 *
 * 又或者因为任务积压，真实的执行时间变成了第2天的 `00:05:00`，导致代码中如果是直接获取`now = new Date()`计算出来的数据范围有误。
 *
 * @author vergilyn
 * @since 2021-10-18
 */
@Import(ExecTimeTests.ExecTimeSchedule.class)
public class ExecTimeTests extends AbstractLtsClientApplication {
	private static final String TASK_ID = "vergilyn-lts-execTime";

	@Test
	public void test(){
		preventExit();
	}

	@LTS
	@Slf4j
	public static class ExecTimeSchedule implements InitializingBean {
		@Autowired
		private JobClient jobClient;
		@Value("${lts.tasktracker.node-group}")
		private String taskTrackerNodeGroup;

		@Override
		public void afterPropertiesSet() throws Exception {
			Job job = new Job();
			job.setTaskId(TASK_ID);
			job.setCronExpression("0 30 23 * * ?");
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

		@JobRunnerItem(shardValue = TASK_ID)
		public Result execute(Job job) {
			System.out.printf("execute >>>> job: %s\n", JSON.toJSON(job));

			// e.g. 1634657400000
			// 并不是`lts-admin, 等待执行的任务 > [触发]`时的“当前时间”，而是“编辑”页面中的“触发时间”（即根据cron计算出的下一次真实执行时间）
			Long triggerTimeMills = job.getTriggerTime();
			LocalDateTime triggerTime = LocalDateTime.ofEpochSecond(triggerTimeMills / 1000, 0,
			                                                                ZoneOffset.ofHours(8));
			System.out.printf("execute >>>> triggerTime: %s\n", triggerTime);

			LocalDateTime now = LocalDateTime.now();
			LocalTime localTime = LocalTime.of(23, 30, 0);
			LocalDateTime begin = LocalDateTime.of(now.minusDays(1).toLocalDate(), localTime);
			LocalDateTime end = LocalDateTime.of(now.toLocalDate(), localTime);

			System.out.printf("query >>>> begin: %s, end: %s\n", begin, end);

			return new Result(Action.EXECUTE_SUCCESS, "helloworld-success");
		}
	}
}

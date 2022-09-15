package com.vergilyn.examples.lts.client.schedule;

import com.alibaba.fastjson.JSON;
import com.github.ltsopensource.core.domain.Job;
import com.github.ltsopensource.jobclient.JobClient;
import com.github.ltsopensource.jobclient.domain.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author dingmaohai
 * @version v1.0
 * @since 2022/04/08 11:18
 */
@Slf4j
public abstract class AbstractLtsCronTask implements InitializingBean {
	@Value("${lts.tasktracker.node-group}")
	private String taskTrackerNodeGroup;

	@Autowired
	private JobClient jobClient;

	protected abstract String getLtsTaskId();
	protected abstract String getLtsCronExpression();

	protected String getLogPrefix(){
		return "[vergilyn]#lts-job#" + getLtsTaskId();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		submitLtsTask();
	}

	private void submitLtsTask(){
		String ltsTaskId = getLtsTaskId();

		Job job = new Job();
		job.setTaskId(ltsTaskId);
		// job.setParam("_taskShard", ltsTaskId);
		job.setCronExpression(getLtsCronExpression());
		job.setReplaceOnExist(true);

		job.setTaskTrackerNodeGroup(taskTrackerNodeGroup);

		Response response = jobClient.submitJob(job);

		String logPrefix = getLogPrefix();
		if (response.isSuccess()) {
			log.info("{} lts-task-submit success, resp >>>> {}", logPrefix, JSON.toJSONString(response));
		}else {
			log.error("{} lts-task-submit failure, resp >>>> {}", logPrefix, JSON.toJSONString(response));
		}
	}
}

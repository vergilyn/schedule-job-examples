package com.vergilyn.examples.lts.client;

import com.alibaba.fastjson.JSON;
import com.github.ltsopensource.core.domain.Job;
import com.github.ltsopensource.jobclient.JobClient;
import com.github.ltsopensource.jobclient.domain.Response;
import com.github.ltsopensource.spring.tasktracker.*;
import com.github.ltsopensource.tasktracker.runner.JobRunner;
import javassist.ClassPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author vergilyn
 * @since 2022-02-25
 *
 * @see com.vergilyn.examples.lts.client.shedule.HelloworldScheduleTests
 */
@LTS
@Slf4j
@SuppressWarnings("JavadocReference")
public abstract class AbstractLtsSchedule implements InitializingBean {
	@Autowired
	private JobClient jobClient;

	@Value("${lts.tasktracker.node-group}")
	private String taskTrackerNodeGroup;

	private String ltsTaskId;

	protected abstract String getLtsTaskId();

	protected abstract String getCornExpression();

	protected abstract void execute(Job job);

	protected void postProcessBeforeSubmitJob(Job job){
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.ltsTaskId = this.getLtsTaskId();

		submitJob(ltsTaskId);

		addJobRunner(ltsTaskId);
	}

	private void submitJob(String ltsTaskId){
		Assert.isTrue(StringUtils.hasText(ltsTaskId), "`ltsTaskId` must be has text.");

		String cornExpression = this.getCornExpression();
		Assert.isTrue(StringUtils.hasText(cornExpression), "`cornExpression` must be has text.");

		Job job = new Job();
		job.setTaskId(ltsTaskId);
		job.setCronExpression(cornExpression);
		job.setReplaceOnExist(true);

		// taskTrackerNodeGroup can not be empty!
		job.setTaskTrackerNodeGroup(taskTrackerNodeGroup);

		postProcessBeforeSubmitJob(job);

		Response resp = jobClient.submitJob(job);

		log.info("job submit response: {}, msg: {}", resp.isSuccess(), resp.getMsg());

		List<Job> failedJobs = resp.getFailedJobs();
		if (failedJobs != null && !failedJobs.isEmpty()){
			log.error("submit failed Jobs: {}", JSON.toJSONString(failedJobs));
		}
	}

	/**
	 * TODO 2022-02-25 暂时想到的几种方案
	 * 1. 实现 {@linkplain JobRunner}，并参考 {@linkplain JobDispatcher} 实现 shard 逻辑。<br/>
	 * 2. 还是用{@linkplain JobRunnerItem}，创建lts-bean 并{@linkplain JobRunnerHolder#addLTSBean(Object)}。主要问题是怎么创建符合的lts-bean  <br/>
	 *   - 2.1) 创建一个 job-runner-bean，动态修改 {@linkplain JobRunnerItem#shardValue()} 以满足需求 <br/>
	 *     2022-02-28，貌似无法做到 class-bean 级别动态修改注解 <br/>
	 *     <br/>
	 *   - 2.2) （与`2.1`类似），为 job-runner-method 添加动态添加注解 {@linkplain JobRunnerItem} <br/>
	 *     同`2.1)` 无法针对到 class-bean 级别 <br/>
	 *     <br/>
	 *   - 2.3) 运行时创建 job-runner，例如 dubbo的 proxy-class。（因为 动态操作annotation没有好的方式，所以直接动态创建一个class 作为 job-runner-class）
	 *         参考`org.apache.dubbo.common.bytecode.Proxy#getProxy(Class)`<br/>
	 *
	 * <p> 2022-02-28: <br/>
	 * 1) 不推荐动态修改注解的方式，几种方案都不理想。 <br/>
	 * 2) 相对动态修改注解，更推荐用 javassist 运行时生成，从而达到动态修改注解的效果。（参考 dubbo-proxy-lcass。弊端：会多生成N个class，并通过classloader加载） <br/>
	 * 3) 更推荐`1.` 参考{@linkplain JobDispatcher}，XXX 2022-02-28，具体怎么做还没理解！ <br/>
	 *
	 */
	private void addJobRunner(String ltsTaskId)  {
		Assert.isTrue(StringUtils.hasText(ltsTaskId), "`ltsTaskId` must be has text.");

		Method jobExecuteMethod = ReflectionUtils.findMethod(this.getClass(), "execute", Job.class);

		// JobRunnerHolder.addLTSBean(ltsJobRunnerProxy);
	}

	/**
	 * 利用 javassist 在运行时创建 job-runner-class。<br/>
	 *
	 * 参考 dubbo：<br/>
	 * - `org.apache.dubbo.common.bytecode.Proxy#getProxy(Class)` <br/>
	 * - `org.apache.dubbo.common.bytecode.ClassGenerator#newInstance(java.lang.ClassLoader)` <br/>
	 *
	 * @param ltsTaskId
	 */
	private void addJobRunnerByJavassist(String ltsTaskId){
		ClassPool pool = ClassPool.getDefault();

	}

	/**
	 * {@linkplain JobRunnerHolder#add(String, JobRunner)} 通过reflect调用未暴露的方法。
	 */
	private void addJonRunnerByReflect(String ltsTaskId) throws InvocationTargetException, IllegalAccessException {
		Method jobMethod = ReflectionUtils.findMethod(this.getClass(), "executeProxy", Job.class);
		JobRunner jobRunner = JobRunnerBuilder.build(this, jobMethod, jobMethod.getParameterTypes());

		Method method = ReflectionUtils.findMethod(JobRunnerHolder.class, "add", String.class, JobRunner.class);
		ReflectionUtils.makeAccessible(method);
		method.invoke(JobRunnerHolder.class, ltsTaskId, jobRunner);
	}

	private void executeProxy(Job job){
		execute(job);
	}
}

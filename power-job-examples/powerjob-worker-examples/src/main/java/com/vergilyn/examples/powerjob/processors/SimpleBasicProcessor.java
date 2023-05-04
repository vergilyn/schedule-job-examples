package com.vergilyn.examples.powerjob.processors;

import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;
import tech.powerjob.worker.log.OmsLogger;

import java.util.Optional;

public class SimpleBasicProcessor implements BasicProcessor {

    @Override
    public ProcessResult process(TaskContext context) throws Exception {

        OmsLogger logger = context.getOmsLogger();

        String logPrefix = "[SimpleBasicProcessor]";

        String jobParams = Optional.ofNullable(context.getJobParams()).orElse("S");
        logger.info("{} Current context: {}", logPrefix, context.getWorkflowContext());
        logger.info("{} jobParams: {}", logPrefix, jobParams);
        logger.info("{} InstanceParams: {}", logPrefix, context.getInstanceParams());

        // 测试中文问题 #581
        if (jobParams.contains("CN")) {
            return new ProcessResult(true, "任务成功啦！！！");
        }

        return jobParams.contains("F") ? new ProcessResult(false) : new ProcessResult(true, "yeah!");

    }
}
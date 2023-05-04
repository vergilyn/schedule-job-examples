package com.vergilyn.examples.powerjob.jobs;

import org.apache.commons.collections4.CollectionUtils;
import tech.powerjob.client.PowerJobClient;
import tech.powerjob.common.enums.*;
import tech.powerjob.common.model.LogConfig;
import tech.powerjob.common.request.http.SaveJobInfoRequest;
import tech.powerjob.common.request.query.JobInfoQuery;
import tech.powerjob.common.response.JobInfoDTO;
import tech.powerjob.common.response.ResultDTO;

import javax.annotation.Resource;
import java.util.List;

/**
 * 实际业务开发中，powerjob 的 `processor` 和 `job` 一般都是强绑定关系。
 * <br/> 为了避免繁琐的在代码中编写 `processor` 后，还需要通过 powerjob-console 去创建 `job`。
 * <br/> 因此通过提供的 OpenAPI 快速创建 `processor` 和 `job`
 *
 * @author vergilyn
 * @since 2023-04-27
 *
 * @see <a href="https://www.yuque.com/powerjob/guidence/olgyf0#0c4dccbf">【powerjob OpenAPI】创建/修改任务</a>
 *
 * @deprecated 2023-04-27，接受powerjob通过console 手动创建
 */
public abstract class AbstractPowerJob {

    @Resource
    private PowerJobClient jobClient;

    // 1. 避免重复创建
    // 2. 创建时机？ 启动时创建，还是启动后？是否需要阻止启动？

    protected SaveJobInfoRequest buildJobInfo(){
        SaveJobInfoRequest request = new SaveJobInfoRequest();
        // 新增 or 修改
        request.setId(null);

        request.setJobName(this.getClass().getSimpleName());
        request.setJobDescription(this.getClass().getName());

        // There is not need to set this property in PowerJob-client, as it would be set automatically.
        // request.setAppId();

        request.setJobParams("");
        request.setTimeExpressionType(TimeExpressionType.CRON);
        request.setTimeExpression("0/10 * * * * *");
        request.setExecuteType(ExecuteType.STANDALONE);
        request.setProcessorType(ProcessorType.BUILT_IN);
        request.setProcessorInfo(this.getClass().getName());
        // request.setMaxInstanceNum();
        // request.setConcurrency();
        // request.setInstanceTimeLimit();
        // request.setInstanceRetryNum();
        // request.setTaskRetryNum();
        // request.setMinCpuCores();
        // request.setMinMemorySpace();
        // request.setMinDiskSpace();
        // request.setEnable();
        // request.setDesignatedWorkers();
        // request.setMaxWorkerCount();
        // request.setNotifyUserIds();
        // request.setExtra();
        // request.setDispatchStrategy();
        // request.setLifeCycle();
        // request.setAlarmConfig();
        // request.setTag();

        // see: `tech.powerjob.worker.log.OmsLoggerFactory#build(...)`
        LogConfig logConfig = new LogConfig();
        logConfig.setType(LogType.LOCAL_AND_ONLINE.getV());
        logConfig.setLevel(LogLevel.INFO.getV());
        logConfig.setLoggerName(null);
        request.setLogConfig(logConfig);

        return request;
    }

    protected boolean checkDuplicate(SaveJobInfoRequest request){
        // 如果worker集群部署，每次启动2个服务实例时，怎么处理并发造成的重复？
        JobInfoQuery query = new JobInfoQuery();
        query.setJobNameEq(request.getJobName());

        ResultDTO<List<JobInfoDTO>> resp = jobClient.queryJob(query);

        return resp.isSuccess() && CollectionUtils.isNotEmpty(resp.getData());
    }
}

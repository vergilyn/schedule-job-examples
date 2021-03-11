# lts-client-examples

为了方便测试，将LTS的3个核心整合到一起：
    提交(job-client) --> 分发(jobt-racker) -->执行(task-tracker) 任务。

扩展工程：`lts-admin`、`lts-monitor`。see: [lts-env-deploy](../lts-env-deploy)

## job-client

## task-tracker
exec sequence-diagram
```
1. job-tracker command-request(netty): ?
2. task-tracker process-request: {@linkplain RemotingDispatcher#processRequest(Channel, RemotingCommand)}
3. request.code:
   - PUSH_JOB(13): 分发任务，{@linkplain JobPushProcessor#processRequest(Channel, RemotingCommand)}
   - JOB_ASK(15) : 询问任务，{@linkplain JobAskProcessor#processRequest(Channel, RemotingCommand)}
4. thread-pool-executor run:
   {@linkplain JobRunnerDelegate#run()}
     -> {@linkplain JobDispatcher#run(JobContext)}
       -> {@linkplain HelloworldSchedule#execute(Job)}
```

## TODO
1. listener
- com.github.ltsopensource.core.listener.MasterChangeListener
- com.github.ltsopensource.core.listener.NodeChangeListener
- com.github.ltsopensource.core.registry.NotifyListener

2. job-client 任务执行反馈结果处理
- com.github.ltsopensource.jobclient.support.JobCompletedHandler

3. 日志上报 JobTracker
```
BizLogger bizLogger = LtsLoggerFactory.getBizLogger();
// 会发送到 LTS (JobTracker上)
bizLogger.info("测试，业务日志啊啊啊啊啊");
```

## Q&A
1. lts-admin \[任务队列管理] 中无法看到 `taskId: vergilyn-lts-helloworld`。
通过观察console，可以看到该TASK正常运行，问题？

2. lts-admin \[任务添加] 如何使用，及使用场景？

3. lts-admin \[暂定任务] 如何使用，及使用场景？
4. lts/lts-admin 中 如何手动cancel-task？
5. jon-tracker `listen.port = 35001` 含义？

6.node-group(节点组) 含义、用途、关联？
- task-tracker 提交节点组
- job-client 执行节点组

> task-tracker  
> 同一个节点组中的任何节点都是对等的，等效的，对外提供相同的服务。  
> 譬如TaskTracker中有10个nodeGroup都是send_msg的节点组，专门执行发送短信的任务。  
> 每个节点组中都有一个master节点，这个master节点是由LTS动态选出来的，  
> 当一个master节点挂掉之后，LTS会立马选出另外一个master节点，框架提供API监听接口给用户。

> job-client
> ？？？？


7. cluster-name 含义和用途？
- lts-admin
- lts-monitor
- lts-job-tracker
- lts-task-tracker
- client
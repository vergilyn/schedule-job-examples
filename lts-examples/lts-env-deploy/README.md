# lts-env-deploy

mysql: 127.0.0.1:3306
registry: `zookeeper://127.0.0.1:2181`
cluster-name: vergilyn-lts-cluster
fail-store: (JobClient/TaskTracker) leveldb(default), berkeleydb, rocksdb, mapdb

|              | required | database           | node-group                      | listen-port | fail-store | job   | queue |
|:-------------|:---------|:-------------------|:--------------------------------|:------------|:-----------|:------|:------|
| admin        | option   | dev_lts_admin      |                                 |             |            |       |       |
| monitor      | option   | dev_lts_monitor    |                                 |             |            | mysql | mysql |
| job-tracker  | required | dev_lts_jobtracker |                                 | 35001       |            | mysql | mysql |
| task-tracker | required |                    | vergilyn-tasktracker-node-group |             | mapdb      |       |       |
| job-client   | required |                    | vergilyn-jobclient-node-group   |             | mapdb      |       |       |

1. 是否在`lts-admin`内嵌启动`lts-monitor`(monitor服务也可以单独启动): `lts.monitorAgent.enable=true`

2. **必须的服务**
- job-client: 提交任务给 job-tracker。（一般内嵌到业务代码）
- job-tracker: 接收 job-client 任务，并进行任务调度。（一般单独部署）
- task-tracker: 负责执行 job-tracker 分发的任务。（一般跟job-client一起内嵌到业务代码）





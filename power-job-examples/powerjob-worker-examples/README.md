# powerjob-worker-examples

参考：<https://github.com/PowerJob/PowerJob/tree/v4.3.2/powerjob-worker-samples>

1) 首先，需要去 powerjob-console 手动创建“执行应用注册信息”；
2) 编写 worker 的 `processor` 代码；
3) 通过 powerjob-console 的`任务管理 -> 新建任务`，配置`2)`中编写的`processor`的执行规则，例如 CRON规则、任务参数等。

**个人：**  
感觉`1) & 3)`的手动方式不友好。可以通过OpenAPI扩展，但是有几个问题需要注意：
- `1)`没有提供 OpenAPI。需要找到console请求的 http接口创建；（并且只有新项目第1次启动时，才需要创建，所以还是建议去console手动创建）
- worker一般都是多实例部署，怎么避免并发创建job？
- 大多数情况下，`2)`也只会在**第1次启动时**去新增job，偶尔可能会调整CRON等。所以也没必要通过 OpenAPI 在worker启动时去自动处理这部分逻辑。

**结论：** 接受powerjob的默认使用方式，都通过console 手动创建 `执行服务注册信息` 和 `任务 job`！
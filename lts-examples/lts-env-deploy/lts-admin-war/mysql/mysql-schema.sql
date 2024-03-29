####  lts-admin
CREATE TABLE IF NOT EXISTS `lts_admin_node_onoffline_log`
(
    `id`            int(11) unsigned NOT NULL AUTO_INCREMENT,
    `log_time`      timestamp        NULL DEFAULT NULL,
    `event`         varchar(32)           DEFAULT NULL,
    `node_type`     varchar(16)           DEFAULT NULL,
    `cluster_name`  varchar(64)           DEFAULT NULL,
    `ip`            varchar(64)           DEFAULT NULL,
    `port`          int(11)               DEFAULT NULL,
    `host_name`     varchar(64)           DEFAULT NULL,
    `group`         varchar(64)           DEFAULT NULL,
    `create_time`   bigint(20)            DEFAULT NULL,
    `threads`       int(11)               DEFAULT NULL,
    `identity`      varchar(64)           DEFAULT NULL,
    `http_cmd_port` int(11)               DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_log_time` (`log_time`),
    KEY `idx_event` (`event`),
    KEY `idx_identity` (`identity`),
    KEY `idx_group` (`group`)
);


####  lts-monitor
CREATE TABLE IF NOT EXISTS `lts_admin_job_client_monitor_data`
(
    `id`                    int(11) unsigned NOT NULL AUTO_INCREMENT,
    `gmt_created`           bigint(20)       NULL DEFAULT NULL,
    `node_group`            varchar(64)           DEFAULT NULL,
    `identity`              varchar(64)           DEFAULT NULL,
    `submit_success_num`    bigint(20)            DEFAULT NULL,
    `submit_failed_num`     bigint(11)            DEFAULT NULL,
    `fail_store_num`        bigint(20)            DEFAULT NULL,
    `submit_fail_store_num` bigint(20)            DEFAULT NULL,
    `handle_feedback_num`   bigint(20)            DEFAULT NULL,
    `timestamp`             bigint(20)       NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_timestamp` (`timestamp`),
    KEY `idx_identity` (`identity`),
    KEY `idx_node_group` (`node_group`)
);

CREATE TABLE IF NOT EXISTS `lts_admin_job_tracker_monitor_data`
(
    `id`                    int(11) unsigned NOT NULL AUTO_INCREMENT,
    `gmt_created`           bigint(20)       NULL DEFAULT NULL,
    `identity`              varchar(64)           DEFAULT NULL,
    `receive_job_num`       bigint(20)            DEFAULT NULL,
    `push_job_num`          bigint(20)            DEFAULT NULL,
    `exe_success_num`       bigint(20)            DEFAULT NULL,
    `exe_failed_num`        bigint(11)            DEFAULT NULL,
    `exe_later_num`         bigint(20)            DEFAULT NULL,
    `exe_exception_num`     bigint(20)            DEFAULT NULL,
    `fix_executing_job_num` bigint(20)            DEFAULT NULL,
    `timestamp`             bigint(20)       NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_timestamp` (`timestamp`),
    KEY `idx_identity` (`identity`)
);

CREATE TABLE IF NOT EXISTS `lts_admin_jvm_gc`
(
    `id`                             int(11) unsigned NOT NULL AUTO_INCREMENT,
    `gmt_created`                    bigint(20)       NULL DEFAULT NULL,
    `identity`                       varchar(64)           DEFAULT NULL,
    `timestamp`                      bigint(20)       NULL DEFAULT NULL,
    `node_type`                      varchar(32)      NULL DEFAULT NULL,
    `node_group`                     varchar(64)      NULL DEFAULT NULL,
    `young_gc_collection_count`      bigint(20)       NULL DEFAULT NULL,
    `young_gc_collection_time`       bigint(20)       NULL DEFAULT NULL,
    `full_gc_collection_count`       bigint(20)       NULL DEFAULT NULL,
    `full_gc_collection_time`        bigint(20)       NULL DEFAULT NULL,
    `span_young_gc_collection_count` bigint(20)       NULL DEFAULT NULL,
    `span_young_gc_collection_time`  bigint(20)       NULL DEFAULT NULL,
    `span_full_gc_collection_count`  bigint(20)       NULL DEFAULT NULL,
    `span_full_gc_collection_time`   bigint(20)       NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_identity` (`identity`),
    KEY `idx_timestamp` (`timestamp`)
);

CREATE TABLE IF NOT EXISTS `lts_admin_jvm_memory`
(
    `id`                        int(11) unsigned NOT NULL AUTO_INCREMENT,
    `gmt_created`               bigint(20)       NULL DEFAULT NULL,
    `identity`                  varchar(64)           DEFAULT NULL,
    `timestamp`                 bigint(20)       NULL DEFAULT NULL,
    `node_type`                 varchar(32)      NULL DEFAULT NULL,
    `node_group`                varchar(64)      NULL DEFAULT NULL,
    `heap_memory_committed`     bigint(20)       NULL DEFAULT NULL,
    `heap_memory_init`          bigint(20)       NULL DEFAULT NULL,
    `heap_memory_max`           bigint(20)       NULL DEFAULT NULL,
    `heap_memory_used`          bigint(20)       NULL DEFAULT NULL,
    `non_heap_memory_committed` bigint(20)       NULL DEFAULT NULL,
    `non_heap_memory_init`      bigint(20)       NULL DEFAULT NULL,
    `non_heap_memory_max`       bigint(20)       NULL DEFAULT NULL,
    `non_heap_memory_used`      bigint(20)       NULL DEFAULT NULL,
    `perm_gen_committed`        bigint(20)       NULL DEFAULT NULL,
    `perm_gen_init`             bigint(20)       NULL DEFAULT NULL,
    `perm_gen_max`              bigint(20)       NULL DEFAULT NULL,
    `perm_gen_used`             bigint(20)       NULL DEFAULT NULL,
    `old_gen_committed`         bigint(20)       NULL DEFAULT NULL,
    `old_gen_init`              bigint(20)       NULL DEFAULT NULL,
    `old_gen_max`               bigint(20)       NULL DEFAULT NULL,
    `old_gen_used`              bigint(20)       NULL DEFAULT NULL,
    `eden_space_committed`      bigint(20)       NULL DEFAULT NULL,
    `eden_space_init`           bigint(20)       NULL DEFAULT NULL,
    `eden_space_max`            bigint(20)       NULL DEFAULT NULL,
    `eden_space_used`           bigint(20)       NULL DEFAULT NULL,
    `survivor_committed`        bigint(20)       NULL DEFAULT NULL,
    `survivor_init`             bigint(20)       NULL DEFAULT NULL,
    `survivor_max`              bigint(20)       NULL DEFAULT NULL,
    `survivor_used`             bigint(20)       NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_identity` (`identity`),
    KEY `idx_timestamp` (`timestamp`)
);

CREATE TABLE IF NOT EXISTS `lts_admin_jvm_thread`
(
    `id`                         int(11) unsigned NOT NULL AUTO_INCREMENT,
    `gmt_created`                bigint(20)       NULL DEFAULT NULL,
    `identity`                   varchar(64)           DEFAULT NULL,
    `timestamp`                  bigint(20)       NULL DEFAULT NULL,
    `node_type`                  varchar(32)      NULL DEFAULT NULL,
    `node_group`                 varchar(64)      NULL DEFAULT NULL,
    `daemon_thread_count`        int(11)          NULL DEFAULT NULL,
    `thread_count`               int(11)          NULL DEFAULT NULL,
    `total_started_thread_count` bigint(20)       NULL DEFAULT NULL,
    `dead_locked_thread_count`   int(11)          NULL DEFAULT NULL,
    `process_cpu_time_rate`      double                DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_identity` (`identity`),
    KEY `idx_timestamp` (`timestamp`)
);

CREATE TABLE IF NOT EXISTS `lts_admin_task_tracker_monitor_data`
(
    `id`                 int(11) unsigned NOT NULL AUTO_INCREMENT,
    `gmt_created`        bigint(20)       NULL DEFAULT NULL,
    `node_group`         varchar(64)           DEFAULT NULL,
    `identity`           varchar(64)           DEFAULT NULL,
    `exe_success_num`    bigint(20)            DEFAULT NULL,
    `exe_failed_num`     bigint(11)            DEFAULT NULL,
    `exe_later_num`      bigint(20)            DEFAULT NULL,
    `exe_exception_num`  bigint(20)            DEFAULT NULL,
    `total_running_time` bigint(20)            DEFAULT NULL,
    `timestamp`          bigint(20)       NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_timestamp` (`timestamp`),
    KEY `idx_identity` (`identity`),
    KEY `idx_node_group` (`node_group`)
);
# xxl-job-examples-official

- [xxl-job]: include samples
- [xxl-job-docs]
- [xxl-job-community]

[xxl-job]: https://github.com/xuxueli/xxl-job
[xxl-job-docs]: https://www.xuxueli.com/xxl-job/
[xxl-job-community]: https://www.xuxueli.com/page/community.html


## xxl-job-admin

1. init mysql table
> [MySQL 建索引时 Specified key was too long; max key length is 767 bytes 错误的处理](https://blog.csdn.net/a1173537204/article/details/88039245)  
>   
> SHOW variables like 'innodb_large_prefix';  
> SET GLOBAL INNODB_LARGE_PREFIX = ON;  
>   
> SHOW variables like 'innodb_file_format';  
> SET GLOBAL innodb_file_format = BARRACUDA;  
  
2. `http://127.0.0.1:8080/xxl-job-admin`    admin/123456

## xxl-job-executor-sample-springboot

可能需要先启动`xxl-job-admin`
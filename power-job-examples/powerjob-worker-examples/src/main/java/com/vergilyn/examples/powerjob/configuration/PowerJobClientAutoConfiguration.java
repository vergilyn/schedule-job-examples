package com.vergilyn.examples.powerjob.configuration;

import com.google.common.base.Splitter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.powerjob.client.PowerJobClient;
import tech.powerjob.worker.autoconfigure.PowerJobProperties;

import java.util.List;

@Configuration
public class PowerJobClientAutoConfiguration {

    @Value("${powerjob.client.instance.password:123456}")
    public String password;

    // TODO 2023-04-27
    // 1. 是否线程安全，全局可以共用1个实例？
    // 2. password 如何配置？
    @Bean
    public PowerJobClient powerJobClient(PowerJobProperties properties){
        PowerJobProperties.Worker worker = properties.getWorker();
        List<String> serverAddress = Splitter.on(",").trimResults().splitToList(worker.getServerAddress());
        return new PowerJobClient(serverAddress, worker.getAppName(), password);
    }
}

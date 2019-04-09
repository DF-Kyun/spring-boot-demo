package com.dfkyun.kettleserver.runner;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.repository.filerep.KettleFileRepositoryMeta;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Order(value=2)
public class KettleServerStartupRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        try {
            System.out.println("kettle初始化...");
            // 日志缓冲不超过5000行，缓冲时间不超过720秒
            KettleLogStore.init(5000, 720);
            KettleEnvironment.init();
            File path = new File("samples/repository");
            KettleFileRepositoryMeta meta = new KettleFileRepositoryMeta();
            meta.setBaseDirectory(path.getAbsolutePath());
            meta.setDescription("default");
            meta.setName("default");
            meta.setReadOnly(false);
            meta.setHidingHiddenFiles(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

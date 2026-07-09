package com.xx.platform.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * 数据库初始化器
 * 应用启动时自动执行schema.sql创建表和初始数据
 */
@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        ClassPathResource resource = new ClassPathResource("schema.sql");
        String sql = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining("\n"));

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            // SQLite支持分批执行，按分号拆分
            String[] statements = sql.split(";");
            for (String s : statements) {
                String trimmed = s.trim();
                if (!trimmed.isEmpty()) {
                    stmt.execute(trimmed);
                }
            }
        }
        System.out.println("数据库初始化完成");
    }
}

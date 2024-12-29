package com.Project.Dompiler.demo.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class ConnectionPoolService implements ConnectionPool {
    private final static String compilerServiceUrl = "http://localhost:8081/compiler-service";
    private List<WebClient> usedConnections = new ArrayList<>();
    private static List<WebClient> pool = new ArrayList<>();

    private static int INITIAL_POOL_SIZE = 10;

    @Bean
    public static ConnectionPoolService create() {

        pool = new ArrayList<>(INITIAL_POOL_SIZE);
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            pool.add(createConnection());
        }
        return new ConnectionPoolService();
    }
    @Override
    public WebClient getConnection() {
        WebClient connection = pool
                .remove(pool.size() - 1);
        usedConnections.add(connection);
        return connection;
    }

    @Override
    public boolean releaseConnection(WebClient connection) {
        pool.add(connection);
        return usedConnections.remove(connection);
    }

    @Override
    public String getUrl() {
        return compilerServiceUrl;
    }

    @Override
    public String getUser() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    private static WebClient createConnection() {
        return WebClient.builder().baseUrl(compilerServiceUrl).build();
    }
}

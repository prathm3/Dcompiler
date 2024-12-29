package com.Project.Dompiler.demo.utils;

import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Connection;

public interface ConnectionPool {
    WebClient getConnection();
    boolean releaseConnection(WebClient connection);
    String getUrl();
    String getUser();
    String getPassword();
}

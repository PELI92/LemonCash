package com.example.demo.lemoncash.database;

@FunctionalInterface
public interface ThrowingRunnable {
    void run() throws Exception;
}
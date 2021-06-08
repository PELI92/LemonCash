package com.example.demo.lemoncash.database;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.sql.SQLException;

@Service
@AllArgsConstructor
public class TransactionService {
    @Resource
    private final PlatformTransactionManager transactionManager;

    public void executeTransaction(ThrowingRunnable runnable) throws SQLException {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            runnable.run();
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new SQLException(e);
        }
    }
}
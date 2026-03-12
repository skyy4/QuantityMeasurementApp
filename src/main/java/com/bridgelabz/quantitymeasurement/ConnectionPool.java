package com.bridgelabz.quantitymeasurement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Very small, purpose-built connection pool for UC16.
 * <p>
 * This is <b>not</b> a full-featured pooling implementation, but it is
 * sufficient to demonstrate connection reuse and basic statistics for this
 * educational use case.
 */
public class ConnectionPool {

    private static final ConnectionPool INSTANCE = new ConnectionPool();

    public static ConnectionPool getInstance() {
        return INSTANCE;
    }

    private final ApplicationConfig config = ApplicationConfig.getInstance();
    private final Deque<Connection> available = new ArrayDeque<>();
    private int inUse = 0;

    private ConnectionPool() {
        try {
            Class.forName(config.getDbDriverClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load DB driver: " + config.getDbDriverClassName(), e);
        }
    }

    public synchronized Connection acquire() throws SQLException {
        if (!available.isEmpty()) {
            Connection conn = available.pop();
            if (conn.isClosed()) {
                return acquire();
            }
            inUse++;
            return conn;
        }
        if (totalConnections() < config.getDbPoolMaxSize()) {
            Connection conn = DriverManager.getConnection(
                    config.getDbUrl(),
                    config.getDbUsername(),
                    config.getDbPassword());
            inUse++;
            return conn;
        }
        // Very simple strategy: no waiting, just throw for exhausted pool.
        throw new SQLException("Connection pool exhausted");
    }

    public synchronized void release(Connection connection) {
        if (connection == null) {
            return;
        }
        inUse--;
        if (available.size() < config.getDbPoolMaxIdle()) {
            available.push(connection);
        } else {
            try {
                connection.close();
            } catch (SQLException ignored) {
            }
        }
    }

    public synchronized String getStatistics() {
        return "ConnectionPool[available=" + available.size() + ", inUse=" + inUse +
                ", maxSize=" + config.getDbPoolMaxSize() + "]";
    }

    public synchronized void shutdown() {
        while (!available.isEmpty()) {
            try {
                available.pop().close();
            } catch (SQLException ignored) {
            }
        }
    }

    private int totalConnections() {
        return available.size() + inUse;
    }
}


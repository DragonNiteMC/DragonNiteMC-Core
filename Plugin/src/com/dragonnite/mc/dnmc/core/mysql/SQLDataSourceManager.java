package com.dragonnite.mc.dnmc.core.mysql;

import com.google.inject.Inject;
import com.dragonnite.mc.dnmc.core.config.implement.DNMCoreConfig;
import com.dragonnite.mc.dnmc.core.config.implement.yaml.DatabaseConfig;
import com.dragonnite.mc.dnmc.core.managers.CoreConfig;
import com.dragonnite.mc.dnmc.core.managers.SQLDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SQLDataSourceManager implements SQLDataSource {
    private static DataSource source;

    @Inject
    private SQLDataSourceManager(CoreConfig coreConfig) {
        //Load the fucking yml
        DatabaseConfig db = ((DNMCoreConfig) coreConfig).getDatabase();

        //Create dat fucking config
        HikariConfig config = new HikariConfig();
        String host = db.host;
        String port = db.port + "";
        String database = db.database;
        String username = db.username;
        String password = db.password;
        String poolname = db.pool.name;
        int minsize = db.pool.minSize;
        int maxsize = db.pool.maxSize;
        boolean SSL = db.useSSL;
        long connectionTimeout = db.pool.connectionTimeout;
        long idleTimeout = db.pool.idleTimeout;
        long maxLifeTime = db.pool.maxLifeTime;
        String jdbc = "jdbc:mysql://" + host + ":" + port + "/" + database + "?" + "useSSL=" + SSL;
        config.setJdbcUrl(jdbc);
        config.setPoolName(poolname);
        config.setMaximumPoolSize(maxsize);
        config.setMinimumIdle(minsize);
        config.setUsername(username);
        config.setPassword(password);
        config.setConnectionTimeout(connectionTimeout);
        config.setIdleTimeout(idleTimeout);
        config.setMaxLifetime(maxLifeTime);
        config.addDataSourceProperty("cachePrepStmts", true);
        config.addDataSourceProperty("useServerPrepStmts", true);
        config.addDataSourceProperty("prepStmtCacheSize", 250);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        //config.addDataSourceProperty("useUnicode",true);
        config.addDataSourceProperty("characterEncoding", "utf8");

        //Create the fucking datasource
        source = new HikariDataSource(config);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return source.getConnection();
    }

    @Override
    public DataSource getDataSource() {
        return source;
    }

}

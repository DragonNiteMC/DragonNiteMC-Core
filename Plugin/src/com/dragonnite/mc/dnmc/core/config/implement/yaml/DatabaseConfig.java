package com.dragonnite.mc.dnmc.core.config.implement.yaml;

import com.dragonnite.mc.dnmc.core.config.yaml.Configuration;
import com.dragonnite.mc.dnmc.core.config.yaml.Resource;


@Resource(locate = "Database.yml")
public class DatabaseConfig extends Configuration {

    public String host;
    public int port;
    public String database;
    public String username;
    public String password;
    public boolean useSSL;
    public DatabasePool pool;
    public RedisInfo redis;
    public String chatFormatPlaceholder;

    public static class DatabasePool {
        public String name;
        public int minSize;
        public int maxSize;
        public long connectionTimeout;
        public long idleTimeout;
        public long maxLifeTime;
    }

    public static class RedisInfo {
        public boolean enabled;
        public String ip;
        public int port;
        public int timeout;
        public boolean usePassword;
        public String password;
    }
}

package com.dragonnite.mc.dnmc.core.config.implement.yaml;

import com.dragonnite.mc.dnmc.core.config.yaml.Configuration;
import com.dragonnite.mc.dnmc.core.config.yaml.Resource;

import java.util.List;
import java.util.Map;

@Resource(locate = "CancelEvent.yml")
public class CancelEventConfig extends Configuration {
    public boolean cancelEventsEnabled;
    public List<String> eventPackageScans;
    public Map<String, Canceller> cancelEvents;

    public static class Canceller {
        public boolean useAsWhitelist;
        public List<String> worlds;
        public Wrapper blocks;
        public Wrapper entities;
        public Wrapper items;
    }

    public static class Wrapper {
        public boolean whitelist;
        public List<String> list;
    }
}

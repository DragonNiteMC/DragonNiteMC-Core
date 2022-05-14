package com.dragonite.mc.dnmc.core.config.implement.yaml;

import com.dragonite.mc.dnmc.core.config.yaml.Configuration;
import com.dragonite.mc.dnmc.core.config.yaml.Resource;


@Resource(locate = "Config.yml")
public class MainConfig extends Configuration {

    public boolean useOwnScoreboard;

    public int fallBackDelay;

    public boolean interactEventDefaultCancelled;

    public boolean clickEventDefaultCancelled;

    public boolean autoLoadExtraWorlds;

}

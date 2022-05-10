package com.dragonite.mc.dnmc.core.config.implement.yaml;

import com.dragonite.mc.dnmc.core.config.yaml.MessageConfiguration;
import com.dragonite.mc.dnmc.core.config.yaml.Prefix;
import com.dragonite.mc.dnmc.core.config.yaml.Resource;


@Resource(locate = "Messages.yml")
@Prefix(path = "Prefix")
public class MessageConfig extends MessageConfiguration {
}

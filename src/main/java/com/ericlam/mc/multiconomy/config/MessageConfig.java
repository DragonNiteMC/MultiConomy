package com.ericlam.mc.multiconomy.config;

import com.dragonnite.mc.dnmc.core.config.yaml.MessageConfiguration;
import com.dragonnite.mc.dnmc.core.config.yaml.Prefix;
import com.dragonnite.mc.dnmc.core.config.yaml.Resource;

@Prefix(path = "prefix")
@Resource(locate = "message.yml")
public class MessageConfig extends MessageConfiguration {
}

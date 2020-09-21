package com.ericlam.mc.multiconomy.config;

import com.hypernite.mc.hnmc.core.config.yaml.MessageConfiguration;
import com.hypernite.mc.hnmc.core.config.yaml.Prefix;
import com.hypernite.mc.hnmc.core.config.yaml.Resource;

@Prefix(path = "prefix")
@Resource(locate = "message.yml")
public class MessageConfig extends MessageConfiguration {
}

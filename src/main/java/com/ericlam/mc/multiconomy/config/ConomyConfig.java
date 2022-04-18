package com.ericlam.mc.multiconomy.config;

import com.dragonnite.mc.dnmc.core.config.yaml.Configuration;
import com.dragonnite.mc.dnmc.core.config.yaml.Resource;

import java.util.List;
import java.util.Map;

@Resource(locate = "config.yml")
public class ConomyConfig extends Configuration {

    public long updateTimeout;

    public String tablePrefix;

    public String vaultCurrency;

    public Map<String, Currency> currencies;

    public static class Currency {
        public int defaultBalance;
        public List<String> alias;
    }


}

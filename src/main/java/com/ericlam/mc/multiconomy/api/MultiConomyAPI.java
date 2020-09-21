package com.ericlam.mc.multiconomy.api;

import com.ericlam.mc.multiconomy.config.ConomyConfig;

public interface MultiConomyAPI {

    CurrencyController getCurrency(String currency);

    CurrencyController getVaultCurrency();

    void registerCurrency(String currency, ConomyConfig.Currency setting);

}

import com.ericlam.mc.multiconomy.MultiConomy;
import com.ericlam.mc.multiconomy.api.CurrencyController;
import com.ericlam.mc.multiconomy.config.ConomyConfig;

import java.util.List;

public class HowToUseAPI {

    public static void main(String[] args) {
        CurrencyController crystalsController = MultiConomy.getAPI().getCurrency("crystals"); //get custom currency controller
        CurrencyController vaultController = MultiConomy.getAPI().getVaultCurrency(); //get vault controller
        ConomyConfig.Currency currency = new ConomyConfig.Currency();
        currency.alias = List.of("stardust");
        currency.defaultBalance = 500;
        MultiConomy.getAPI().registerCurrency("dust", currency); // register new currency
    }
}

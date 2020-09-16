package com.caxerx.mc.interconomy.runnable;

import com.caxerx.mc.interconomy.InterConomy;
import com.caxerx.mc.interconomy.UpdateResult;
import com.caxerx.mc.interconomy.cache.TransitionAction;
import com.caxerx.mc.interconomy.cache.TransitionManager;
import com.caxerx.mc.interconomy.cache.TransitionalType;
import com.caxerx.mc.interconomy.sql.MYSQLController;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Queue;

/**
 * Created by caxerx on 2016/6/28.
 */
public class TransitionRunnable extends BukkitRunnable {

    private final Queue<TransitionAction> trans;

    public TransitionRunnable(Queue<TransitionAction> trans) {
        this.trans = trans;
    }

    @Override
    public void run() {
        TransitionAction action = trans.poll();
        if (action != null) {
            double value = action.getValue();
            TransitionalType type = action.getType();
            OfflinePlayer player = action.getPlayer();
            if (type == TransitionalType.WITHDRAW) {
                value = -value;
            }
            var result = MYSQLController.getInstance().updatePlayer(player, value, type == TransitionalType.SET);
            if (player.isOnline() && result == UpdateResult.SUCCESS) {
                new CacheUpdateRunnable(player).runTaskAsynchronously(InterConomy.getInstance());
            }
            //sqlController.logTransition(player.getUniqueId().toString(), operator, "WITHDRAW", value, System.currentTimeMillis(), connection, true);
        }
    }
}

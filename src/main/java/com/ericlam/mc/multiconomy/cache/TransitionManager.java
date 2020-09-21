package com.ericlam.mc.multiconomy.cache;

import com.ericlam.mc.multiconomy.MultiConomy;
import com.ericlam.mc.multiconomy.runnable.TransitionRunnable;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by caxerx on 2016/8/13.
 */
public class TransitionManager {
    private final ConcurrentLinkedQueue<TransitionAction> transitional;
    private final MultiConomy plugin;

    public TransitionManager(MultiConomy plugin) {
        this.plugin = plugin;
        transitional = new ConcurrentLinkedQueue<>();
    }

    public void offer(CacheManager cacheManager, TransitionAction action) {
        transitional.offer(action);
        TransitionRunnable runnable = new TransitionRunnable(cacheManager, transitional);
        runnable.runTaskAsynchronously(plugin);
    }
}

package com.dragonnite.mc.dnmc.core.bungeecord;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;

public class SlowIteratorRunnable<T> extends BukkitRunnable {
    private final Iterator<T> iterator;
    private final Consumer<T> func;

    public SlowIteratorRunnable(Collection<T> collection, Consumer<T> func) {
        this.iterator = collection.iterator();
        this.func = func;
    }

    @Override
    public void run() {
        if (iterator.hasNext()) func.accept(iterator.next());
        else cancel();
    }
}

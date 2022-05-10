package com.dragonite.mc.dnmc.core.listener.cancelevent;

import com.dragonite.mc.dnmc.core.GetterFunction;
import com.dragonite.mc.dnmc.core.managers.EventCancelManager;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public final class CancelEventManager implements EventCancelManager {

    private final Map<Class<? extends Event>, Map<Class<?>, GetterFunction>> getterMap = new LinkedHashMap<>();


    @Override
    public <T extends Event, R> void register(Class<T> event, Class<R> type, GetterFunction<T, R> getter) {
        this.getterMap.putIfAbsent(event, new ConcurrentHashMap<>());
        this.getterMap.get(event).put(type, getter);
    }

    public boolean canGetWith(Event e, Class<?> type) {
        return this.getterMap.keySet().stream().filter(cls -> cls.isInstance(e)).anyMatch(key -> Optional.ofNullable(this.getterMap.get(key)).map(m -> m.containsKey(type)).orElse(false));
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public <T, E extends Event> T getEventWith(E event, Class<T> type) {
        for (Class<? extends Event> key : this.getterMap.keySet().stream().filter(cls -> cls.isInstance(event)).collect(Collectors.toSet())) {
            T result = (T) Optional.ofNullable(this.getterMap.get(key)).map(map -> map.get(type)).map(func -> func.apply(event)).orElse(null);
            if (result == null) continue;
            return result;
        }
        return null;
    }

}

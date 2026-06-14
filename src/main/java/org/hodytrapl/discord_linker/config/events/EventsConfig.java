package org.hodytrapl.discord_linker.config.events;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.List;

public class EventsConfig {
    public static final EventsConfig INSTANCE;
    public static final ModConfigSpec SPEC;

    static {
        Pair<EventsConfig, ModConfigSpec> pair = new ModConfigSpec.Builder()
                .configure(EventsConfig::new);
        INSTANCE = pair.getLeft();
        SPEC = pair.getRight();
    }

    public final EventEntryConfig playerJoin;
    public final EventEntryConfig playerLeave;

    public EventsConfig(ModConfigSpec.Builder builder) {
        builder.comment("All event-related settings").push("events");
        playerJoin = new EventEntryConfig(builder, "player_join","%username% joined in game!","");
        playerLeave = new EventEntryConfig(builder, "player_leave","%username% leave in game!","");
        builder.pop(); // events
    }
}
package se.mickelus.tetracelium.compat.soul_fire_d;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;

public class SoulFiredCompat {
    public static final String modId = "soul_fire_d";
    public static final Boolean isLoaded = ModList.get().isLoaded(modId);

    public static void init() {
        MinecraftForge.EVENT_BUS.register(SoulFiredEffect.class);
    }
}

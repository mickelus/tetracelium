package se.mickelus.tetracelium.compat.farmersdelight;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;

public class FarmersDelightCompat {
    public static final String modId = "farmersdelight";
    public static final Boolean isLoaded = ModList.get().isLoaded(modId);

    public static void init() {
        MinecraftForge.EVENT_BUS.register(FarmersDelightEvents.class);
    }
}

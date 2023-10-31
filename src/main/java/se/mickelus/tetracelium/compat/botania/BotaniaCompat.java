package se.mickelus.tetracelium.compat.botania;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BotaniaCompat {
    public static final String modId = "botania";
    public static final Boolean isLoaded = ModList.get().isLoaded(modId);

    public static void init() {
        MinecraftForge.EVENT_BUS.register(ManaRepair.class);
    }

    public static void clientSetup() {
        ManaRepair.clientInit();
    }
}

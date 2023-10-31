package se.mickelus.tetracelium.compat.apotheosis;

import net.minecraftforge.fml.ModList;
import se.mickelus.tetra.module.ItemUpgradeRegistry;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ApotheosisCompat {
    public static final String modId = "apotheosis";
    public static final Boolean isLoaded = ModList.get().isLoaded(modId);

    public static void setup() {
        ItemUpgradeRegistry.instance.registerReplacementHook(new AffixReplacementHook());
    }
}

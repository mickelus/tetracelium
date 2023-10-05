package se.mickelus.tetracelium;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import se.mickelus.tetra.module.ItemUpgradeRegistry;
import se.mickelus.tetracelium.compat.apotheosis.AffixReplacementHook;
import se.mickelus.tetracelium.compat.apotheosis.ApotheosisCompat;
import se.mickelus.tetracelium.compat.botania.BotaniaCompat;
import se.mickelus.tetracelium.compat.botania.ManaRepair;
import se.mickelus.tetracelium.compat.farmersdelight.provider.FarmersDelightCuttingRecipeProvider;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod(TetraceliumMod.MOD_ID)
@ParametersAreNonnullByDefault
public class TetraceliumMod {
    public static final String MOD_ID = "data/tetracelium";

    public TetraceliumMod() {
        TetraceliumRegistries.init(FMLJavaModLoadingContext.get().getModEventBus());
        if (ApotheosisCompat.isLoaded) {
            ItemUpgradeRegistry.instance.registerReplacementHook(new AffixReplacementHook());
        }

        if (BotaniaCompat.isLoaded) {
            MinecraftForge.EVENT_BUS.register(ManaRepair.class);
        }
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();
        if (event.includeServer()) {
            DataGenerator gen = event.getGenerator();
            PackOutput packOutput = gen.getPackOutput();
            CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

            dataGenerator.addProvider(true, new FarmersDelightCuttingRecipeProvider(packOutput));
        }
    }
}

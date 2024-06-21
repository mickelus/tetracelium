package se.mickelus.tetracelium;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import se.mickelus.mutil.network.PacketHandler;
import se.mickelus.mutil.scheduling.AbstractScheduler;
import se.mickelus.mutil.scheduling.ServerScheduler;
import se.mickelus.tetracelium.compat.apotheosis.ApotheosisCompat;
import se.mickelus.tetracelium.compat.botania.BotaniaCompat;
import se.mickelus.tetracelium.compat.farmersdelight.FarmersDelightCompat;
import se.mickelus.tetracelium.compat.farmersdelight.provider.FarmersDelightCuttingRecipeProvider;
import se.mickelus.tetracelium.compat.soul_fire_d.SoulFiredCompat;
import se.mickelus.tetracelium.compat.twilightforest.TwilightForestCompat;
import se.mickelus.tetracelium.compat.twilightforest.effects.SapParticlePacket;
import se.mickelus.tetracelium.compat.twilightforest.effects.TwilightBoltPacket;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod(TetraceliumMod.MOD_ID)
@ParametersAreNonnullByDefault
public class TetraceliumMod {
    public static final String MOD_ID = "tetracelium";
    public static PacketHandler packetHandler;
    public static AbstractScheduler serverScheduler = new ServerScheduler();

    public TetraceliumMod() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(serverScheduler);

        TetraceliumRegistries.init(FMLJavaModLoadingContext.get().getModEventBus());

        if (BotaniaCompat.isLoaded) {
            BotaniaCompat.init();
        }

        if (FarmersDelightCompat.isLoaded) {
            FarmersDelightCompat.init();
        }

        if (TwilightForestCompat.isLoaded) {
            TwilightForestCompat.init();
        }

        if (SoulFiredCompat.isLoaded) {
            SoulFiredCompat.init();
        }

        packetHandler = new PacketHandler("tetracelium", "main", "1");
    }

    @SubscribeEvent
    public void setup(FMLCommonSetupEvent event) {
        if (ApotheosisCompat.isLoaded) {
            ApotheosisCompat.setup();
        }

        packetHandler.registerPacket(TwilightBoltPacket.class, TwilightBoltPacket::new);
        packetHandler.registerPacket(SapParticlePacket.class, SapParticlePacket::new);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void clientSetup(FMLClientSetupEvent event) {
        if (BotaniaCompat.isLoaded) {
            BotaniaCompat.clientSetup();
        }

        if (TwilightForestCompat.isLoaded) {
            TwilightForestCompat.clientInit();
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

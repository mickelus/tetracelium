package se.mickelus.tetracompat;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.mickelus.mutil.network.PacketHandler;
import se.mickelus.tetracompat.aspect.TetraEnchantmentHelper;
import se.mickelus.tetracompat.blocks.multischematic.MultiblockSchematicScrollPacket;
import se.mickelus.tetracompat.blocks.workbench.WorkbenchTile;
import se.mickelus.tetracompat.compat.apotheosis.AffixReplacementHook;
import se.mickelus.tetracompat.compat.curios.CuriosCompat;
import se.mickelus.tetracompat.craftingeffect.CraftingEffectRegistry;
import se.mickelus.tetracompat.craftingeffect.condition.*;
import se.mickelus.tetracompat.craftingeffect.outcome.ApplyEnchantmentOutcome;
import se.mickelus.tetracompat.craftingeffect.outcome.ApplyImprovementOutcome;
import se.mickelus.tetracompat.craftingeffect.outcome.MaterialReductionOutcome;
import se.mickelus.tetracompat.craftingeffect.outcome.RemoveImprovementOutcome;
import se.mickelus.tetracompat.data.DataManager;
import se.mickelus.tetracompat.data.UpdateDataPacket;
import se.mickelus.tetracompat.data.provider.TetraBlockStateProvider;
import se.mickelus.tetracompat.data.provider.TetraLootTableProvider;
import se.mickelus.tetracompat.data.provider.TetraTagsProvider;
import se.mickelus.tetracompat.effect.ItemEffectHandler;
import se.mickelus.tetracompat.effect.LungeEchoPacket;
import se.mickelus.tetracompat.effect.TruesweepPacket;
import se.mickelus.tetracompat.effect.howling.HowlingPacket;
import se.mickelus.tetracompat.effect.revenge.AddRevengePacket;
import se.mickelus.tetracompat.effect.revenge.RemoveRevengePacket;
import se.mickelus.tetracompat.interactions.SecondaryInteractionPacket;
import se.mickelus.tetracompat.items.forged.VibrationDebuffer;
import se.mickelus.tetracompat.items.modular.ChargedAbilityPacket;
import se.mickelus.tetracompat.items.modular.SecondaryAbilityPacket;
import se.mickelus.tetracompat.items.modular.impl.bow.ProjectileMotionPacket;
import se.mickelus.tetracompat.items.modular.impl.toolbelt.ToolbeltModule;
import se.mickelus.tetracompat.module.*;
import se.mickelus.tetracompat.module.improvement.DestabilizationEffect;
import se.mickelus.tetracompat.module.improvement.HonePacket;
import se.mickelus.tetracompat.module.improvement.SettlePacket;
import se.mickelus.tetracompat.module.schematic.BookEnchantSchematic;
import se.mickelus.tetracompat.module.schematic.CleanseSchematic;
import se.mickelus.tetracompat.module.schematic.requirement.*;
import se.mickelus.tetracompat.properties.TetraAttributes;
import se.mickelus.tetracompat.trades.TradeHandler;
import se.mickelus.tetracompat.util.TierHelper;
import se.mickelus.tetracompat.util.ToolActionHelper;

import javax.annotation.ParametersAreNonnullByDefault;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod(TetraCompatMod.MOD_ID)

@ParametersAreNonnullByDefault
public class TetraCompatMod {
    public static final String MOD_ID = "tetracompat";

    public TetraCompatMod() {
        ItemUpgradeRegistry.instance.registerReplacementHook(new AffixReplacementHook());
    }
}

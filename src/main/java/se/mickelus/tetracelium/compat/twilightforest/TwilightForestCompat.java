package se.mickelus.tetracelium.compat.twilightforest;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.StatsHelper;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;
import se.mickelus.tetra.gui.stats.getter.LabelGetterBasic;
import se.mickelus.tetra.gui.stats.getter.StatGetterEffectLevel;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterInteger;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;
import se.mickelus.tetracelium.compat.twilightforest.effects.*;

public class TwilightForestCompat {
    public static final String modId = "twilightforest";
    public static final Boolean isLoaded = ModList.get().isLoaded(modId);

    public static void init() {
        MinecraftForge.EVENT_BUS.register(IgnitingEffect.class);
        MinecraftForge.EVENT_BUS.register(HydralBondEffect.class);
        MinecraftForge.EVENT_BUS.register(PunishingEffect.class);
        MinecraftForge.EVENT_BUS.register(ChargingEffect.class);
        MinecraftForge.EVENT_BUS.register(ReanimatingEffect.class);
        MinecraftForge.EVENT_BUS.register(SappingEffect.class);
        MinecraftForge.EVENT_BUS.register(VerticalMultishotEffect.class);
        MinecraftForge.EVENT_BUS.register(SeekerProjectilesEffect.class);
        MinecraftForge.EVENT_BUS.register(IceProjectilesEffect.class);
        MinecraftForge.EVENT_BUS.register(EnderProjectilesEffect.class);
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientInit() {
        MinecraftForge.EVENT_BUS.register(TwilightBoltEffect.class);

        IgnitingEffect.clientInit();
        HydralBondEffect.clientInit();
        PunishingEffect.clientInit();
        ChargingEffect.clientInit();
        FortifyingEffect.clientInit();
        ReanimatingEffect.clientInit();
        SappingEffect.clientInit();
        TwilightBoltEffect.clientInit();
        VerticalMultishotEffect.clientInit();
        SeekerProjectilesEffect.clientInit();
        IceProjectilesEffect.clientInit();
        EnderProjectilesEffect.clientInit();

        IStatGetter statGetter = new StatGetterEffectLevel(ItemEffect.get("autoSmelt"), 1);
        GuiStatBar statBar = new GuiStatBar(0, 0, StatsHelper.barLength, "tetra.stats.autoSmelt",
                0, 1, false, false, false, statGetter, LabelGetterBasic.noLabel,
                new TooltipGetterInteger("tetra.stats.autoSmelt.tooltip", statGetter));

        WorkbenchStatsGui.addBar(statBar);
        HoloStatsGui.addBar(statBar);
    }
}

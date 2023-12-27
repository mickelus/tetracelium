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
import se.mickelus.tetracelium.compat.twilightforest.effects.IgnitingEffect;

public class TwilightForestCompat {
    public static final String modId = "twilightforest";
    public static final Boolean isLoaded = ModList.get().isLoaded(modId);

    public static void init() {
        MinecraftForge.EVENT_BUS.register(IgnitingEffect.class);
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientInit() {
        IgnitingEffect.clientInit();

        IStatGetter statGetter = new StatGetterEffectLevel(ItemEffect.get("autoSmelt"), 1);
        GuiStatBar statBar = new GuiStatBar(0, 0, StatsHelper.barLength, "tetra.stats.autoSmelt",
                0, 1, false, false, false, statGetter, LabelGetterBasic.noLabel,
                new TooltipGetterInteger("tetra.stats.autoSmelt.tooltip", statGetter));

        WorkbenchStatsGui.addBar(statBar);
        HoloStatsGui.addBar(statBar);
    }
}

package se.mickelus.tetracelium.compat.twilightforest.effects;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.effect.EffectHelper;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.event.ModularLooseProjectilesEvent;
import se.mickelus.tetra.gui.stats.StatsHelper;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;
import se.mickelus.tetra.gui.stats.getter.LabelGetterBasic;
import se.mickelus.tetra.gui.stats.getter.StatGetterEffectLevel;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterNone;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;
import twilightforest.init.TFItems;
import twilightforest.item.IceBowItem;

public class IceProjectilesEffect {
    public static final ItemEffect iceProjectilesEffect = ItemEffect.get("iceProjectiles");

    @SubscribeEvent
    public static void onModularLooseProjectiles(ModularLooseProjectilesEvent event) {
        if (EffectHelper.getEffectLevel(event.getFiringStack(), iceProjectilesEffect) > 0) {
            IceBowItem bow = (IceBowItem) TFItems.ICE_BOW.get();
            event.addProjectileRemapper(bow::customArrow);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientInit() {
        IStatGetter countGetter = new StatGetterEffectLevel(iceProjectilesEffect, 1);
        GuiStatBar statBar = new GuiStatBar(0, 0, StatsHelper.barLength, "tetra.stats.iceProjectiles",
                0, 1, false, false, false, countGetter, LabelGetterBasic.noLabel,
                new TooltipGetterNone("tetra.stats.iceProjectiles.tooltip"));

        WorkbenchStatsGui.addBar(statBar);
        HoloStatsGui.addBar(statBar);
    }
}

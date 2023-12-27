package se.mickelus.tetracelium.compat.twilightforest.effects;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.effect.ApplyHitTargetEffectsEvent;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.StatsHelper;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;
import se.mickelus.tetra.gui.stats.getter.LabelGetterBasic;
import se.mickelus.tetra.gui.stats.getter.StatGetterEffectLevel;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterInteger;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;

public class IgnitingEffect {
    public static final ItemEffect ignitingEffect = ItemEffect.get("igniting");

    @SubscribeEvent
    public static void onApplyHitTargetEffects(ApplyHitTargetEffectsEvent event) {
        if (!event.getTarget().fireImmune()) {
            ItemStack weaponStack = event.getUsedItemStack();
            if (!weaponStack.isEmpty() && weaponStack.getItem() instanceof IModularItem item) {
                int ignitingLevel = item.getEffectLevel(weaponStack, ignitingEffect);
                event.getAttacker().setSecondsOnFire(ignitingLevel);
            }
        }
    }


    @OnlyIn(Dist.CLIENT)
    public static void clientInit() {
        IStatGetter statGetter = new StatGetterEffectLevel(ignitingEffect, 1);
        GuiStatBar statBar = new GuiStatBar(0, 0, StatsHelper.barLength, "tetra.stats.igniting",
                0, 10, false, false, false, statGetter, LabelGetterBasic.integerLabel,
                new TooltipGetterInteger("tetra.stats.igniting.tooltip", statGetter));

        WorkbenchStatsGui.addBar(statBar);
        HoloStatsGui.addBar(statBar);
    }
}

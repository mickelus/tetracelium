package se.mickelus.tetracelium.compat.twilightforest.effects;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.effect.EffectHelper;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.StatsHelper;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.*;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;

import java.util.stream.Stream;

public class HydralBondEffect {
    public static final ItemEffect hydralBondEffect = ItemEffect.get("hydralBond");

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.getSource().is(DamageTypeTags.IS_FIRE)) {
            LivingEntity entity = event.getEntity();
            Stream.of(entity.getMainHandItem(), entity.getOffhandItem())
                    .filter(itemStack -> itemStack.getItem() instanceof IModularItem)
                    .filter(itemStack -> EffectHelper.getEffectLevel(itemStack, hydralBondEffect) > 0)
                    .forEach(itemStack -> {
                        int amount = EffectHelper.getEffectLevel(itemStack, hydralBondEffect);
                        float probability = EffectHelper.getEffectEfficiency(itemStack, hydralBondEffect);
                        if (probability >= 1 || entity.getRandom().nextFloat() < probability) {
                            itemStack.setDamageValue(itemStack.getDamageValue() - amount);
                        }
                    });
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientInit() {
        IStatGetter amount = new StatGetterEffectLevel(hydralBondEffect, 1);
        IStatGetter chance = new StatGetterEffectEfficiency(hydralBondEffect, 1);
        IStatGetter barValue = new StatGetterMultiply(amount, chance);
        GuiStatBar statBar = new GuiStatBar(0, 0, StatsHelper.barLength, "tetra.stats.hydralBond",
                0, 5, false, false, false, barValue, LabelGetterBasic.decimalLabel,
                new TooltipGetterMultiValue("tetra.stats.hydralBond.tooltip",
                        new IStatGetter[] { chance, amount },
                        new StatFormat[] { StatFormat.twoDecimal, StatFormat.noDecimal }));

        WorkbenchStatsGui.addBar(statBar);
        HoloStatsGui.addBar(statBar);
    }
}

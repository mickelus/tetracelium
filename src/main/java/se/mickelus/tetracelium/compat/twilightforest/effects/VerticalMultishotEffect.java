package se.mickelus.tetracelium.compat.twilightforest.effects;

import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.effect.EffectHelper;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.event.ModularLooseProjectilesEvent;
import se.mickelus.tetra.gui.stats.StatsHelper;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.*;
import se.mickelus.tetra.items.modular.impl.bow.ModularBowItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;

import static se.mickelus.tetra.gui.stats.StatsHelper.withFormat;
import static se.mickelus.tetra.gui.stats.StatsHelper.withStats;

public class VerticalMultishotEffect {
    public static final ItemEffect verticalMultishotEffect = ItemEffect.get("verticalMultishot");


    @SubscribeEvent
    public static void onModularLooseProjectiles(ModularLooseProjectilesEvent event) {
        ItemStack itemStack = event.getFiringStack();
        int verticalCount = EffectHelper.getEffectLevel(event.getFiringStack(), verticalMultishotEffect);
        if (verticalCount > 0 && event.getProjectileVelocity() > 0.1f && !event.getLevel().isClientSide) {
            int powerLevel = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.POWER_ARROWS, itemStack);
            int punchLevel = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.PUNCH_ARROWS, itemStack);
            int flameLevel = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.FLAMING_ARROWS, itemStack);
            int piercingLevel = EffectHelper.getEffectLevel(itemStack, ItemEffect.piercing) + EnchantmentHelper.getTagEnchantmentLevel(Enchantments.PIERCING, itemStack);


            float spread = EffectHelper.getEffectEfficiency(event.getFiringStack(), verticalMultishotEffect);

            for (int i = 0; i < verticalCount; i++) {
                int offset = (i / 2 + 1) * (i % 2 == 0 ? 1 : -1);
                double pitch = event.getBasePitch() + spread * offset;
                ModularBowItem.fireProjectile(itemStack, event.getLevel(), (ArrowItem) event.getAmmoStack().getItem(), event.getAmmoStack(),
                        event.getProjectileRemappers(), event.getShooter(), (float) pitch, (float) event.getBaseYaw(),
                        event.getProjectileVelocity(), event.getAccuracy(), event.getDrawProgress(), event.getStrength(), powerLevel,
                        punchLevel, flameLevel, piercingLevel, event.isHasSuspend(), true);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientInit() {
        IStatGetter countGetter = new StatGetterEffectLevel(verticalMultishotEffect, 1);
        IStatGetter spreadGetter = new StatGetterEffectEfficiency(verticalMultishotEffect, 1);
        GuiStatBar statBar = new GuiStatBar(0, 0, StatsHelper.barLength, "tetra.stats.verticalMultishot",
                0, 12, true, false, false, countGetter, LabelGetterBasic.integerLabel,
                new TooltipGetterMultiValue("tetra.stats.verticalMultishot.tooltip",
                        withStats(countGetter, spreadGetter),
                        withFormat(StatFormat.noDecimal, StatFormat.oneDecimal)));

        WorkbenchStatsGui.addBar(statBar);
        HoloStatsGui.addBar(statBar);
    }
}

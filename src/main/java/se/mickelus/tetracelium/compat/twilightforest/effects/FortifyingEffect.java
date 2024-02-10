package se.mickelus.tetracelium.compat.twilightforest.effects;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.effect.EffectHelper;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.StatsHelper;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.*;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.ThrownModularItemEntity;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;
import twilightforest.capabilities.CapabilityList;

import java.util.Optional;

public class FortifyingEffect {
    public static final ItemEffect fortifyingEffect = ItemEffect.get("fortifying");

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        ItemStack itemStack;
        Entity killer;
        if ("trident".equals(event.getSource().getMsgId())
                && event.getSource().getDirectEntity() instanceof ThrownModularItemEntity modularEntity) {
            itemStack = modularEntity.getThrownStack();
            killer = modularEntity.getOwner();
        } else {
            itemStack = Optional.ofNullable(event.getSource().getEntity())
                    .filter(entity -> entity instanceof Player)
                    .map(entity -> (LivingEntity) entity)
                    .map(LivingEntity::getMainHandItem)
                    .filter(stack -> stack.getItem() instanceof IModularItem)
                    .orElse(null);
            killer = event.getSource().getEntity();
        }

        if (itemStack != null) {
            Level level = event.getEntity().level();
            if (!level.isClientSide
                    && EffectHelper.getEffectLevel(itemStack, fortifyingEffect) > 0
                    && killer instanceof ServerPlayer player) {
                player.getCapability(CapabilityList.SHIELDS).ifPresent(cap -> {
                    cap.setShields(Math.min(cap.temporaryShieldsLeft() + 1, 4), true);
                    itemStack.hurt((int) Math.ceil(itemStack.getMaxDamage() * 0.01 * EffectHelper.getEffectEfficiency(itemStack, fortifyingEffect)), level.getRandom(), player);
                });
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientInit() {
        IStatGetter statGetter = new StatGetterEffectLevel(fortifyingEffect, 1);
        IStatGetter efficiencyGetter = new StatGetterEffectEfficiency(fortifyingEffect, 1);
        GuiStatBar statBar = new GuiStatBar(0, 0, StatsHelper.barLength, "tetra.stats.fortifying",
                0, 1, false, false, false, statGetter, LabelGetterBasic.noLabel,
                new TooltipGetterPercentage("tetra.stats.fortifying.tooltip", efficiencyGetter));

        WorkbenchStatsGui.addBar(statBar);
        HoloStatsGui.addBar(statBar);
    }
}

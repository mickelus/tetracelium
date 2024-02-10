package se.mickelus.tetracelium.compat.twilightforest.effects;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.effect.ApplyHitTargetEffectsEvent;
import se.mickelus.tetra.effect.ApplyUsageEffectsEvent;
import se.mickelus.tetra.effect.EffectHelper;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.StatsHelper;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.*;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.ThrownModularItemEntity;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;
import se.mickelus.tetracelium.TetraceliumMod;
import twilightforest.data.tags.EntityTagGenerator;
import twilightforest.item.LifedrainScepterItem;

import javax.annotation.Nullable;
import java.util.Optional;

import static se.mickelus.tetra.gui.stats.StatsHelper.withFormat;
import static se.mickelus.tetra.gui.stats.StatsHelper.withStats;

public class SappingEffect {
    public static final ItemEffect sappingEffect = ItemEffect.get("sapping");

    @SubscribeEvent
    public static void onApplyHitTargetEffects(ApplyHitTargetEffectsEvent event) {
        ItemStack itemStack = event.getUsedItemStack();
        int effectLevel = EffectHelper.getEffectLevel(itemStack, sappingEffect);
        LivingEntity attacker = event.getAttacker();
        if (effectLevel > 0) {
            attacker.heal(effectLevel);
            if (attacker instanceof Player player) {
                player.getFoodData().eat(effectLevel, 0.1F);
            }

            Level level = event.getTarget().level();
            InteractionHand hand = getHand(itemStack, attacker);
            Vec3 targetPosition = event.getTarget().getEyePosition();
            if (level.isClientSide) {
                makeRedMagicTrail(level, hand, attacker, targetPosition);
            } else {
                TetraceliumMod.packetHandler.sendToAllPlayersNear(new SapParticlePacket(hand, targetPosition), attacker.blockPosition(), 64, level.dimension());
            }
        }
    }


    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        ItemStack itemStack;
        if ("trident".equals(event.getSource().getMsgId())
                && event.getSource().getDirectEntity() instanceof ThrownModularItemEntity modularEntity) {
            itemStack = modularEntity.getThrownStack();
        } else {
            itemStack = Optional.ofNullable(event.getSource().getEntity())
                    .filter(entity -> entity instanceof Player)
                    .map(entity -> (LivingEntity) entity)
                    .map(LivingEntity::getMainHandItem)
                    .filter(stack -> stack.getItem() instanceof IModularItem)
                    .orElse(null);
        }

        if (itemStack != null) {
            Level level = event.getEntity().level();
            if (!level.isClientSide
                    && !event.getEntity().getType().is(EntityTagGenerator.LIFEDRAIN_DROPS_NO_FLESH)
                    && EffectHelper.getEffectLevel(itemStack, sappingEffect) > 0) {
                LifedrainScepterItem.animateTargetShatter((ServerLevel) level, event.getEntity());
            }
        }
    }

    @SubscribeEvent
    public static void onApplyUsageEffects(ApplyUsageEffectsEvent event) {
        ItemStack itemStack = event.getItemStack();
        double effectEfficiency = EffectHelper.getEffectEfficiency(itemStack, sappingEffect);
        if (effectEfficiency > 0) {
            ServerPlayer player = CastOptional.cast(event.getUsingEntity(), ServerPlayer.class).orElse(null);
            itemStack.hurt((int) Math.ceil(itemStack.getMaxDamage() * 0.01 * effectEfficiency), event.getUsingEntity().getRandom(), player);
        }
    }

    private static String getIdentifier(ItemStack itemStack) {
        return CastOptional.cast(itemStack.getItem(), IModularItem.class)
                .map(item -> item.getIdentifier(itemStack))
                .orElse(null);
    }

    @Nullable
    private static InteractionHand getHand(ItemStack itemStack, LivingEntity entity) {
        String usedIdentifier = getIdentifier(itemStack);
        if (usedIdentifier.equals(getIdentifier(entity.getItemInHand(InteractionHand.MAIN_HAND)))) {
            return InteractionHand.MAIN_HAND;
        }
        if (usedIdentifier.equals(getIdentifier(entity.getItemInHand(InteractionHand.OFF_HAND)))) {
            return InteractionHand.OFF_HAND;
        }
        return null;
    }

    // based on private method in LifedrainScepterItem
    public static void makeRedMagicTrail(Level level, InteractionHand hand, LivingEntity source, Vec3 target) {
        double handOffset = 0;
        if (hand == InteractionHand.MAIN_HAND) {
            handOffset = 0.35;
        }
        if (hand == InteractionHand.OFF_HAND) {
            handOffset = -0.35;
        }

        int particles = 32;
        for (int i = 0; i < particles; i++) {
            double trailFactor = i / (particles - 1.0D);
            float f = 1.0F;
            float f1 = 0.5F;
            float f2 = 0.5F;

            double tx = source.getX() + (target.x() - source.getX()) * trailFactor + level.getRandom().nextGaussian() * 0.005D + (handOffset * Direction.fromYRot(source.yBodyRot).get2DDataValue());
            double ty = source.getEyeY() - 0.1D + (target.y() - source.getEyeY()) * trailFactor + level.getRandom().nextGaussian() * 0.005D - 0.1D;
            double tz = source.getZ() + (target.z() - source.getZ()) * trailFactor + level.getRandom().nextGaussian() * 0.005D + (handOffset * Direction.fromYRot(source.yBodyRot).get2DDataValue());
            level.addParticle(ParticleTypes.ENTITY_EFFECT, tx, ty, tz, f, f1, f2);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientInit() {
        IStatGetter statGetter = new StatGetterEffectLevel(sappingEffect, 1);
        IStatGetter saturationGetter = new StatGetterEffectLevel(sappingEffect, 0.2f);
        IStatGetter durabilityGetter = new StatGetterEffectEfficiency(sappingEffect, 1);
        GuiStatBar statBar = new GuiStatBar(0, 0, StatsHelper.barLength, "tetra.stats.sapping",
                0, 4, false, false, false, statGetter, LabelGetterBasic.integerLabel,
                new TooltipGetterMultiValue("tetra.stats.sapping.tooltip",
                        withStats(statGetter, saturationGetter, durabilityGetter),
                        withFormat(StatFormat.noDecimal, StatFormat.oneDecimal, StatFormat.noDecimal)));

        WorkbenchStatsGui.addBar(statBar);
        HoloStatsGui.addBar(statBar);
    }
}

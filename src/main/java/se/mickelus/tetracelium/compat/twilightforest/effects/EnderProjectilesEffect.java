package se.mickelus.tetracelium.compat.twilightforest.effects;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.effect.EffectHelper;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.event.ModularLooseProjectilesEvent;
import se.mickelus.tetra.gui.stats.StatsHelper;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.bar.GuiStatIndicator;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;
import se.mickelus.tetra.gui.stats.getter.LabelGetterBasic;
import se.mickelus.tetra.gui.stats.getter.StatGetterEffectLevel;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterNone;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;
import se.mickelus.tetracelium.TetraceliumTextures;

public class EnderProjectilesEffect {
    public static final String yankKey = "tetracelium:ender_yank";
    public static final String jauntKey = "tetracelium:ender_jaunt";
    public static final ItemEffect enderProjectilesEffect = ItemEffect.get("enderProjectiles");
    public static final ItemEffect enderProjectilesYankEffect = ItemEffect.get("enderProjectilesYank");
    public static final ItemEffect enderProjectilesJauntEffect = ItemEffect.get("enderProjectilesJaunt");

    public static AbstractArrow addYankData(AbstractArrow arrow) {
        arrow.getPersistentData().putBoolean(yankKey, true);
        return arrow;
    }

    public static AbstractArrow addJauntData(AbstractArrow arrow) {
        arrow.getPersistentData().putBoolean(jauntKey, true);
        return arrow;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onModularLooseProjectiles(ModularLooseProjectilesEvent event) {
        if (EffectHelper.getEffectLevel(event.getFiringStack(), enderProjectilesEffect) > 0) {
            if (EffectHelper.getEffectLevel(event.getFiringStack(), enderProjectilesYankEffect) > 0) {
                event.addProjectileRemapper(EnderProjectilesEffect::addYankData);
            }
            if (EffectHelper.getEffectLevel(event.getFiringStack(), enderProjectilesJauntEffect) > 0) {
                event.addProjectileRemapper(EnderProjectilesEffect::addJauntData);
            }
        }
    }

    @SubscribeEvent
    public static void onEnderBowHit(ProjectileImpactEvent evt) {
        Projectile arrow = evt.getProjectile();
        if (arrow.getOwner() instanceof Player player
                && evt.getRayTraceResult() instanceof EntityHitResult result
                && result.getEntity() instanceof LivingEntity living
                && arrow.getOwner() != result.getEntity()) {

            double playerX = player.getX();
            double playerY = player.getY();
            double playerZ = player.getZ();
            float playerYaw = player.getYRot();
            @Nullable Entity playerVehicle = player.getVehicle();
            boolean hasJaunt = arrow.getPersistentData().contains(jauntKey);
            boolean hasYank = arrow.getPersistentData().contains(yankKey);

            if (hasJaunt) {
                Entity vehicle = living.isPassenger() ? living.getVehicle() : null;
                teleport(player, vehicle, living.getYRot(), living.getX(), living.getY(), living.getZ());
                if (vehicle != null) {
                    living.stopRiding();
                }
            }
            if (hasYank) {
                teleport(living, playerVehicle, playerYaw, playerX, playerY, playerZ);
                if (playerVehicle != null) {
                    player.stopRiding();
                }
            }

            if (hasYank || hasJaunt) {
                player.invulnerableTime = 40;
            }
        }
    }

    private static void teleport(LivingEntity teleportingEntity, @Nullable Entity targetVehicle, float yRot, double x, double y, double z) {
        teleportingEntity.setYRot(yRot);
        teleportingEntity.teleportTo(x, y, z);
        teleportingEntity.level().broadcastEntityEvent(teleportingEntity, (byte) 46);
        if (targetVehicle != null) {
            teleportingEntity.startRiding(targetVehicle, true);
        }
        teleportingEntity.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientInit() {
        IStatGetter statGetter = new StatGetterEffectLevel(enderProjectilesEffect, 1);
        GuiStatBar statBar = new GuiStatBar(0, 0, StatsHelper.barLength, "tetra.stats.enderProjectiles",
                0, 1, false, false, false, statGetter, LabelGetterBasic.noLabel,
                new TooltipGetterNone("tetra.stats.enderProjectiles.tooltip"))
                .setIndicators(
                        new GuiStatIndicator(0, 0, "tetra.stats.enderProjectilesYank", 0, 160, TetraceliumTextures.glyphs,
                                new StatGetterEffectLevel(enderProjectilesYankEffect, 1), new TooltipGetterNone("tetra.stats.enderProjectilesYank.tooltip")),
                        new GuiStatIndicator(0, 0, "tetra.stats.enderProjectilesJaunt", 8, 160, TetraceliumTextures.glyphs,
                                new StatGetterEffectLevel(enderProjectilesJauntEffect, 1), new TooltipGetterNone("tetra.stats.enderProjectilesJaunt.tooltip")));

        WorkbenchStatsGui.addBar(statBar);
        HoloStatsGui.addBar(statBar);
    }
}

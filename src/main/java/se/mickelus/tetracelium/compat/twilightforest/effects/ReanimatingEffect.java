package se.mickelus.tetracelium.compat.twilightforest.effects;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.effect.EffectHelper;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.StatsHelper;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.*;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.ThrownModularItemEntity;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;
import twilightforest.entity.monster.LoyalZombie;
import twilightforest.init.TFEntities;
import twilightforest.init.TFSounds;

import java.util.Optional;

import static se.mickelus.tetra.gui.stats.StatsHelper.withFormat;
import static se.mickelus.tetra.gui.stats.StatsHelper.withStats;

public class ReanimatingEffect {
    public static final ItemEffect reanimatingEffect = ItemEffect.get("reanimating");

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
            int effectLevel = EffectHelper.getEffectLevel(itemStack, reanimatingEffect);
            if (!level.isClientSide
                    && !MobType.UNDEAD.equals(event.getEntity().getMobType())
                    && effectLevel > 0) {

                ServerPlayer player = CastOptional.cast(killer, ServerPlayer.class).orElse(null);

                spawnZombie(level, killer, event.getEntity().getPosition(0), effectLevel);
                itemStack.hurt((int) Math.ceil(itemStack.getMaxDamage() * 0.01 * EffectHelper.getEffectEfficiency(itemStack, reanimatingEffect)), level.getRandom(), player);
            }
        }
    }

    static void spawnZombie(Level level, Entity owner, Vec3 spawnPosition, int duration) {
        LoyalZombie zombie = TFEntities.LOYAL_ZOMBIE.get().create(level);
        zombie.moveTo(spawnPosition);
        if (level.noCollision(zombie, zombie.getBoundingBox())) {
            zombie.spawnAnim();
            zombie.setTame(true);
            zombie.setOwnerUUID(owner.getUUID());
            zombie.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, duration * 20, 1));
            level.addFreshEntity(zombie);
            level.gameEvent(owner, GameEvent.ENTITY_PLACE, spawnPosition);
            zombie.playSound(TFSounds.LOYAL_ZOMBIE_SUMMON.get(), 1.0F, zombie.getVoicePitch());
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientInit() {
        IStatGetter statGetter = new StatGetterEffectLevel(reanimatingEffect, 1);
        IStatGetter efficiencyGetter = new StatGetterEffectEfficiency(reanimatingEffect, 1);
        GuiStatBar statBar = new GuiStatBar(0, 0, StatsHelper.barLength, "tetra.stats.reanimating",
                0, 120, false, false, false, statGetter, LabelGetterBasic.integerLabel,
                new TooltipGetterMultiValue("tetra.stats.reanimating.tooltip",
                        withStats(statGetter, efficiencyGetter),
                        withFormat(StatFormat.noDecimal, StatFormat.noDecimal)));

        WorkbenchStatsGui.addBar(statBar);
        HoloStatsGui.addBar(statBar);
    }
}

package se.mickelus.tetracelium.compat.twilightforest.effects;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.effect.EffectHelper;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.StatsHelper;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.*;
import se.mickelus.tetra.items.modular.ItemModularHandheld;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;
import se.mickelus.tetracelium.TetraceliumMod;
import twilightforest.entity.projectile.TwilightWandBolt;
import twilightforest.init.TFSounds;

import static se.mickelus.tetra.gui.stats.StatsHelper.withFormat;
import static se.mickelus.tetra.gui.stats.StatsHelper.withStats;

public class TwilightBoltEffect {
    public static final ItemEffect twilightBoltEffect = ItemEffect.get("twilightBolt");

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onClickInput(InputEvent.InteractionKeyMappingTriggered event) {
        Minecraft mc = Minecraft.getInstance();
        ItemStack itemStack = mc.player.getMainHandItem();
        if (event.isAttack()
                && !event.isCanceled()
                && itemStack.getItem() instanceof ItemModularHandheld
                && mc.hitResult != null
                && HitResult.Type.MISS.equals(mc.hitResult.getType())) {
            if (canTrigger(mc.player, itemStack)) {
                sendPacket();
            }
        }
    }

    static boolean canTrigger(Player player, ItemStack itemStack) {
        return player.getAttackStrengthScale(0.5F) > 0.9F
                && EffectHelper.getEffectLevel(itemStack, twilightBoltEffect) > 0
                && !player.getCooldowns().isOnCooldown(itemStack.getItem());
    }


    public static void sendPacket() {
        TetraceliumMod.packetHandler.sendToServer(new TwilightBoltPacket());
    }

    public static void handleServer(Player player) {
        ItemStack itemStack = player.getMainHandItem();
        if (canTrigger(player, itemStack)) {
            fireProjectile(player, player.level(), itemStack);
        }
    }

    static void fireProjectile(Player player, Level level, ItemStack itemStack) {
        player.playSound(TFSounds.SCEPTER_PEARL.get(), 1.0F, (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2F + 1.0F);
        level.addFreshEntity(new TwilightWandBolt(level, player));
        itemStack.hurt((int) Math.ceil(itemStack.getMaxDamage() * 0.01f), level.getRandom(), null);
        player.getCooldowns().addCooldown(itemStack.getItem(), (int) Math.ceil(((ItemModularHandheld) itemStack.getItem()).getCooldownBase(itemStack) * 4 * 20));
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientInit() {
        IStatGetter statGetter = new StatGetterEffectLevel(twilightBoltEffect, 1);
        IStatGetter efficiencyGetter = new StatGetterEffectEfficiency(twilightBoltEffect, 1);
        IStatGetter cooldownGetter = new StatGetterCooldown(0, 4);
        GuiStatBar statBar = new GuiStatBar(0, 0, StatsHelper.barLength, "tetra.stats.twilightBolt",
                0, 1, false, false, false, statGetter, LabelGetterBasic.noLabel,
                new TooltipGetterMultiValue("tetra.stats.twilightBolt.tooltip",
                        withStats(efficiencyGetter, cooldownGetter),
                        withFormat(StatFormat.noDecimal, StatFormat.oneDecimal)));

        WorkbenchStatsGui.addBar(statBar);
        HoloStatsGui.addBar(statBar);
    }
}

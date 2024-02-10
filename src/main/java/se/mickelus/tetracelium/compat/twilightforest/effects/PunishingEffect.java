package se.mickelus.tetracelium.compat.twilightforest.effects;

import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.StatsHelper;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;
import se.mickelus.tetra.gui.stats.getter.LabelGetterBasic;
import se.mickelus.tetra.gui.stats.getter.StatGetterEffectLevel;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterInteger;
import se.mickelus.tetra.items.modular.ItemModularHandheld;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;

public class PunishingEffect {
    public static final ItemEffect punishingEffect = ItemEffect.get("punishing");

    @SubscribeEvent
    public static void onKnightmetalToolDamage(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();

        if (!target.level().isClientSide() && event.getSource().getDirectEntity() instanceof LivingEntity living) {
            ItemStack itemStack = living.getMainHandItem();

            if (!itemStack.isEmpty() && itemStack.getItem() instanceof ItemModularHandheld item) {
                int effectLevel = item.getEffectLevel(itemStack, punishingEffect);
                if (effectLevel > 0 && target.getArmorValue() > 0) {
                    if (target.getArmorCoverPercentage() > 0) {
                        event.setAmount(event.getAmount() + effectLevel * target.getArmorCoverPercentage());
                    } else {
                        event.setAmount(event.getAmount() + effectLevel);
                    }
                    ((ServerLevel) target.level()).getChunkSource().broadcastAndSend(target, new ClientboundAnimatePacket(target, 5));
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientInit() {
        IStatGetter statGetter = new StatGetterEffectLevel(punishingEffect, 1);
        GuiStatBar statBar = new GuiStatBar(0, 0, StatsHelper.barLength, "tetra.stats.punishing",
                0, 10, false, false, false, statGetter, LabelGetterBasic.integerLabel,
                new TooltipGetterInteger("tetra.stats.punishing.tooltip", statGetter));

        WorkbenchStatsGui.addBar(statBar);
        HoloStatsGui.addBar(statBar);
    }
}

package se.mickelus.tetracelium.compat.twilightforest.effects;

import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
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
import se.mickelus.tetra.gui.stats.getter.TooltipGetterDecimalSingle;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;

public class ChargingEffect {
    public static final ItemEffect chargingEffect = ItemEffect.get("charging");

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();
        Entity source = event.getSource().getDirectEntity();
        if (!target.level().isClientSide()
                && source instanceof LivingEntity attacker
                && source.isSprinting()
                && (event.getSource().getMsgId().equals("player") || event.getSource().getMsgId().equals("mob"))) {
            ItemStack weaponStack = attacker.getMainHandItem();
            if (!weaponStack.isEmpty() && weaponStack.getItem() instanceof IModularItem item) {
                float chargingLevel = item.getEffectLevel(weaponStack, chargingEffect);
                if (chargingLevel > 0) {
                    event.setAmount(event.getAmount() + chargingLevel * 0.5f);
                    ((ServerLevel) target.level()).getChunkSource().broadcastAndSend(target, new ClientboundAnimatePacket(target, 5));
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientInit() {
        IStatGetter statGetter = new StatGetterEffectLevel(chargingEffect, 0.5);
        GuiStatBar statBar = new GuiStatBar(0, 0, StatsHelper.barLength, "tetra.stats.charging",
                0, 10, false, false, false, statGetter, LabelGetterBasic.singleDecimalLabel,
                new TooltipGetterDecimalSingle("tetra.stats.charging.tooltip", statGetter));

        WorkbenchStatsGui.addBar(statBar);
        HoloStatsGui.addBar(statBar);
    }
}

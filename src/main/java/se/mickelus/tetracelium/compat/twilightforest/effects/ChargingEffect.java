package se.mickelus.tetracelium.compat.twilightforest.effects;

import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.items.modular.IModularItem;

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
                event.setAmount(event.getAmount() + chargingLevel * 0.5f);
                ((ServerLevel) target.level()).getChunkSource().broadcastAndSend(target, new ClientboundAnimatePacket(target, 5));
            }
        }
    }
}

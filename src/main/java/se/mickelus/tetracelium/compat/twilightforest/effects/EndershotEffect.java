package se.mickelus.tetracelium.compat.twilightforest.effects;

import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.event.ModularProjectileSpawnEvent;
import se.mickelus.tetra.items.modular.IModularItem;
import twilightforest.item.EnderBowItem;

public class EndershotEffect {
    public static final ItemEffect enderShotEffect = ItemEffect.get("enderShot");

    @SubscribeEvent
    public static void onArrowLoose(ModularProjectileSpawnEvent event) {
        if (event.getFiringStack().getItem() instanceof IModularItem item
                && item.getEffectLevel(event.getFiringStack(), enderShotEffect) > 0) {
            event.getProjectileEntity().getPersistentData().putBoolean(EnderBowItem.KEY, true);
        }
    }
}

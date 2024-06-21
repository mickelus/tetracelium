package se.mickelus.tetracelium.compat.soul_fire_d;

import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.enchantment.FireEnchantmentHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.event.ModularProjectileSpawnEvent;

public class SoulFiredEffect {

    @SubscribeEvent
    public static void onModularProjectileSpawn(ModularProjectileSpawnEvent event) {
        FireEnchantmentHelper.FireEnchantment fireEnchantment = FireEnchantmentHelper.getWhichFlame(event.getFiringStack());
        if (fireEnchantment.isApplied()) {
            FireManager.setOnFire(event.getProjectileEntity(), 100, fireEnchantment.getFireType());
        }
    }
}

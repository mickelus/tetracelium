package se.mickelus.tetracelium;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import se.mickelus.tetracelium.compat.farmersdelight.DummyKnifeBladeItem;

public class TetraceliumRegistries {
    public static final DeferredRegister<Item> items = DeferredRegister.create(ForgeRegistries.ITEMS, TetraceliumMod.MOD_ID);

    public static void init(IEventBus bus) {
        bus.register(TetraceliumRegistries.class);
        items.register(bus);

        items.register(DummyKnifeBladeItem.identifier, DummyKnifeBladeItem::new);
    }
}

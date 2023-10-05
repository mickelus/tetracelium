package se.mickelus.tetracelium.compat.farmersdelight;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolAction;

public class DummyKnifeBladeItem extends Item {
    public static final String identifier = "dummy_knife_blade";

    public DummyKnifeBladeItem() {
        super(new Properties());
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return FarmersDelightToolActions.bladeCut.equals(toolAction);
    }
}

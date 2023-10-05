package se.mickelus.tetracelium.compat.botania;

import com.google.common.collect.Streams;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.effect.EffectHelper;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.event.ModularItemDamageEvent;
import se.mickelus.tetra.gui.stats.StatsHelper;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;
import se.mickelus.tetra.gui.stats.getter.LabelGetterBasic;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterInteger;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ManaRepair {
    public static ItemEffect effect = ItemEffect.get("manaRepair");

    @OnlyIn(Dist.CLIENT)
    public static void clientInit() {
        IStatGetter statGetter = new ManaRepairStatGetter();
        GuiStatBar statBar = new GuiStatBar(0, 0, StatsHelper.barLength, "tetra.stats.manaRepair",
                0, 400, false, false, true, statGetter, LabelGetterBasic.integerLabelInverted,
                new TooltipGetterInteger("tetra.stats.manaRepair.tooltip", statGetter));

        WorkbenchStatsGui.addBar(statBar);
        HoloStatsGui.addBar(statBar);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!event.player.level().isClientSide && event.player.level().getGameTime() % 20 == 0) {
            Inventory inventory = event.player.getInventory();
            Streams.concat(inventory.items.stream(), inventory.armor.stream(), inventory.offhand.stream())
                    .filter(itemStack -> !itemStack.isEmpty())
                    .filter(itemStack -> itemStack.getItem() instanceof IModularItem)
                    .forEach(itemStack -> {
                        itemInventoryTick(itemStack, event.player);
                    });
        }
    }

    public static void itemInventoryTick(ItemStack itemStack, Entity entity) {
        float durabilityPerMana = EffectHelper.getEffectEfficiency(itemStack, effect);
        if (durabilityPerMana > 0 && itemStack.getDamageValue() > 0 && entity instanceof Player player) {
            int damage = itemStack.getDamageValue();
            int repairAmount = requestManaForDurability(player, itemStack, damage, durabilityPerMana);
            if (repairAmount > 0) {
                itemStack.setDamageValue(damage - repairAmount);
//                    player.getInventory().setChanged();
            }
        }
    }

    @SubscribeEvent
    public static void onModularItemDamage(ModularItemDamageEvent event) {
        event.setAmount(reduceDurabilityDamage(event.getUsingEntity(), event.getItemStack(), event.getAmount()));
    }

    public static int reduceDurabilityDamage(Entity entity, ItemStack itemStack, int amount) {
        float durabilityPerMana = EffectHelper.getEffectEfficiency(itemStack, effect);
        if (durabilityPerMana > 0 && entity instanceof Player player) {
            int result = requestManaForDurability(player, itemStack, amount, durabilityPerMana);
            return amount - result;
        }
        return amount;
    }

    private static int requestManaForDurability(Player player, ItemStack itemStack, int damage, float durabilityPerMana) {
        int grantedMana = 0; //ManaItemHandler.instance().requestManaForTool(itemStack, player, Mth.ceil(damage / durabilityPerMana), true);
        return Math.min((int) (durabilityPerMana * grantedMana), damage);
    }
}

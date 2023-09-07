package se.mickelus.tetracompat.compat.botania;

import com.google.common.collect.Streams;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetracompat.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetracompat.effect.EffectHelper;
import se.mickelus.tetracompat.effect.ItemEffect;
import se.mickelus.tetracompat.gui.stats.StatsHelper;
import se.mickelus.tetracompat.gui.stats.bar.GuiStatBar;
import se.mickelus.tetracompat.gui.stats.getter.IStatGetter;
import se.mickelus.tetracompat.gui.stats.getter.LabelGetterBasic;
import se.mickelus.tetracompat.gui.stats.getter.TooltipGetterInteger;
import se.mickelus.tetracompat.items.modular.impl.holo.gui.craft.HoloStatsGui;
import vazkii.botania.api.mana.ManaItemHandler;

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
        Inventory inventory = event.player.getInventory();
        Streams.concat(inventory.items.stream(), inventory.armor.stream(), inventory.offhand.stream())
                .filter(itemStack -> !itemStack.isEmpty())
                .filter(itemStack -> itemStack.getItem() instanceof IModularItem)
                .forEach(itemStack -> {
                    itemInventoryTick(itemStack, event.player.level(), event.player);
                });
    }

    public static void itemInventoryTick(ItemStack itemStack, Level world, Entity entity) {
        if (!world.isClientSide && world.getGameTime() % 20 == 0) {
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
    }

    public static int reduceDurabilityDamage(Level level, Entity entity, ItemStack itemStack, int amount) {
        if (entity instanceof Player player) {
            float durabilityPerMana = EffectHelper.getEffectEfficiency(itemStack, effect);
            if (durabilityPerMana > 0) {
                int result = requestManaForDurability(player, itemStack, amount, durabilityPerMana);
                return amount - result;
            }
        }
        return amount;
    }

    private static int requestManaForDurability(Player player, ItemStack itemStack, int damage, float durabilityPerMana) {
        int grantedMana = ManaItemHandler.instance().requestManaForTool(itemStack, player, Mth.ceil(damage / durabilityPerMana), true);
        return Math.min((int) (durabilityPerMana * grantedMana), damage);
    }
}

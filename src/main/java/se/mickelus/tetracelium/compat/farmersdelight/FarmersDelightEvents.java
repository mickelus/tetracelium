package se.mickelus.tetracelium.compat.farmersdelight;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.items.modular.ItemModularHandheld;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;

public class FarmersDelightEvents {
    @SubscribeEvent
    public static void onSneakPlaceTool(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        ItemStack heldStack = player.getMainHandItem();
        BlockEntity tileEntity = level.getBlockEntity(event.getPos());
        if (player.isSecondaryUseActive()
                && !heldStack.isEmpty()
                && tileEntity instanceof CuttingBoardBlockEntity
                && heldStack.getItem() instanceof ItemModularHandheld) {
            boolean success = ((CuttingBoardBlockEntity) tileEntity).carveToolOnBoard(player.getAbilities().instabuild ? heldStack.copy() : heldStack);
            if (success) {
                level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 0.8F);
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }

    }
}

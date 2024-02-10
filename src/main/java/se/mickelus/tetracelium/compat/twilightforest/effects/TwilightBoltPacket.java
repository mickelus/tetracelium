package se.mickelus.tetracelium.compat.twilightforest.effects;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import se.mickelus.mutil.network.AbstractPacket;

public class TwilightBoltPacket extends AbstractPacket {
    public TwilightBoltPacket() {
    }

    public void toBytes(FriendlyByteBuf buffer) {
    }

    public void fromBytes(FriendlyByteBuf buffer) {
    }

    public void handle(Player player) {
        TwilightBoltEffect.handleServer(player);
    }
}

package se.mickelus.tetracelium.compat.twilightforest.effects;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import se.mickelus.mutil.network.AbstractPacket;

public class SapParticlePacket extends AbstractPacket {
    InteractionHand hand;
    Vec3 target;

    public SapParticlePacket() {
    }

    public SapParticlePacket(InteractionHand hand, Vec3 target) {
        this.hand = hand;
        this.target = target;
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeByte(hand == null ? 2 : hand.ordinal());
        buffer.writeVector3f(target.toVector3f());
    }

    public void fromBytes(FriendlyByteBuf buffer) {
        int handByte = buffer.readByte();
        if (handByte != 2) {
            hand = InteractionHand.values()[handByte];
        }
        target = new Vec3(buffer.readVector3f());
    }

    public void handle(Player player) {
        SappingEffect.makeRedMagicTrail(player.level(), hand, player, target);
    }
}

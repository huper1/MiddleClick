package me.huper_1.middleclick.client.mixin;


import me.huper_1.middleclick.client.packet.ContainerInteraction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void clickMouse(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        var client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;

        if (player != null) {
            if (button == 2) {
                var slot = getSlotAt(mouseX, mouseY);

                if (slot != null) {
                    if (!player.isCreative()) {
                        var containerInteraction = new ContainerInteraction();
                        containerInteraction.sendPacket(slot.id, player.currentScreenHandler.syncId,
                                0, button, SlotActionType.CLONE, slot.getStack());
                    }
                }
            }
        }
    }

    @Shadow
    protected abstract Slot getSlotAt(double x, double y);
}

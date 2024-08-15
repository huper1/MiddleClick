package me.huper_1.middleclick.client.packet;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import org.jetbrains.annotations.NotNull;

public class ContainerInteraction {

    public static void sendPacket(int slot, int syncId, int revision, int button,
                                  @NotNull SlotActionType actionType, @NotNull ItemStack stack) {

        var client = MinecraftClient.getInstance();
        var player = client.player;

        if (player == null) return;

        var map = getChangeItems(slot, stack);
        var slotC2SPacket = new ClickSlotC2SPacket(syncId, revision, slot, button, actionType, stack, map);

        var network = client.getNetworkHandler();
        if (network != null) {
            network.sendPacket(slotC2SPacket);
        }
    }

    private static Int2ObjectMap<ItemStack> getChangeItems(int slot, @NotNull ItemStack itemStack) {
        Int2ObjectMap<ItemStack> changeItems = new Int2ObjectOpenHashMap<>();
        changeItems.put(slot, itemStack.copyWithCount(64));

        return changeItems;
    }
}
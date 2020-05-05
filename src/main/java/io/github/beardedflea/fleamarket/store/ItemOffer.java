package io.github.beardedflea.fleamarket.store;

import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.command.CommandBase;

import javax.annotation.Nullable;

public class ItemOffer {

    private final ItemStack itemStack;
    private final String nbtRaw, soldMessage, broadcastMessage, rewardCommand;
    private final int uptime;

    public ItemOffer(Item item, int meta, @Nullable String nbtString,
                     String sellMsg, String broadcastMsg, String rewardCmd, int time) {

        itemStack = new ItemStack(item, 1, meta);
        nbtRaw = nbtString;
        soldMessage = sellMsg;
        broadcastMessage = broadcastMsg;
        rewardCommand = rewardCmd;
        uptime = time;
    }

    public ItemStack getItemStack(){
        return itemStack;
    }
}

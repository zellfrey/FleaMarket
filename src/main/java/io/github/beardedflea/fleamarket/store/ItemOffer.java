package io.github.beardedflea.fleamarket.store;

import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.command.CommandBase;

import javax.annotation.Nullable;

public class ItemOffer {

    private final ItemStack itemStack;
    private final String nbtRaw, soldMessage, broadcastMessage, rewardCommand;
    private final int amount, uptime;

    public ItemOffer(Item item, int meta, @Nullable String nbtString,
                     int quantity, String sellMsg, String broadcastMsg, String rewardCmd, int time) {

        itemStack = new ItemStack(item, 1, meta);
        amount = quantity;
        nbtRaw = nbtString;
        soldMessage = sellMsg;
        broadcastMessage = broadcastMsg;
        rewardCommand = rewardCmd;
        uptime = time;
    }

    public ItemStack getItemStack(){
        return itemStack;
    }

    public String getSoldMsg(){return soldMessage;}

    public String getBroadcastMsg(){return broadcastMessage;}

    public int getItemAmount(){return amount;}

    public String getNbtRaw(){return nbtRaw;}

    public String getDisplayName(){
        return itemStack.getItem().getItemStackDisplayName(itemStack);
    }
    public String getItemName(){
        String itemToRemove = itemStack.getItem().getRegistryName() + "";
        itemToRemove += itemStack.getItem().getMetadata(itemStack) != 0 ? ":" + itemStack.getItem().getMetadata(itemStack) : "";
        return itemToRemove;
    }
}

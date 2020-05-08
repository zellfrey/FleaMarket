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
        return this.itemStack;
    }

    public String getSoldMsg(){return this.soldMessage;}

    public String getBroadcastMsg(){return this.broadcastMessage;}

    public int getItemAmount(){return this.amount;}

    public String getNbtRaw(){return this.nbtRaw;}

    public String getDisplayName(){
        return this.itemStack.getItem().getItemStackDisplayName(this.itemStack);
    }

    public String getItemName(){
        String itemToRemove = this.itemStack.getItem().getRegistryName() + "";
        itemToRemove += this.itemStack.getItem().getMetadata(this.itemStack) != 0 ? ":" + this.itemStack.getItem().getMetadata(this.itemStack) : "";
        return itemToRemove;
    }
}

package io.github.beardedflea.fleamarket.store;

import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.command.CommandBase;

public class ItemOffer {

//    private final ItemStack itemStack;
//    private final String item, nbtRaw, soldMessage, broadcastMessage, rewardCommand;
//    private final int amount, uptime;
//
//
//    public ItemOffer(Item item, @Nullable String nbtString, String soldMessage, String broadcastMessage, String rewardCommand, int amount, int uptime){
//        this.nbtRaw = nbtString;
//        this.itemStack = item;

//    }
    public static final String currentItemName  = "minecraft:carpet:3";
//    public static final Item currentItemOffer = CommandBase.getItemByText(null, currentItemName);
//    public static final String currentItemNBTRaw = "{Potion:\"minecraft:long_night_vision\"}";
    public static final String currentItemNBTRaw = "";
    public static final int currentItemAmount = 25;

    public static String currentItemBroadcast = "Flea Market is offering $400 for 25 potions of long night vision";

//    public static final ItemStack currentItem = new ItemStack(currentItemOffer,currentItemAmount, 0, currentItemNBTRaw);
}

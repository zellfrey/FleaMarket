package io.github.beardedflea.fleamarket.store;

import io.github.beardedflea.fleamarket.FleaMarket;
import io.github.beardedflea.fleamarket.utils.TextUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nullable;

public class ItemOffer {

    private ItemStack itemStack;
    private String nbtRaw, soldMessage, broadcastMessage, rewardCommand;
    private int amount, uptime;

    public ItemOffer(Item item, int meta, @Nullable String nbtString,
                     int quantity, String sellMsg, String broadcastMsg, String rewardCmd, int time) {

        itemStack = new ItemStack(item, 1, meta);
        amount = quantity;
        nbtRaw = nbtString;
        this.setNbtTag();
        rewardCommand = rewardCmd;
        uptime = time;
        //Message doesn't need to constantly change due to playerName, might as well execute once and be done with it
        soldMessage = TextUtils.replaceItemAndAmount(sellMsg, this.amount, this.getDisplayName());
        broadcastMessage = TextUtils.replaceItemAndAmount(broadcastMsg, this.amount, this.getDisplayName());

    }
    private void setNbtTag (){
        if(nbtRaw != null) {
            try {
                NBTTagCompound nbt = JsonToNBT.getTagFromJson(this.nbtRaw);
                this.itemStack.setTagCompound(nbt);
            }
            catch (NBTException e) {
                FleaMarket.getLogger().error("unable to parse NBT string: {}", nbtRaw);
            }
        }
    }

    public int getUpTime(){ return this.uptime; }

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

    public String getRewardCommand(){ return this.rewardCommand; }


    public void activate(MinecraftServer server, EntityPlayerMP playerMP){
        String rwdCmdString = TextUtils.replacePlayerPlaceHolder(this.rewardCommand, playerMP.getName());
        FleaMarket.getLogger().info(rwdCmdString);
        server.commandManager.executeCommand(server, rwdCmdString);
    }
}

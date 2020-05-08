package io.github.beardedflea.fleamarket.store;

import io.github.beardedflea.fleamarket.config.FleaMarketConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.entity.player.EntityPlayerMP;

import io.github.beardedflea.fleamarket.FleaMarket;
import io.github.beardedflea.fleamarket.store.ItemOffer;
import io.github.beardedflea.fleamarket.utils.TextUtils;

import java.util.ArrayList;

public class ItemOfferList {

    private static final ArrayList<ItemOffer> ITEM_OFFERS = new ArrayList<>();

    private static ArrayList<String> playerTransactionList = new ArrayList<>();

    private static ItemOffer currentItemOffer;


//    private static int itemOfferIndx;

    //manages ITEM_OFFERS array
    public static void addItemOffer(ItemOffer itemOffer) {
        ITEM_OFFERS.add(itemOffer);
    }

    public static void clearItemOffers(){
        ITEM_OFFERS.clear();
    }

    public static int getItemOfferSize(){
        return ITEM_OFFERS.size();
    }

    //manages PlayerTransactionList array
    public static ArrayList<String> getPlayerTransactionList(){return playerTransactionList;}

    private static void addPlayerTransactionUUID(String uuid){playerTransactionList.add(uuid);}

    private static void clearPlayerTransactionList(){playerTransactionList.clear();}

    private static boolean checkPlayerTransactionList(String uuid){
        boolean soldItemOffer = false;

        for(String playerUUID : playerTransactionList){
            if(playerUUID.equals(uuid)){
                soldItemOffer = true;
            }
        }

        return soldItemOffer;
    }

    //methods below are executed via commands
    //opfm start
    public static boolean startItemOfferCycle(MinecraftServer server){
        if(currentItemOffer != null){
            return false;
        }
        else{
            setCurrentItemOffer(server);
            return true;
        }
    }
    //opfm skip & currentItemOffer.uptime runs out
    public static void setCurrentItemOffer(MinecraftServer server){
        if(ITEM_OFFERS.size() == 0){
            FleaMarket.getLogger().error("No itemOffers exist. Cannot implement itemOffers");
            FleaMarket.getLogger().info("Try reloading the files with /opfm reload" );
            return;
        }
        currentItemOffer = ITEM_OFFERS.get(0);
        clearPlayerTransactionList();
        server.getPlayerList().sendMessage(new TextComponentString(currentItemOffer.getBroadcastMsg()));
    }

    //fm sell
    public static void sellItemOffer(EntityPlayerMP playerMP){
        String playerUUID = playerMP.getUniqueID().toString();

        if(ItemOfferList.checkPlayerTransactionList(playerUUID)){
            playerMP.sendMessage(new TextComponentString(TextFormatting.GREEN + "You have already sold the current item offer to Flea Market"));
            return;
        }
        boolean containsItemOffer = false;
        int amountOfCorrectItem = 0;
        //scans inventory checking for current ItemOffer
        for(ItemStack item : playerMP.inventory.mainInventory) {

            String itemFullName = item.getItem().getRegistryName() + "";
            int itemDamageNum = item.getItem().getDamage(item);
            //Stitches the registry name with the meta data id: modName:blockName:dmgVal
            itemFullName += itemDamageNum != 0 ? ":" + itemDamageNum : "";
            String itemNBTRaw = item.getItem().getNBTShareTag(item) + "";

            if(FleaMarketConfig.debugMode){
                TextUtils.printDebugStrConsole(item.getItem().getItemStackDisplayName(item), itemFullName, itemNBTRaw);
            }

            if(itemFullName.equals(currentItemOffer.getItemName())
                    && itemNBTRaw.equals(currentItemOffer.getNbtRaw() + "")) {

                containsItemOffer = true;
                amountOfCorrectItem += item.getCount();
                FleaMarket.getLogger().info("item found, moving to item removal");

            }
        }
        //checks if item is inventory
        if(containsItemOffer){
            //final check to see if the amount is equal or greater to the desired amount in the offer
            if(amountOfCorrectItem >= currentItemOffer.getItemAmount()){
//                int k = playerMP.inventory.clearMatchingItems( , 0,3,  null);
//                playerMP.inventoryContainer.detectAndSendChanges();
                FleaMarket.getLogger().info("Removed {} items from {}'s inventory", amountOfCorrectItem, playerMP.getName());

                playerMP.sendMessage(new TextComponentString(TextFormatting.GOLD + currentItemOffer.getSoldMsg()));

                ItemOfferList.addPlayerTransactionUUID(playerMP.getUniqueID().toString());
            }
            else{
                playerMP.sendMessage(new TextComponentString("You only have " + amountOfCorrectItem + " " +
                        currentItemOffer.getDisplayName() +  " in your inventory."));
            }
        }
        else{
            playerMP.sendMessage(new TextComponentString(TextFormatting.RED + "You do not have the required item on offer"));
        }
    }
}

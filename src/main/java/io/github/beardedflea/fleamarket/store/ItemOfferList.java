package io.github.beardedflea.fleamarket.store;

import io.github.beardedflea.fleamarket.FleaMarket;
import io.github.beardedflea.fleamarket.config.FleaMarketConfig;
import io.github.beardedflea.fleamarket.config.CurrentItemOfferParser;
import static io.github.beardedflea.fleamarket.utils.TextUtils.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.ArrayList;




public class ItemOfferList {

    private static  ArrayList<ItemOffer> ITEM_OFFERS = new ArrayList<>();

    private static ArrayList<String> playerTransactionList = new ArrayList<>();

    public static boolean pauseCycle = false;

    public static ItemOffer currentItemOffer;

    public static int itemOfferUptime = 0;

    public static int itemOfferIndex = -1;

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

    public static void addPlayerTransactionUUID(String uuid){playerTransactionList.add(uuid);}

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
        switch(FleaMarketConfig.selectionType){
            case "descending" :
                if(itemOfferIndex == -1){
                    itemOfferIndex = 0;
                }else{
                    itemOfferIndex = itemOfferIndex == ITEM_OFFERS.size()-1 ? 0 : itemOfferIndex+1;
                }
            break;

            case "ascending" :
                if(itemOfferIndex == -1){
                    itemOfferIndex = ITEM_OFFERS.size()-1;
                }else {
                    itemOfferIndex = itemOfferIndex == 0 ? ITEM_OFFERS.size()-1 : itemOfferIndex-1;
                }
            break;

            case "random" :
                 itemOfferIndex = (int)(Math.random() * ITEM_OFFERS.size());
            break;

//            case "fairrandom":
//
//                //"\"fairrandom\" = once and item has been used, it will not pick that item again until the list has been completed."
//                // Will implement at somepoint in version 1.1
//                break;

            default:
                throw new IllegalStateException("Unexpected value: " + FleaMarketConfig.selectionType);
        }

        if(FleaMarketConfig.debugMode){
            printDebugStrConsole(itemOfferIndex+"", FleaMarketConfig.selectionType, ITEM_OFFERS.size()+"");
        }

        currentItemOffer = ITEM_OFFERS.get(itemOfferIndex);
        itemOfferUptime = ItemOfferList.currentItemOffer.getUpTime();
        clearPlayerTransactionList();
        server.getPlayerList().sendMessage(new TextComponentString(ItemOfferList.currentItemOffer.getBroadcastMsg()));
        CurrentItemOfferParser.saveCurrentItemOffer();
    }

    //fm sell
    public static void sellItemOffer(EntityPlayerMP playerMP){
        String playerUUID = playerMP.getUniqueID().toString();
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

        if(ItemOfferList.checkPlayerTransactionList(playerUUID)){
//            playerMP.sendMessage(TransformModLanguage(modLanguageMap.get("alreadySoldMsg")));
            return;
        }
        int amountOfCorrectItem = 0;
        //scans inventory checking for current ItemOffer
        for(ItemStack item : playerMP.inventory.mainInventory) {

            String itemFullName = item.getItem().getRegistryName().toString();
            int itemDamageNum = item.getItem().getDamage(item);
            //Stitches the registry name with the meta data id: modName:blockName:dmgVal
            itemFullName += itemDamageNum != 0 ? ":" + itemDamageNum : "";
            String itemNBTRaw = item.getItem().getNBTShareTag(item) + "";

            if(FleaMarketConfig.debugMode){ printDebugStrConsole(item.getItem().getItemStackDisplayName(item), itemFullName, itemNBTRaw); }

            if(itemFullName.equals(currentItemOffer.getItemName())
                    && itemNBTRaw.equals(currentItemOffer.getNbtRaw() + "")) {

                amountOfCorrectItem += item.getCount();
            }
        }
        //checks if item is inventory
        if(amountOfCorrectItem > 0){
            //final check to see if the amount is equal or greater to the desired amount in the offer
            if(amountOfCorrectItem >= currentItemOffer.getItemAmount()){
                int k = playerMP.inventory.clearMatchingItems(currentItemOffer.getItemStack().getItem(), currentItemOffer.getItemStack().getMetadata(),
                    currentItemOffer.getItemAmount(), currentItemOffer.getItemStack().getTagCompound());

                playerMP.inventoryContainer.detectAndSendChanges();

                if(FleaMarket.isDebugMode()){
                    FleaMarket.getLogger().info("Removed {} items from {}'s inventory", currentItemOffer.getItemAmount(), playerMP.getName());
                }
                String soldString = replacePlayerPlaceHolder(currentItemOffer.getSoldMsg(), playerMP.getName());

                playerMP.sendMessage(new TextComponentString(soldString));

                ItemOfferList.addPlayerTransactionUUID(playerMP.getUniqueID().toString());
                currentItemOffer.activate(server, playerMP);

                CurrentItemOfferParser.saveCurrentItemOffer();

            }
            else{
                playerMP.sendMessage(new TextComponentString("You only have " + amountOfCorrectItem + " " +
                        currentItemOffer.getDisplayName() +  " in your inventory."));
            }
        }
        else{
            playerMP.sendMessage(new TextComponentString(modLanguageMap.get("noItemFoundMsg")));
        }
    }
}

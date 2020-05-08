package io.github.beardedflea.fleamarket.store;

import net.minecraft.util.text.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.entity.player.EntityPlayerMP;

import io.github.beardedflea.fleamarket.FleaMarket;
import io.github.beardedflea.fleamarket.store.ItemOffer;
import io.github.beardedflea.fleamarket.utils.TextUtils;

import java.util.ArrayList;

public class ItemOfferList {

    private static final ArrayList<ItemOffer> ITEM_OFFERS = new ArrayList<>();

    private static ArrayList<String> PlayerTransactionList = new ArrayList<>();

    public static ItemOffer currentItemOffer;


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

    public static boolean startItemOfferCycle(MinecraftServer server){
        if(currentItemOffer != null){
            return false;
        }
        else{
            setCurrentItemOffer(server);
            return true;
        }
    }

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

    //manages PlayerTransactionList array
    public static void addPlayerTransactionUUID(String uuid){PlayerTransactionList.add(uuid);}

    public static void clearPlayerTransactionList(){PlayerTransactionList.clear();}

    public static boolean checkPlayerTransactionList(String uuid){
        boolean soldItemOffer = false;

        for(String playerUUID : PlayerTransactionList){
            if(playerUUID.equals(uuid)){
                soldItemOffer = true;
            }
        }

        return soldItemOffer;
    }
}

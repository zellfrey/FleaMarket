package io.github.beardedflea.fleamarket.event;

import io.github.beardedflea.fleamarket.store.ItemOffer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.relauncher.Side;

import io.github.beardedflea.fleamarket.FleaMarket;
import io.github.beardedflea.fleamarket.config.FleaMarketConfig;
import io.github.beardedflea.fleamarket.store.ItemOfferList;

@Mod.EventBusSubscriber(value = Side.SERVER, modid = FleaMarket.MODID)
public class BroadcastEventHandler {

    //run checking on 1 minute basis i.e 1200 minecraft ticks
    private static final int broadCastInterval =  FleaMarketConfig.broadcastReminder;
    private static int salesInterval = FleaMarketConfig.saleInterval;
    public static MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

    private static int minuteCounter = 0;
    private static int broadcastCounter = 0;
    private static int salesIntervalCounter = 0;
    private static boolean executing = false;

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if(!executing && ItemOfferList.currentItemOffer != null) {

            if(broadCastInterval != 0 && ItemOfferList.itemOfferUptime > 0) {
                if (++broadcastCounter >= 1200 * broadCastInterval *2) {
                    executing = true;
                    broadcastCounter -= 1200 * broadCastInterval *2;
                    server.getPlayerList().sendMessage(new TextComponentString(ItemOfferList.currentItemOffer.getBroadcastMsg()));
                    executing = false;
                }
            }
            if(ItemOfferList.itemOfferUptime != 0 ){
                if(++minuteCounter >= 1200 *2) {
                    executing = true;
                    minuteCounter -= 1200 *2;

                    if(!ItemOfferList.pauseCycle){
                        ItemOfferList.itemOfferUptime--;

                    }else{
                        FleaMarket.getLogger().info("Item offer cycle is paused!");
                        FleaMarket.getLogger().info(ItemOfferList.itemOfferUptime + " minutes remaining for item offer");
                    }
                    executing = false;
                    if(salesInterval == 0 && ItemOfferList.itemOfferUptime == 0){
                        ItemOfferList.setCurrentItemOffer(server);
                    }
                }
            }
            if(salesInterval != 0 && ItemOfferList.itemOfferUptime <= 0){

                if(++salesIntervalCounter >= 1200 *2) {
                    executing = true;
                    salesIntervalCounter -= 1200 *2;
                    salesInterval -= 1;
                    executing = false;

                    if(salesInterval == 0){
                        ItemOfferList.setCurrentItemOffer(server);
                        salesInterval = FleaMarketConfig.saleInterval;
                    }
                }
            }

        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if( (FleaMarketConfig.joinMessage) && (ItemOfferList.currentItemOffer != null) ){
            event.player.sendMessage(new TextComponentString(ItemOfferList.currentItemOffer.getBroadcastMsg()));
        }
    }
}
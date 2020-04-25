package io.github.beardedflea.fleamarket.event;

import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.relauncher.Side;

import io.github.beardedflea.fleamarket.FleaMarket;
import io.github.beardedflea.fleamarket.config.FleaMarketConfig;

@Mod.EventBusSubscriber(value = Side.SERVER, modid = FleaMarket.MODID)
public class BroadcastEventHandler {

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if(FleaMarketConfig.joinMessage){
            event.player.sendMessage(new TextComponentString(TextFormatting.BLUE + "This is a player broadcast test"));
        }
    }

}
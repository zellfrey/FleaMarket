package io.github.beardedflea.fleamarket.event;

import io.github.beardedflea.fleamarket.FleaMarket;
import io.github.beardedflea.fleamarket.store.ItemOfferList;
import io.github.beardedflea.fleamarket.utils.ModUpdateHandler;
import io.github.beardedflea.fleamarket.utils.TextUtils;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.SERVER, modid = FleaMarket.MODID)
public class PlayerEventHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {

        if( (FleaMarket.config.joinMessage()) && (ItemOfferList.currentItemOffer != null) ){
            event.player.sendMessage(new TextComponentString(ItemOfferList.currentItemOffer.getBroadcastMsg()));
        }

        if(FleaMarket.config.updateCheckerEnabled() && FleaMarket.isOpped(event.player.getGameProfile())){
            if(ModUpdateHandler.hasUpdate(ModUpdateHandler.getResult())){

                String notifyUpdate = "&2There's an update available for &6Flea Market&2!\n";
                String availableUpdate = "&2Check the server log for more details.";
                String updateNotification = TextUtils.TransformModLanguageConfig(notifyUpdate+ availableUpdate);

                event.player.sendMessage(new TextComponentString(updateNotification));
            }

        }
    }
}

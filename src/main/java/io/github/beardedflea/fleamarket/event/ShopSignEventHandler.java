package io.github.beardedflea.fleamarket.event;

import io.github.beardedflea.fleamarket.FleaMarket;
import io.github.beardedflea.fleamarket.store.ItemOfferList;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ShopSignEventHandler {

    public static void init() {
        FleaMarket.getLogger().info("Registering ShopSignEventHandler...");
        MinecraftForge.EVENT_BUS.register(ShopSignEventHandler.class);

    }

    public static void shutdown(){
        FleaMarket.getLogger().info("ShopSignEventHandler shutting down...");
        MinecraftForge.EVENT_BUS.unregister(ShopSignEventHandler.class);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onSignInteract(PlayerInteractEvent event){
        World world = event.getWorld();
        Block targetBlock = world.getBlockState(event.getPos()).getBlock();

        if(targetBlock.getLocalizedName().equals("Sign")){
            TileEntitySign shopSign = (TileEntitySign) world.getTileEntity(event.getPos());
            int fmSignID = world.loadedTileEntityList.indexOf(shopSign);

            if(shopSign.signText[0].getUnformattedText().equals("[FLEAMARKET]")) {
                String name = event.getEntityPlayer().getName();
                EntityPlayerMP playerMP = event.getEntityPlayer().getServer().getPlayerList().getPlayerByUsername(name);

                if(event instanceof PlayerInteractEvent.RightClickBlock){
                    if(ItemOfferList.checkItemOffer(playerMP)) ItemOfferList.sellItemOffer(playerMP);

                }else if(event instanceof PlayerInteractEvent.LeftClickBlock){
                    if(ItemOfferList.checkItemOffer(playerMP))
                        playerMP.sendMessage(new TextComponentString(ItemOfferList.currentItemOffer.getBroadcastMsg()));
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void shouldSignBreak(BlockEvent.BreakEvent event){
        World world = event.getWorld();
        Block targetBlock = world.getBlockState(event.getPos()).getBlock();

        if(targetBlock.getLocalizedName().equals("Sign")){
            EntityPlayer player = event.getPlayer();
            TileEntitySign shopSign = (TileEntitySign) world.getTileEntity(event.getPos());
            int fmSignID = world.loadedTileEntityList.indexOf(shopSign);
            FleaMarket.getLogger().info(fmSignID);
            if(!FleaMarket.isOpped(player.getGameProfile()) && shopSign.signText[0].getUnformattedText().equals("[FLEAMARKET]")){

                event.setCanceled(true);
            }
        }
    }
}

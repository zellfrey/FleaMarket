package io.github.beardedflea.fleamarket.event;

import io.github.beardedflea.fleamarket.FleaMarket;
import io.github.beardedflea.fleamarket.config.ShopSignParser;
import io.github.beardedflea.fleamarket.store.ItemOfferList;
import io.github.beardedflea.fleamarket.store.ShopSign;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
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
    public static void onSignInteract(PlayerInteractEvent.RightClickBlock event){
        World world = event.getWorld();
        IBlockState state = world.getBlockState(event.getPos());

        if (state.getBlock() != Blocks.WALL_SIGN && state.getBlock() != Blocks.STANDING_SIGN) {
            return;
        }
        if(ShopSign.shopSigns.isEmpty()){
            return;
        }

        TileEntitySign potentialShopSign = (TileEntitySign) world.getTileEntity(event.getPos());

        if(ShopSign.isRegistered(potentialShopSign.getPos())) {
            String name = event.getEntityPlayer().getName();
            EntityPlayerMP playerMP = event.getEntityPlayer().getServer().getPlayerList().getPlayerByUsername(name);

            if(event.getEntityPlayer().isSneaking()){
                if(ItemOfferList.checkItemOffer(playerMP)) ItemOfferList.sellItemOffer(playerMP);

            }else{
                if(ItemOfferList.checkItemOffer(playerMP))
                    playerMP.sendMessage(new TextComponentString(ItemOfferList.currentItemOffer.getBroadcastMsg()));
                }
            }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void shouldRegisterShopSign(PlayerInteractEvent.LeftClickBlock event){
        World world = event.getWorld();
        IBlockState state = world.getBlockState(event.getPos());

        if (state.getBlock() != Blocks.WALL_SIGN && state.getBlock() != Blocks.STANDING_SIGN) {
            return;
        }

        EntityPlayer player = event.getEntityPlayer();
        TileEntitySign shopSign = (TileEntitySign) world.getTileEntity(event.getPos());
        String sign1stLine = shopSign.signText[0].getUnformattedText();


        if(sign1stLine.contains("[FLEAMARKET]") && event.getEntityPlayer().isSneaking()) {
            int dimID = event.getEntityPlayer().dimension;
            ShopSign.registerShopSign(player, dimID, shopSign.getPos());
            ShopSign.updateShopSigns();
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void shouldSignBreak(BlockEvent.BreakEvent event) {
        World world = event.getWorld();
        IBlockState state = world.getBlockState(event.getPos());

        if (state.getBlock() != Blocks.WALL_SIGN && state.getBlock() != Blocks.STANDING_SIGN) {
            return;
        }
        if(ShopSign.shopSigns.isEmpty()){
            return;
        }

        EntityPlayer player = event.getPlayer();
        TileEntitySign potentialShopSign = (TileEntitySign) world.getTileEntity(event.getPos());

        if(ShopSign.isRegistered(potentialShopSign.getPos())){
            if (!FleaMarket.isOpped(player.getGameProfile())){
                event.setCanceled(true);
            }else{
                int shopSignIdx = ShopSign.findIndexFromBlockPos(potentialShopSign.getPos());
                if(shopSignIdx != -1){
                    ShopSign.shopSigns.remove(shopSignIdx);
                    ShopSignParser.saveShopSignsData();
                    player.sendMessage(new TextComponentString("Removed registered Shop sign"));
                }
            }
        }
    }
}

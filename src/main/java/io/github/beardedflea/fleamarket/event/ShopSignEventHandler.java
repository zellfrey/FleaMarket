package io.github.beardedflea.fleamarket.event;

import io.github.beardedflea.fleamarket.FleaMarket;
import io.github.beardedflea.fleamarket.store.ItemOfferList;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketSignEditorOpen;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

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

        TileEntitySign shopSign = (TileEntitySign) world.getTileEntity(event.getPos());
        int fmSignID = world.loadedTileEntityList.indexOf(shopSign);

        if(shopSign.signText[0].getUnformattedText().equals("[FLEAMARKET]")) {
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
        if(!FleaMarket.isOpped(player.getGameProfile())){
            return;
        }

        TileEntitySign shopSign = (TileEntitySign) world.getTileEntity(event.getPos());

        if(event.getEntityPlayer().isSneaking() && shopSign.signText[0].getUnformattedText().equals("[FLEAMARKET]")) {

            event.getEntityPlayer().sendMessage(new TextComponentString("Registering Sign"));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void shouldSignBreak(BlockEvent.BreakEvent event) {
        World world = event.getWorld();
        IBlockState state = world.getBlockState(event.getPos());

        if (state.getBlock() != Blocks.WALL_SIGN && state.getBlock() != Blocks.STANDING_SIGN) {
            return;
        }

        EntityPlayer player = event.getPlayer();
        TileEntitySign shopSign = (TileEntitySign) world.getTileEntity(event.getPos());
        int fmSignID = world.loadedTileEntityList.indexOf(shopSign);
        if (!FleaMarket.isOpped(player.getGameProfile()) && shopSign.signText[0].getUnformattedText().equals("[FLEAMARKET]")) {

            event.setCanceled(true);
        }
    }
}

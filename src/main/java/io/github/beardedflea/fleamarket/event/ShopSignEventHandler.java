package io.github.beardedflea.fleamarket.event;

import io.github.beardedflea.fleamarket.FleaMarket;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
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

    @SubscribeEvent
    public static void onSignInteract(PlayerInteractEvent.RightClickBlock event){
        World world = event.getWorld();
        Block targetBlock = world.getBlockState(event.getPos()).getBlock();

        if(targetBlock.getLocalizedName().equals("Sign")){
            EntityPlayer player = event.getEntityPlayer();
            TileEntitySign shopSign = (TileEntitySign) world.getTileEntity(event.getPos());
            if(shopSign.signText[0].getUnformattedText().equals("[FleaMarket]")) {
                for (ITextComponent line : shopSign.signText) {
                    player.sendMessage(line);
                }
                player.sendMessage(new TextComponentString(TextFormatting.GOLD + "You have sold those items!"));
            }
        }
    }
}

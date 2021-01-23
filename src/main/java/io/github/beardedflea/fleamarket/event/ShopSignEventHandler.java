package io.github.beardedflea.fleamarket.event;

import io.github.beardedflea.fleamarket.FleaMarket;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.SERVER, modid = FleaMarket.MODID)
public class ShopSignEventHandler {

    //    @SubscribeEvent(priority = FleaMarket.config.signShopEnabled ? EventPriority.LOW : null)
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

package io.github.beardedflea.fleamarket.store;


import io.github.beardedflea.fleamarket.FleaMarket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class ShopSign {

    private int signID, dimID, posX, posY, posZ;
    private String creator;

    public static ArrayList<ShopSign> shopSigns = new ArrayList<>();

    public ShopSign(String playerName, int signID, int dimID, BlockPos signPos){
        this.creator = playerName;
        this.signID = signID;
        this.dimID = dimID;
        this.posX = signPos.getX();
        this.posY = signPos.getY();
        this.posZ = signPos.getZ();
    }

    public static void registerShopSign(EntityPlayer player, int newSignID, int dimID, BlockPos signPos){

        if(!FleaMarket.isOpped(player.getGameProfile())){
            String onlyOps = "Only Opped players are able to register flea market signs!";
            player.sendMessage(new TextComponentString(TextFormatting.RED + onlyOps));
            return;
        }
        boolean alreadyRegistered = false;
        for(ShopSign fmSign : shopSigns){
            if(fmSign.signID == newSignID){
                alreadyRegistered = true;
                break;
            }
        }
        if(!alreadyRegistered){
            player.sendMessage(new TextComponentString(TextFormatting.GREEN + "Sign registered"));
            shopSigns.add(new ShopSign(player.getName(), newSignID, dimID, signPos));
            ShopSign newSign = shopSigns.get(shopSigns.size()-1);
            FleaMarket.getLogger().info("New flea market shop sign registered by {}", newSign.creator);
            String signParamsString = "SignID:{}, DIM:{}, PosX:{}, PosY:{}, PosZ:{}";
            FleaMarket.getLogger().info(signParamsString , newSign.signID, newSign.dimID, newSign.posX, newSign.posY, newSign.posZ);
        }
        else{
            player.sendMessage(new TextComponentString("Shop sign has already been registered"));
        }
    }

    private SPacketUpdateTileEntity setShopSign(TileEntitySign sign, @Nullable ItemOffer item){
        sign.signText[0] = new TextComponentString(TextFormatting.DARK_PURPLE + "[FLEAMARKET]");

        if(item != null){
            sign.signText[1] = new TextComponentString("Buying: " + item.getItemAmount());
            sign.signText[2] = new TextComponentString(item.getDisplayName());
            sign.signText[3] = new TextComponentString("123456789012345");
            //Sign can have a max of 15 characters on a line
            FleaMarket.getLogger().info(item.getDisplayName().length());
        }else{
            sign.signText[1] = new TextComponentString("Looking for");
            sign.signText[2] = new TextComponentString("another item");
            sign.signText[3] = new TextComponentString("come back later");
        }
        sign.markDirty();
        return sign.getUpdatePacket();
    }

    public static void updateShopSigns(){
        if(!FleaMarket.config.shopSignEnabled() || shopSigns.isEmpty()){
            return;
        }
        MinecraftServer server =  FMLCommonHandler.instance().getMinecraftServerInstance();
        World world = server.getEntityWorld();

        for(ShopSign sign : shopSigns){
            TileEntitySign registeredSign = (TileEntitySign)world.loadedTileEntityList.get(sign.signID);
            WorldServer worldServer = (WorldServer) registeredSign.getWorld();
            int chunkX = registeredSign.getPos().getX() >> 4;
            int chunkZ = registeredSign.getPos().getZ() >> 4;
            SPacketUpdateTileEntity updateTileEntity = sign.setShopSign(registeredSign, ItemOfferList.currentItemOffer);
            for (EntityPlayer entityPlayer : registeredSign.getWorld().playerEntities) {
                EntityPlayerMP entityPlayerMP = (EntityPlayerMP) entityPlayer;
                if (worldServer.getPlayerChunkMap().isPlayerWatchingChunk(entityPlayerMP, chunkX, chunkZ)) {
                    entityPlayerMP.connection.sendPacket(updateTileEntity);
                }
            }
        }
    }

    public static boolean isRegistered(int potentialSign){
        for(ShopSign fmSign : shopSigns){
            if(fmSign.signID == potentialSign){
                return true;
            }
        }
        return false;
    }

    public static int findIndexFromSignID(int potentialSignID){
        for(ShopSign fmSign : shopSigns){
            if(fmSign.signID == potentialSignID){
                return shopSigns.indexOf(fmSign);
            }
        }
        return -1;
    }

    public int getSignID() { return this.signID; }

    public String getCreator() { return this.creator; }
}

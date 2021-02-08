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
            SPacketUpdateTileEntity updateTileEntity = sign.setShopSign(registeredSign);
            for (EntityPlayer entityPlayer : registeredSign.getWorld().playerEntities) {
                EntityPlayerMP entityPlayerMP = (EntityPlayerMP) entityPlayer;
                if (worldServer.getPlayerChunkMap().isPlayerWatchingChunk(entityPlayerMP, chunkX, chunkZ)) {
                    entityPlayerMP.connection.sendPacket(updateTileEntity);
                }
            }
        }
    }

    private SPacketUpdateTileEntity setShopSign(TileEntitySign sign){
        ItemOffer item = ItemOfferList.currentItemOffer;
        sign.signText[0] = new TextComponentString(TextFormatting.DARK_PURPLE + "[FLEAMARKET]");

        if(ItemOfferList.itemOfferUptime != 0 && item != null){
            String[] itemSignNameArray = {"",""};
            sign.signText[1] = new TextComponentString("Buying: " + item.getItemAmount());
            transformNameForSign(itemSignNameArray, item.getDisplayName());
            sign.signText[2] = new TextComponentString(itemSignNameArray[0]);
            sign.signText[3] = new TextComponentString(itemSignNameArray[1]);
        }else{
            sign.signText[1] = new TextComponentString("Looking for");
            sign.signText[2] = new TextComponentString("another item");
            sign.signText[3] = new TextComponentString("come back later");
        }
        sign.markDirty();
        return sign.getUpdatePacket();
    }

    private void transformNameForSign(String[] itemSignNameArray, String itemName){
        if(itemName.length() > 15){
            StringBuilder lineOneText = new StringBuilder(15);
            StringBuilder lineTwoText = new StringBuilder(15);
            String[] nameArray = itemName.split(" ");
            for(String word : nameArray){
                if((lineOneText.length() < 15) && ((lineOneText.length()-1) + word.length() < 15)){
                    lineOneText.append(word).append(" ");
                }else{
                    lineTwoText.append(word).append(" ");
                }
            }
            itemSignNameArray[0] = lineOneText.toString().trim();
            itemSignNameArray[1] = lineTwoText.toString().trim();
        }else{
            itemSignNameArray[0] = itemName;
            itemSignNameArray[1] = "";
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

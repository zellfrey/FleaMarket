package io.github.beardedflea.fleamarket.store;


import io.github.beardedflea.fleamarket.FleaMarket;
import io.github.beardedflea.fleamarket.config.ShopSignParser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.ArrayList;

public class ShopSign {

    private int signID, dimID;
    private BlockPos pos;
    private String creator;

    public static ArrayList<ShopSign> shopSigns = new ArrayList<>();

    public ShopSign(String playerName, int signID, int dimID, BlockPos signPos){
        this.creator = playerName;
        this.signID = signID;
        this.dimID = dimID;
        this.pos = signPos;
    }

    public static void registerShopSign(EntityPlayer player, int newSignID, int dimID, BlockPos signPos){

        if(!FleaMarket.isOpped(player.getGameProfile())){
            String onlyOps = "Only Opped players are able to register flea market signs!";
            player.sendMessage(new TextComponentString(TextFormatting.RED + onlyOps));
            return;
        }

        if(!isRegistered(newSignID)){
            ShopSign newSign = new ShopSign(player.getName(), newSignID, dimID, signPos);
            shopSigns.add(newSign);
            ShopSignParser.saveShopSignsData();
            player.sendMessage(new TextComponentString(TextFormatting.GREEN + "Sign registered"));
            FleaMarket.getLogger().info("New flea market shop sign registered by {}", newSign.creator);
            FleaMarket.getLogger().info("SignID:{}, {}" , newSign.signID, newSign.pos.toString());
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

        for(ShopSign sign : shopSigns){
            World dim = server.getWorld(sign.dimID);
            TileEntitySign registeredSign = (TileEntitySign)dim.getTileEntity(sign.pos);
//            WorldServer worldServer = (WorldServer) registeredSign.getWorld();
//            int chunkX = registeredSign.getPos().getX() >> 4;
//            int chunkZ = registeredSign.getPos().getZ() >> 4;
            if(registeredSign != null){
                SPacketUpdateTileEntity updateTileEntity = sign.setShopSign(registeredSign);

                for (EntityPlayer entityPlayer : registeredSign.getWorld().playerEntities) {
                    EntityPlayerMP entityPlayerMP = (EntityPlayerMP) entityPlayer;
                    entityPlayerMP.connection.sendPacket(updateTileEntity);
//                if (worldServer.getPlayerChunkMap().isPlayerWatchingChunk(entityPlayerMP, chunkX, chunkZ)) {
//                    entityPlayerMP.connection.sendPacket(updateTileEntity);
//                }
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

    public int getDimID() { return this.dimID; }

    public BlockPos getPos() { return this.pos; }

    public String getCreator() { return this.creator; }
}

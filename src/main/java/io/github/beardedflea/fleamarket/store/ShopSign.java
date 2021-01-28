package io.github.beardedflea.fleamarket.store;


import io.github.beardedflea.fleamarket.FleaMarket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.LinkedList;

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

package io.github.beardedflea.fleamarket.command;

import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import io.github.beardedflea.fleamarket.FleaMarket;
import io.github.beardedflea.fleamarket.utils.*;

public class CommandFleaMarket extends CommandBase{


    @Override
    public String getName() {
        return "fm";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/fm [help:check:sell]";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    public static ITextComponent getHelpUsage(){
        ITextComponent comp1 = TranslationUtils.getTextBorder();
        ITextComponent comp2 = new TextComponentString("\n/fm help - what you are currently looking at\n");
        ITextComponent comp3 = new TextComponentString("/fm check - checks the current item on offer\n");
        ITextComponent comp4 = new TextComponentString("/fm sell - Current method of selling items\n");
        ITextComponent comp5 = TranslationUtils.getTextBorder();

        comp1.appendSibling(comp2).appendSibling(comp3).appendSibling(comp4).appendSibling(comp5);
        return comp1;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP playerMP = getCommandSenderAsPlayer(sender);
        
        if(args.length > 1){
            throw new SyntaxErrorException("Too many arguments");
        }
        if(args.length == 1){
            switch(args[0].toLowerCase()){
                case "help":
                    playerMP.sendMessage(getHelpUsage());
                break;

                case "check":
                    playerMP.sendMessage(new TextComponentString("checks the current item on offer"));
                break;

                case "sell":
                    playerMP.sendMessage(new TextComponentString("Current method of selling item"));

                    sellItemOffer(sender, playerMP.inventory.mainInventory);
                break;

                default:
                throw new WrongUsageException(getUsage(playerMP));
            }

        }
        else{
            throw new WrongUsageException(getUsage(playerMP));
        }
    }



    private static void sellItemOffer(ICommandSender sender, NonNullList<ItemStack> playerInventory){
        String currentOfferTest = "minecraft:potion";
        String currentOfferTestNBTStr = "{Potion:\"minecraft:long_night_vision\"}";
        boolean containsItemOffer = false;
        int amountOfCorrectItem = 0;
            //scans inventory checking for current ItemOffer
            for(ItemStack item : playerInventory){

                String itemFullName = item.getItem().getRegistryName()+ "";
                int itemDamageNum = item.getItem().getDamage(item);
                itemFullName += itemDamageNum != 0 ? ":"+ itemDamageNum : "";

                String itemNBTRaw = item.getItem().getNBTShareTag(item) + "";

                //Get name displayed on item e.g "Potion of Night Vision"
                FleaMarket.getLogger().info(item.getItem().getItemStackDisplayName(item));
                FleaMarket.getLogger().info(itemFullName);
                FleaMarket.getLogger().info(itemNBTRaw);

                if(itemFullName.equals(currentOfferTest) && itemNBTRaw.equals(currentOfferTestNBTStr)){
                    containsItemOffer = true;
                    amountOfCorrectItem +=item.getCount();
                }
            }
            if(containsItemOffer){
                sender.sendMessage(new TextComponentString(TextFormatting.GOLD + "You have " + amountOfCorrectItem + " " + currentOfferTest));
            }
            else{
                sender.sendMessage(new TextComponentString(TextFormatting.RED + "You do not have the required item on offer"));
            }
    }
}
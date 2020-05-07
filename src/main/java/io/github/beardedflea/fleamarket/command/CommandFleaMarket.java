package io.github.beardedflea.fleamarket.command;

import io.github.beardedflea.fleamarket.FleaMarket;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.*;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

import io.github.beardedflea.fleamarket.store.*;
import io.github.beardedflea.fleamarket.utils.TextUtils;
import io.github.beardedflea.fleamarket.config.FleaMarketConfig;

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

    private static ITextComponent getHelpUsage(){
        ITextComponent comp1 = TextUtils.getTextBorder();
        ITextComponent comp2 = new TextComponentString("\n/fm help - what you are currently looking at\n");
        ITextComponent comp3 = new TextComponentString("/fm check - checks the current item on offer\n");
        ITextComponent comp4 = new TextComponentString("/fm sell - Current method of selling items\n");
        ITextComponent comp5 = TextUtils.getTextBorder();

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
                    sender.sendMessage(getHelpUsage());
                break;

                case "check":

                    if(ItemOfferList.currentItemOffer == null){
                        sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "There is no item offer available at this time"));
                    }
                    else{
                        sender.sendMessage(new TextComponentString(ItemOfferList.currentItemOffer.getBroadcastMsg()));
                    }

                break;

                case "sell":

                    if(ItemOfferList.currentItemOffer == null){
                        playerMP.sendMessage(new TextComponentString(TextFormatting.GREEN + "There is no item offer available at this time"));
                    }
                    else{
//                      sellItemOffer(playerMP);
                        playerMP.sendMessage(new TextComponentString("Selling item"));
                    }

                break;

                default:
                throw new WrongUsageException(getUsage(sender));
            }

        }
        else{
            throw new WrongUsageException(getUsage(sender));
        }
    }



    private static void sellItemOffer(EntityPlayerMP playerMP){
        boolean containsItemOffer = false;
        int amountOfCorrectItem = 0;
        Item itemToRemove = null;
        //scans inventory checking for current ItemOffer
        for(ItemStack item : playerMP.inventory.mainInventory) {

            String itemFullName = item.getItem().getRegistryName() + "";
            int itemDamageNum = item.getItem().getDamage(item);

            //Stitches the registry name with the meta data id: modName:blockName:dmgVal
            itemFullName += itemDamageNum != 0 ? ":" + itemDamageNum : "";

            String itemNBTRaw = item.getItem().getNBTShareTag(item) + "";
            //Get name displayed on item e.g "Potion of Night Vision"
//            if(FleaMarketConfig.debugMode){DebugUtils.printDebugStrConsole(item.getItem().getItemStackDisplayName(item), itemFullName, itemNBTRaw);}

//            if(itemFullName.equals(ItemOffer.currentItemName)) {
////                && itemNBTRaw.equals(ItemOffer.currentItemNBTRaw)
//                containsItemOffer = true;
//                amountOfCorrectItem += item.getCount();
//                itemToRemove = item.getItem();
//                FleaMarket.getLogger().info(itemToRemove);
//            }
        }
            //checks if item is inventory
        if(containsItemOffer){
            //final check to see if the amount is equal or greater to the desired amount in the offer
//            if(amountOfCorrectItem >= ItemOffer.currentItemAmount){
//                int k = playerMP.inventory.clearMatchingItems(itemToRemove, 0,3,  null);
//                playerMP.inventoryContainer.detectAndSendChanges();
//                FleaMarket.getLogger().info("Removed " + k + "  items from " + playerMP.getName() + "'s inventory");
//
//            }
//            else{
//                playerMP.sendMessage(new TextComponentString("Flea Market wants " + ItemOffer.currentItemAmount + " " + ItemOffer.currentItemName));
//                playerMP.sendMessage(new TextComponentString("You only have " + amountOfCorrectItem + " in your inventory."));
//            }

        }
        else{
            playerMP.sendMessage(new TextComponentString(TextFormatting.RED + "You do not have the required item on offer"));
        }
    }
}
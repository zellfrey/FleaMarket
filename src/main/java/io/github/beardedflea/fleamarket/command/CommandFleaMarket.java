package io.github.beardedflea.fleamarket.command;

import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.*;
import net.minecraft.server.MinecraftServer;

import io.github.beardedflea.fleamarket.store.*;
import io.github.beardedflea.fleamarket.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class CommandFleaMarket extends CommandBase{


    @Override
    public String getName() {
        return "fleamarket";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/fleamarket [help:check:sell]";
    }


    @Override
    public List<String> getAliases()
    {
        ArrayList<String> aliases = new ArrayList<>();
        aliases.add("fm");
        aliases.add("fleamkt");
        return aliases;
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
        ITextComponent comp2 = new TextComponentString("\n/fm help - Shows a list of commands for players\n");
        ITextComponent comp3 = new TextComponentString("/fm check - checks the current item on offer\n");
        ITextComponent comp4 = new TextComponentString("/fm sell - Current method of selling items\n");
        ITextComponent comp5 = TextUtils.getTextBorder();

        comp1.appendSibling(comp2).appendSibling(comp3).appendSibling(comp4).appendSibling(comp5);

        return comp1;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        
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
                    else if(ItemOfferList.itemOfferUptime <= 0){
                        sender.sendMessage(new TextComponentString(TextFormatting.BLUE + "Flea market is finding another item to offer"));
                    }
                    else{
                        sender.sendMessage(new TextComponentString(TextFormatting.AQUA + ItemOfferList.currentItemOffer.getBroadcastMsg()));
                    }
                break;

                case "sell":

                    EntityPlayerMP playerMP = getCommandSenderAsPlayer(sender);
                    if(ItemOfferList.currentItemOffer == null){
                        playerMP.sendMessage(new TextComponentString(TextFormatting.GREEN + "There is no item offer available at this time"));
                    }
                    else if(ItemOfferList.itemOfferUptime <= 0){
                        playerMP.sendMessage(new TextComponentString(TextFormatting.BLUE + "Flea market is finding another item to offer"));
                    }
                    else{
                      ItemOfferList.sellItemOffer(playerMP);
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

}
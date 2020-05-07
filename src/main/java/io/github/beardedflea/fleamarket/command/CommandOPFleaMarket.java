package io.github.beardedflea.fleamarket.command;

import net.minecraft.command.*;
import net.minecraft.util.text.*;
import net.minecraft.server.MinecraftServer;
import io.github.beardedflea.fleamarket.FleaMarket;
import io.github.beardedflea.fleamarket.utils.*;
import io.github.beardedflea.fleamarket.config.ItemOfferParser;
import io.github.beardedflea.fleamarket.store.ItemOfferList;

public class CommandOPFleaMarket extends CommandBase{


    @Override
    public String getName() {
        return "opfm";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/opfm [help:start:pause:skip]";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    public static long getServerTime(){
        return MinecraftServer.getCurrentTimeMillis();
    }

    private static ITextComponent getOPHelpUsage(){
        ITextComponent comp1 = TextUtils.getTextBorder();
        ITextComponent comp2 = new TextComponentString("\n/opfm help - what you are currently looking at\n");
        ITextComponent comp3 = new TextComponentString("/opfm start - starts the cycle of item offers\n");
        ITextComponent comp4 = new TextComponentString("/opfm pause - Pauses the cycle. Players can still offer the item\n");
        ITextComponent comp5 = new TextComponentString("/opfm skip - Moves to the next item on the list\n");
        ITextComponent comp6 = new TextComponentString("/opfm reload - Reloads ItemOffers folder\n");
        ITextComponent comp7 = TextUtils.getTextBorder();

        comp1.appendSibling(comp2).appendSibling(comp3).appendSibling(comp4).appendSibling(comp5).appendSibling(comp6).appendSibling(comp7);
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
                    sender.sendMessage(getOPHelpUsage());
                break;

                case "start":

                    boolean startedCycle = ItemOfferList.startItemOfferCycle(server);

                    if(!startedCycle){
                        sender.sendMessage(new TextComponentString(TextFormatting.GOLD + "ItemOffers cycle has already started"));
                    }
                    else{
                        sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Starting cycle of itemOffers"));
                    }

                break;

                case "pause":
                    sender.sendMessage(new TextComponentString("Pauses the cycle. Players can still offer the item"));
                break;

                case "skip":
                    sender.sendMessage(new TextComponentString("Moves to the next item offer in the list"));
                break;

                case "reload":
                    reloadItemOfferData(sender);
                break;
                default:
                throw new WrongUsageException(getUsage(sender));
            }

        }else{
            throw new WrongUsageException(getUsage(sender));
        }
    }

    private static void reloadItemOfferData(ICommandSender sender){
        sender.sendMessage(new TextComponentString(TextFormatting.BLUE + "Reloading FleaMarket ItemOffers"));

        ItemOfferParser.loadItemOfferData();
        int itemOfferCount = ItemOfferList.getItemOfferSize();
        int fileCount = ItemOfferParser.configDir.listFiles().length;
        if(itemOfferCount == 0){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Found 0 ItemOffers!"));
        }
        if(fileCount == 0){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Found 0 ItemOffers files!"));
        }
        sender.sendMessage(new TextComponentString(TextFormatting.BLUE + "FleaMarket registered a total of " + itemOfferCount + " ItemOffers in " + fileCount + " files!"));
        sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Finished reloading"));
    }
}
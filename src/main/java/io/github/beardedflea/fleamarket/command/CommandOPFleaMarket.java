package io.github.beardedflea.fleamarket.command;

import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.*;
// import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import io.github.beardedflea.fleamarket.FleaMarket;
import io.github.beardedflea.fleamarket.utils.*;

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

    public static ITextComponent getOPHelpUsage(){
        ITextComponent comp1 = TranslationUtils.getTextBorder();
        ITextComponent comp2 = new TextComponentString("\n/opfm help - what you are currently looking at\n");
        ITextComponent comp3 = new TextComponentString("/opfm start - starts the cycle of item offers\n");
        ITextComponent comp4 = new TextComponentString("/opfm pause - Pauses the cycle. Players can still offer the item\n");
        ITextComponent comp5 = new TextComponentString("/opfm skip - Moves to the next item on the list\n");
        ITextComponent comp6 = TranslationUtils.getTextBorder();

        comp1.appendSibling(comp2).appendSibling(comp3).appendSibling(comp4).appendSibling(comp5).appendSibling(comp6);
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
                    sender.sendMessage(new TextComponentString("starts automatic cycling of item offers"));
                    sender.sendMessage(new TextComponentString(sender.getName()));
                break;

                case "pause":
                    sender.sendMessage(new TextComponentString("Pauses the cycle. Players can still offer the item"));
                break;

                case "skip":
                    sender.sendMessage(new TextComponentString("Moves to the next item offer in the list"));
                break;

                default:
                throw new WrongUsageException(getUsage(sender));
            }

        }else{
            throw new WrongUsageException(getUsage(sender));
        }
    }
}
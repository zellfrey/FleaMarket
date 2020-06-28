package io.github.beardedflea.fleamarket.command;

import net.minecraft.command.*;
import net.minecraft.util.text.*;
import net.minecraft.server.MinecraftServer;

import static io.github.beardedflea.fleamarket.utils.TextUtils.*;
import io.github.beardedflea.fleamarket.config.ItemOfferParser;
import io.github.beardedflea.fleamarket.store.ItemOfferList;
import io.github.beardedflea.fleamarket.config.LanguageParser;

import java.util.ArrayList;
import java.util.List;

public class CommandOPFleaMarket extends CommandBase{


    @Override
    public String getName() {
        return "opfleamarket";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/opfleamarket [help:start:pause:skip:reload]";
    }

    @Override
    public List<String> getAliases()
    {
        ArrayList<String> aliases = new ArrayList<>();
        aliases.add("opfm");
        aliases.add("opfleamkt");
        return aliases;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }


    private static ITextComponent getOPHelpUsage(){
        ITextComponent comp1 = getModTextBorder();
        ITextComponent comp2 = new TextComponentString(modLanguageMap.get("opfmhelp"));
        ITextComponent comp3 = new TextComponentString(modLanguageMap.get("opfmstart"));
        ITextComponent comp4 = new TextComponentString(modLanguageMap.get("opfmpause"));
        ITextComponent comp5 = new TextComponentString(modLanguageMap.get("opfmskip"));
        ITextComponent comp6 = new TextComponentString(modLanguageMap.get("opfmreload"));
        ITextComponent comp7 = getModTextBorder();

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
                        sender.sendMessage(new TextComponentString(modLanguageMap.get("cycleInProgress")));
                    }
                    else{
                        sender.sendMessage(new TextComponentString(modLanguageMap.get("cycleStart")));
                    }
                break;

                case "pause":
                    ItemOfferList.pauseCycle = !ItemOfferList.pauseCycle;
                    sender.sendMessage(new TextComponentString(TransformModLanguage("&7ItemOffer paused: " + ItemOfferList.pauseCycle)));
                break;

                case "skip":
                    sender.sendMessage(new TextComponentString(modLanguageMap.get("skipItemOffer")));
                    ItemOfferList.setCurrentItemOffer(server);
                break;

                case "reload":
                    sender.sendMessage(new TextComponentString(modLanguageMap.get("reloadMod")));
                    LanguageParser.loadModLanguage();
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
        ItemOfferList.clearFairRandomArray();
        ItemOfferParser.loadItemOfferData();
        int itemOfferCount = ItemOfferList.getItemOfferSize();
        int fileCount = ItemOfferParser.configDir.listFiles().length;

        if(itemOfferCount == 0){
            sender.sendMessage(new TextComponentString(modLanguageMap.get("noItemsFound")));
        }

        if(fileCount == 0){
            sender.sendMessage(new TextComponentString(modLanguageMap.get("noFilesFound")));
        }

        sender.sendMessage(new TextComponentString(TransformModLanguage("&9FleaMarket registered a total of " + itemOfferCount +
                " ItemOffers in " + fileCount + " files!")));

        sender.sendMessage(new TextComponentString(modLanguageMap.get("reloadFinished")));
    }
}
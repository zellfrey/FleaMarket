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
        ITextComponent comp2 = TransformModLanguage(modLanguageMap.get("opfmhelp"));
        ITextComponent comp3 = TransformModLanguage(modLanguageMap.get("opfmstart"));
        ITextComponent comp4 = TransformModLanguage(modLanguageMap.get("opfmpause"));
        ITextComponent comp5 = TransformModLanguage(modLanguageMap.get("opfmskip"));
        ITextComponent comp6 = TransformModLanguage(modLanguageMap.get("opfmreload"));
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
                        sender.sendMessage(TransformModLanguage("&4ItemOffers cycle has already started"));
                    }
                    else{
                        sender.sendMessage(TransformModLanguage("&aStarting cycle of itemOffers"));
                    }
                break;

                case "pause":
                    ItemOfferList.pauseCycle = !ItemOfferList.pauseCycle;
//                    sender.sendMessage(new TextComponentString("ItemOffer paused: " + ItemOfferList.pauseCycle));
                    sender.sendMessage(TransformModLanguage("ItemOffer paused: " + ItemOfferList.pauseCycle));
                break;

                case "skip":
                    sender.sendMessage(TransformModLanguage("Moving to next time offer"));
                    ItemOfferList.setCurrentItemOffer(server);
                break;

                case "reload":
                    sender.sendMessage(TransformModLanguage("&8Reloading lang.yml..."));
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
        sender.sendMessage(TransformModLanguage( "&8Reloading FleaMarket ItemOffers"));

        ItemOfferParser.loadItemOfferData();
        int itemOfferCount = ItemOfferList.getItemOfferSize();
        int fileCount = ItemOfferParser.configDir.listFiles().length;

        if(itemOfferCount == 0){
            sender.sendMessage(TransformModLanguage( "&c&lFound 0 ItemOffers!"));
        }

        if(fileCount == 0){
            sender.sendMessage(TransformModLanguage( "&c&lFound 0 ItemOffer files!"));
        }

        sender.sendMessage(new TextComponentString(TextFormatting.BLUE + "FleaMarket registered a total of " +
                                                    itemOfferCount + " ItemOffers in " + fileCount + " files!"));

        sender.sendMessage(TransformModLanguage("&2Finished reloading"));
    }
}
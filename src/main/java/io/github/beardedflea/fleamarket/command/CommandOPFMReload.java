package io.github.beardedflea.fleamarket.command;

import io.github.beardedflea.fleamarket.FleaMarket;
import io.github.beardedflea.fleamarket.event.ShopSignEventHandler;
import io.github.beardedflea.fleamarket.store.ItemOfferList;
import io.github.beardedflea.fleamarket.config.ItemOfferParser;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import static io.github.beardedflea.fleamarket.utils.TextUtils.*;

public class CommandOPFMReload extends CommandBase{

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return modLanguageMap.get("opfmreload");
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) { return true; }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args){

        sender.sendMessage(new TextComponentString(modLanguageMap.get("reloadMod")));
        FleaMarket.instance.loadConfig();

        if(!FleaMarket.config.shopSignEnabled()){
            ShopSignEventHandler.shutdown();
        }else{
            ShopSignEventHandler.init();
        }

        loadTextUtils(FleaMarket.config.messagesConfigMap());
        reloadItemOfferData(sender);
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

        sender.sendMessage(new TextComponentString(TransformModLanguageConfig(
                "&9FleaMarket registered a total of " + itemOfferCount + " ItemOffers in " + fileCount + " files!")));

        sender.sendMessage(new TextComponentString(modLanguageMap.get("reloadFinished")));
    }
}

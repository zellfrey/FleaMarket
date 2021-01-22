package io.github.beardedflea.fleamarket.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import io.github.beardedflea.fleamarket.store.ItemOfferList;

import static io.github.beardedflea.fleamarket.utils.TextUtils.modLanguageMap;

public class CommandFMCheck extends CommandBase{

    @Override
    public String getName() {
        return "check";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return modLanguageMap.get("fmcheck");
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) { return true; }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args){
        if(ItemOfferList.checkItemOffer(sender)){
            sender.sendMessage(new TextComponentString(ItemOfferList.currentItemOffer.getBroadcastMsg()));
        }
    }
}

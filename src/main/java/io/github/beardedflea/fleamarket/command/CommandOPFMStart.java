package io.github.beardedflea.fleamarket.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import io.github.beardedflea.fleamarket.store.ItemOfferList;

import static io.github.beardedflea.fleamarket.utils.TextUtils.modLanguageMap;

public class CommandOPFMStart extends CommandBase{

    @Override
    public String getName() {
        return "start";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return modLanguageMap.get("opfmstart");
    }

    @Override
    public int getRequiredPermissionLevel() { return 4; }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) { return true; }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args){

        if(!ItemOfferList.startItemOfferCycle(server)){
            sender.sendMessage(new TextComponentString(modLanguageMap.get("cycleInProgress")));
        }
        else{
            sender.sendMessage(new TextComponentString(modLanguageMap.get("cycleStart")));
        }
    }
}

package io.github.beardedflea.fleamarket.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import io.github.beardedflea.fleamarket.store.ItemOfferList;

import static io.github.beardedflea.fleamarket.utils.TextUtils.*;

public class CommandOPFMPause extends CommandBase{

    @Override
    public String getName() {
        return "pause";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return modLanguageMap.get("opfmpause");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args){

        ItemOfferList.pauseCycle = !ItemOfferList.pauseCycle;
        String pauseCycleMsg = TransformModLanguageConfig("&7ItemOffer paused: " + ItemOfferList.pauseCycle);
        sender.sendMessage(new TextComponentString(pauseCycleMsg));
    }
}

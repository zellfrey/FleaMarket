package io.github.beardedflea.fleamarket.command;

import io.github.beardedflea.fleamarket.store.ShopSign;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import io.github.beardedflea.fleamarket.store.ItemOfferList;

import static io.github.beardedflea.fleamarket.utils.TextUtils.modLanguageMap;

public class CommandOPFMSkip extends CommandBase{

    @Override
    public String getName() { return "skip"; }

    @Override
    public String getUsage(ICommandSender sender) { return modLanguageMap.get("opfmskip"); }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args){

        sender.sendMessage(new TextComponentString(modLanguageMap.get("skipItemOffer")));
        ItemOfferList.setCurrentItemOffer(server);
    }
}

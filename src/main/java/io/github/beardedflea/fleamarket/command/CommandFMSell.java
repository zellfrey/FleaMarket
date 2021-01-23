package io.github.beardedflea.fleamarket.command;

import io.github.beardedflea.fleamarket.FleaMarket;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.entity.player.EntityPlayerMP;

import io.github.beardedflea.fleamarket.store.ItemOfferList;
import net.minecraft.util.text.TextComponentString;

import static io.github.beardedflea.fleamarket.utils.TextUtils.modLanguageMap;

public class CommandFMSell extends CommandBase{

    @Override
    public String getName() {
        return "sell";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return modLanguageMap.get("fmsell");
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) { return true; }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args)throws CommandException{
        EntityPlayerMP playerMP = getCommandSenderAsPlayer(sender);

        if(!FleaMarket.config.shopSignEnabled()) {
            if (ItemOfferList.checkItemOffer(sender)) ItemOfferList.sellItemOffer(playerMP);
        }
        else{
            sender.sendMessage(new TextComponentString(modLanguageMap.get("shopSignMessage")));
        }
    }
}

package com.beardedflea.fleamarket.command;

import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
// import net.minecraft.util.text.*;
// import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import com.beardedflea.fleamarket.FleaMarket;

public class CommandFleaMarket extends CommandBase{

    public static long serverTime = MinecraftServer.getCurrentTimeMillis();

    @Override
    public String getName() {
        return "fm";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/fm test";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP playerMP = getCommandSenderAsPlayer(sender);
        
        if(args.length <= 0){
            throw new WrongUsageException(getUsage(sender));
        }
        else{
            FleaMarket.getLogger().info(serverTime);
        }
    }
}
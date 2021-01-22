package io.github.beardedflea.fleamarket.command;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.github.beardedflea.fleamarket.utils.TextUtils.*;

public class CommandFleaMarket extends CommandTreeBase {

    public CommandFleaMarket(){
        addSubcommand(new CommandFMCheck());
        addSubcommand(new CommandFMSell());
    }

    @Override
    public int getRequiredPermissionLevel() { return 0; }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public String getName() {
        return "fleamarket";
    }

    @Override
    public String getUsage(ICommandSender sender) { return "/fleamarket [help:check:sell]"; }

    @Override
    public List<String> getAliases() {
        ArrayList<String> aliases = new ArrayList<>();
        aliases.add("fm");
        aliases.add("fleamkt");
        return aliases;
    }

    private static ITextComponent getHelpUsage(){
        ITextComponent comp1 = getModTextBorder();
        ITextComponent comp2 = new TextComponentString("\n" + modLanguageMap.get("fmhelp") + "\n");
        ITextComponent comp3 = new TextComponentString(modLanguageMap.get("fmcheck") + "\n");
        ITextComponent comp4 = new TextComponentString(modLanguageMap.get("fmsell") + "\n");
        ITextComponent comp5 = getModTextBorder();

        comp1.appendSibling(comp2).appendSibling(comp3).appendSibling(comp4).appendSibling(comp5);
        return comp1;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if(args.length == 0 || args[0].toLowerCase().equals("help")) {
            sender.sendMessage(getHelpUsage());
        }
        else {
            try{
                super.execute(server, sender, args);
            }
            catch (CommandException e){
                throw new WrongUsageException(getUsage(sender));
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        String [] subCmdNames = new String[] {"help", "check", "sell"};
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, subCmdNames) : Collections.emptyList();
    }
}
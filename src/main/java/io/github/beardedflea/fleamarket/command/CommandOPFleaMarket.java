package io.github.beardedflea.fleamarket.command;


import net.minecraft.command.CommandException;
import net.minecraftforge.server.command.CommandTreeBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.github.beardedflea.fleamarket.utils.TextUtils.*;

public class CommandOPFleaMarket extends CommandTreeBase {

    public CommandOPFleaMarket() {
        addSubcommand(new CommandOPFMStart());
        addSubcommand(new CommandOPFMPause());
        addSubcommand(new CommandOPFMSkip());
        addSubcommand(new CommandOPFMReload());
    }

    @Override
    public int getRequiredPermissionLevel() { return 4; }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return sender.canUseCommand(getRequiredPermissionLevel(), this.getName());
    }

    @Override
    public String getName() {
        return "opfleamarket";
    }

    @Override
    public String getUsage(ICommandSender sender) { return "/opfleamarket [help:start:pause:skip:reload]"; }

    @Override
    public List<String> getAliases() {
        ArrayList<String> aliases = new ArrayList<>();
        aliases.add("opfm");
        aliases.add("opfleamkt");
        return aliases;
    }

    private static ITextComponent getOPHelpUsage() {
        ITextComponent comp1 = getModTextBorder();
        ITextComponent comp2 = new TextComponentString("\n" + modLanguageMap.get("opfmhelp") + "\n");
        ITextComponent comp3 = new TextComponentString(modLanguageMap.get("opfmstart") + "\n");
        ITextComponent comp4 = new TextComponentString(modLanguageMap.get("opfmpause") + "\n");
        ITextComponent comp5 = new TextComponentString(modLanguageMap.get("opfmskip") + "\n");
        ITextComponent comp6 = new TextComponentString(modLanguageMap.get("opfmreload") + "\n");
        ITextComponent comp7 = getModTextBorder();

        comp1.appendSibling(comp2).appendSibling(comp3).appendSibling(comp4).appendSibling(comp5).appendSibling(comp6).appendSibling(comp7);
        return comp1;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if(args.length == 0 || args[0].toLowerCase().equals("help")) {
            sender.sendMessage(getOPHelpUsage());
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
        String [] subCmdNames = new String[] {"help", "start", "pause", "skip", "reload"};
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, subCmdNames) : Collections.emptyList();
    }
}
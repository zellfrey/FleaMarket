package io.github.beardedflea.fleamarket.utils;

import io.github.beardedflea.fleamarket.FleaMarket;
import net.minecraft.util.text.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {

    public static HashMap<String,Object> modLanguageMap = new HashMap<>();

    private static final Pattern COLOUR_CODE_PATTERN = Pattern.compile("&[0-9A-flmnork]");

    private static HashMap<String, String> COLOUR_MAP = new HashMap <>();

    public static void populateColourMap(){
        COLOUR_MAP.put("&0", "\u00A70");
        COLOUR_MAP.put("&1", "\u00A71");
        COLOUR_MAP.put("&2", "\u00A72");
        COLOUR_MAP.put("&3", "\u00A73");
        COLOUR_MAP.put("&4", "\u00A74");
        COLOUR_MAP.put("&5", "\u00A75");
        COLOUR_MAP.put("&6", "\u00A76");
        COLOUR_MAP.put("&7", "\u00A77");
        COLOUR_MAP.put("&8", "\u00A78");
        COLOUR_MAP.put("&9", "\u00A79");
        COLOUR_MAP.put("&a", "\u00A7a");
        COLOUR_MAP.put("&b", "\u00A7b");
        COLOUR_MAP.put("&c", "\u00A7c");
        COLOUR_MAP.put("&d", "\u00A7d");
        COLOUR_MAP.put("&e", "\u00A7e");
        COLOUR_MAP.put("&f", "\u00A7f");
        COLOUR_MAP.put("&l", "\u00A7l");
        COLOUR_MAP.put("&m", "\u00A7m");
        COLOUR_MAP.put("&n", "\u00A7n");
        COLOUR_MAP.put("&o", "\u00A7o");
        COLOUR_MAP.put("&r", "\u00A7r");
        COLOUR_MAP.put("&k", "\u00A7k");
        // &K MAGIC             //   OBFUSCATED(null,' ',0),
        //Using bukkit colour code formatting with motd colour code
        //As seen here >>> https://minecraft.gamepedia.com/Formatting_codes
    }

    public static ITextComponent TransformModLanguage(Object yamlObj){

        ArrayList<String> colourCodes = new ArrayList<>();

        String inputString = yamlObj.toString();

        Matcher codeMatch = COLOUR_CODE_PATTERN.matcher(inputString);

        while (codeMatch.find()) {
            if(!colourCodes.contains(codeMatch.group())){
                colourCodes.add(codeMatch.group());
            }
        }
        if(colourCodes.size() != 0){

            for(String colour : colourCodes){
                inputString = inputString.replace(colour, COLOUR_MAP.get(colour));
            }
        }

        ITextComponent textLine = new TextComponentString(inputString);

        return textLine;
    }

    public static ITextComponent getModTextBorder(){
        ITextComponent borderLeft = new TextComponentString(TextFormatting.BLUE + "==================");
        ITextComponent modNameMid = new TextComponentString(TextFormatting.LIGHT_PURPLE + " " + FleaMarket.NAME + " ");
        ITextComponent borderRight = new TextComponentString(TextFormatting.BLUE + "==================");

        borderLeft.appendSibling(modNameMid).appendSibling(borderRight);
        //"================== Flea Market =================="
        return borderLeft;
    }

    //Will implement with version 1.1
//    public static ITextComponent getModTextTag(){
//        ITextComponent bracketLeft = new TextComponentString(TextFormatting.BLUE + "[");
//        ITextComponent bracketRight = new TextComponentString(TextFormatting.BLUE + "]");
//        ITextComponent modNameMid = new TextComponentString(TextFormatting.LIGHT_PURPLE + " " + FleaMarket.NAME + " ");
//        bracketLeft.appendSibling(modNameMid).appendSibling(bracketRight);
//        // [ Flea Market ]
//        return bracketLeft;
//    }

    public static void printDebugStrConsole(String... sArgs){
        for(String line : sArgs){
            FleaMarket.getLogger().info(line);
        }
    }

    public static String replacePlayerPlaceHolder(String line, String playerName){
        return line.replace("%playerName%", playerName);
    }

    public static String replaceItemAndAmount(String line, int itemAmount, String itemName){
        return line.replace("%item%", itemName).replace("%amount%", Integer.toString(itemAmount));
    }
}
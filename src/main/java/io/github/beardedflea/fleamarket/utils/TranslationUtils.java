package com.beardedflea.fleamarket.utils;

import net.minecraft.util.text.*;
import com.beardedflea.fleamarket.FleaMarket;

public class TranslationUtils {


    public static ITextComponent getTextBorder(){
        ITextComponent borderSide = new TextComponentString(TextFormatting.BLUE + "==================");
        ITextComponent modNameMid = new TextComponentString(TextFormatting.LIGHT_PURPLE + " " + FleaMarket.NAME + " ");

        borderSide.appendSibling(modNameMid).appendSibling(borderSide);
        //"================== Flea Market =================="
        return borderSide;
    }
}
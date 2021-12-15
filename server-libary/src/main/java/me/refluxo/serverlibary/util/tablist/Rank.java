package me.refluxo.serverlibary.util.tablist;

import net.minecraft.EnumChatFormat;

public interface Rank {

    String getPrefix();

    String getSuffix();

    int getTablistHeight();

    EnumChatFormat getColor();

}

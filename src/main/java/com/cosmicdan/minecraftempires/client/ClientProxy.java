package com.cosmicdan.minecraftempires.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.MinecraftForgeClient;

import com.cosmicdan.minecraftempires.CommonProxy;
import com.cosmicdan.minecraftempires.client.gui.GuiJournalBook;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
    
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }
    
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }
    
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
    
    @Override
    public void openJournal() {
        // client-only hook command for when user presses right-click on their Journal (called from the Journal item class)
        // remember - getMinecraft() only exists in the client!
        Minecraft.getMinecraft().displayGuiScreen(new GuiJournalBook());
    }
}
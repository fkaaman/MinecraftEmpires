package com.cosmicdan.minecraftempires;

import com.cosmicdan.minecraftempires.eventhandlers.BlockEvents;
import com.cosmicdan.minecraftempires.eventhandlers.EntityEvents;
import com.cosmicdan.minecraftempires.eventhandlers.WorldTickEvents;
import com.cosmicdan.minecraftempires.eventhandlers.WorldEvents;
import com.cosmicdan.minecraftempires.medata.player.EntityPlayerME;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.world.WorldEvent;

public class CommonProxy {
    
    public void preInit(FMLPreInitializationEvent event) {
        
    }
    
    
    public void init(FMLInitializationEvent event) {
        
    }
    
    
    public void postInit(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new EntityEvents());
        MinecraftForge.EVENT_BUS.register(new WorldEvents());
        FMLCommonHandler.instance().bus().register(new WorldTickEvents());
    }
    
    
    public void openJournal(EntityPlayer player) {
        //if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            // sync data whenever the player opens their log/journal/whatever
        //    EntityPlayerME.get((EntityPlayerMP) player).sync();
        //}
    }
    
    public EntityPlayer getPlayerFromMessageContext(MessageContext ctx) {
        switch(ctx.side) {
            case CLIENT:
                assert false : "Message for CLIENT received on dedicated server";
                break;
            case SERVER:
                EntityPlayer entityPlayerMP = ctx.getServerHandler().playerEntity;
                return entityPlayerMP;
            default:
                assert false : "Invalid side in TestMsgHandler: " + ctx.side;
        }
        return null;
    }
}
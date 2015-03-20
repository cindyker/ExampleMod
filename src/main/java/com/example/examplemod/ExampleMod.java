package com.example.examplemod;

import com.example.examplemod.commands.ExampleQuietCommand;
import com.example.examplemod.commands.ExampleShowCommand;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S36PacketSignEditorOpen;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@Mod(modid = ExampleMod.MODID, version = ExampleMod.VERSION)
public class ExampleMod
{
    public static final String MODID = "examplemod";
    public static final String VERSION = "1.0";

    public static ArrayList<String> quietList;
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		// some example code
        System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
        System.out.println("GRAVEL BLOCK >> " + Blocks.gravel.getUnlocalizedName());

        MinecraftForge.EVENT_BUS.register(this);

        quietList = new ArrayList<String>();
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new ExampleQuietCommand());
        event.registerServerCommand(new ExampleShowCommand());
    }


    //I'm a bit of test code and has no real purpose being here... :) But it works!
    @SubscribeEvent
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (!event.world.isRemote && event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && event.entityPlayer.getHeldItem() == null
                && event.entityPlayer.isSneaking()) {
            final TileEntity te = event.world.getTileEntity(event.pos);
            if (te != null && te instanceof TileEntitySign) {
                if (event.entityPlayer instanceof EntityPlayerMP) {
                    ((EntityPlayerMP) event.entityPlayer).playerNetServerHandler
                            .sendPacket(new S36PacketSignEditorOpen(te.getPos()));
                    addMessage("Helllllo SIGN!", (EntityPlayerMP) event.entityPlayer);
                }

            }
        }
    }

    @SubscribeEvent
    public void onPlayerChatEvent(ServerChatEvent event) {

        EntityPlayerMP sender = event.player;
        event.setCanceled(true);

        if(quietList.contains(sender.getUniqueID().toString())){
            addMessage("You have quieted chat. Use /show to see it again.",sender);
            return;
        }


        List<EntityPlayerMP> players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
        for (EntityPlayerMP receiver : players)
        {
            if(!quietList.contains(receiver.getUniqueID().toString()))
                addMessage( sender.getName() + " say '" + event.message, receiver);

        }

    }

    public static void addMessage(String msg,EntityPlayerMP ep) {
        ep.addChatMessage(new ChatComponentText("\2478[\247cMODCHAT\2478] \247f" + msg));
    }
}

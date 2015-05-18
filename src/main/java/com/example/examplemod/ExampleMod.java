package com.example.examplemod;

import com.example.examplemod.commands.ExampleQuietCommand;
import com.example.examplemod.commands.ExampleShowCommand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.server.S36PacketSignEditorOpen;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Mod(modid = ExampleMod.MODID, version = ExampleMod.VERSION , acceptableRemoteVersions = "*")
public class ExampleMod
{
    public static final String MODID = "examplemod";
    public static final String VERSION = "1.0";

    public static final String aqua = EnumChatFormatting.RESET + "" + EnumChatFormatting.AQUA;
    public static final String pink = EnumChatFormatting.RESET + "" + EnumChatFormatting.LIGHT_PURPLE;


    public static ArrayList<String> quietList;
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		// some example code
        System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
        System.out.println("GRAVEL BLOCK >> " + Blocks.gravel.getUnlocalizedName());

        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);

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
      /*  if (!event.world.isRemote && event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && event.entityPlayer.getHeldItem() == null
                && event.entityPlayer.isSneaking()) {
            final TileEntity te = event.world.getTileEntity(event.pos);
            if (te != null && te instanceof TileEntitySign) {
                if (event.entityPlayer instanceof EntityPlayerMP) {
                    ((EntityPlayerMP) event.entityPlayer).playerNetServerHandler
                            .sendPacket(new S36PacketSignEditorOpen(te.getPos()));
                    addMessage("Hello SIGN!", (EntityPlayerMP) event.entityPlayer);
                }

            }
        }*/
    }

    @SubscribeEvent
    public void onEntityJoin(PlayerEvent.PlayerLoggedInEvent event){


            System.out.println("Login Player found!");
            EntityPlayer player = event.player;
            System.out.println(player.getName() + " joined!");

            SaveHandler saveHandler = (SaveHandler) DimensionManager.getWorld(0).getSaveHandler();
            File file = new File(saveHandler.getWorldDirectory(), "/playerdata/" + player.getGameProfile().getId().toString() + ".dat");

            if(!file.exists()) {
                if( event.player.worldObj.isRemote ) return;

                //TODO: Clean UP this Mess!!!
                Item item = Item.getItemById(320);
                ItemStack is = new ItemStack(item, 5);

             //   is.setStackDisplayName("Welcome Food!");
                NBTTagCompound display = new NBTTagCompound();
                if( is.hasTagCompound()) {
                    System.out.println("Display Tag Found");
                    display = is.getTagCompound().getCompoundTag("display");
                }
                else {
                    System.out.println("Creating Tags");
                    is.setTagCompound(new NBTTagCompound());
                    is.setTagInfo("display", new NBTTagCompound());
                    display = is.getTagCompound().getCompoundTag("display");
                    display.setTag("Name",new NBTTagString("Welcome Food!"));
                    display.setTag("Lore", new NBTTagCompound());
                }

                NBTTagList list = display.getTagList("Lore", 9);

                list.appendTag(new NBTTagString(aqua+"Enjoy this food!"));
                list.appendTag(new NBTTagString(pink+"I hope you are ready to catch"));
                list.appendTag(new NBTTagString(pink+"all the pokemon!"));

                display.setTag("Lore", list);
                display.setTag("Name",new NBTTagString("Welcome Food!"));
                is.getTagCompound().setTag("display", display);
                //is.setTagInfo("display",new NBTTagString("{display:{Name:\"name here\",color:0x5555F,Lore:[\"lore here\", \"lore here\"]}}"));
                player.inventory.addItemStackToInventory(is);

                player.addChatMessage(new ChatComponentText("\2478**\247cWelcome\247f to our server, " + player.getName()));
            }
            else {
                player.addChatMessage(new ChatComponentText("\2478**\247cWelcome\247f back! " + player.getName()));
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

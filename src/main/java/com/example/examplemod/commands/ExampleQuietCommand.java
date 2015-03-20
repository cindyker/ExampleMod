package com.example.examplemod.commands;

import com.example.examplemod.ExampleMod;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cindy on 3/20/2015.
 */
public class ExampleQuietCommand implements ICommand {


    private List aliases;
    public ExampleQuietCommand()
    {
        this.aliases = new ArrayList();
        this.aliases.add("quiet");
    }

    @Override
    public String getName()
    {
        return "quiet";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        return "quiet <text>";
    }

    @Override
    public List getAliases()
    {
        return this.aliases;
    }

    @Override
    public void execute(ICommandSender icommandsender, String[] astring)
    {
        if(astring.length > 1)
        {
            icommandsender.addChatMessage(new ChatComponentText("Invalid arguments"));
            return;
        }

        if(icommandsender instanceof  EntityPlayerMP) {
            EntityPlayerMP ep = (EntityPlayerMP) icommandsender;

            icommandsender.addChatMessage(new ChatComponentText("[MODCHAT]  Quieting chat now."));
            if (!ExampleMod.quietList.contains(ep.getUniqueID().toString())) {
                ExampleMod.quietList.add(ep.getUniqueID().toString());
                ExampleMod.addMessage("Chat is now Quieted! Use /show to see chat again!", ep);
                return;
            }
        }
    }

    @Override
    public boolean canCommandSenderUse(ICommandSender icommandsender)
    {
        return true;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender icommandsender,
                                        String[] astring, BlockPos loc)
    {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] astring, int i)
    {
        return false;
    }

    @Override
    public int compareTo(Object o)
    {
        return 0;
    }
}


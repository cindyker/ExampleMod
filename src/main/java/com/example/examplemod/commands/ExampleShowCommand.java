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
public class ExampleShowCommand implements ICommand {

    private List aliases;
    public ExampleShowCommand()
    {
        this.aliases = new ArrayList();
        this.aliases.add("show");
    }

    @Override
    public String getName()
    {
        return "show";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        return "show <text>";
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

        if(icommandsender instanceof EntityPlayerMP) {
            EntityPlayerMP ep = (EntityPlayerMP) icommandsender;

            icommandsender.addChatMessage(new ChatComponentText("[MODCHAT]  Quieting chat now."));
            if (ExampleMod.quietList.contains(ep.getUniqueID().toString())) {
                ExampleMod.quietList.remove(ep.getUniqueID().toString());
                ExampleMod.addMessage("Chat will now be shown again! Use /quiet to remove chat again!", ep);
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

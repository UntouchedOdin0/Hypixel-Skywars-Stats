package io.conorthedev.hypixelswstats.cmds;

import java.util.List;

import io.conorthedev.hypixelswstats.MainClass;
import io.conorthedev.hypixelswstats.config.FileHandler;
import io.conorthedev.hypixelswstats.gui.SettingsGui;
import io.conorthedev.hypixelswstats.utils.C;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@SuppressWarnings("unused")
public class MainCommand extends CommandBase {

    Minecraft mc = Minecraft.getMinecraft();
    private static boolean display = false;
    private MainClass mod;

    @Override
    public String getCommandName() {

        return "skywarsstats";
    }

    @Override
    public String getCommandUsage(ICommandSender arg0) {
        return "/skywarsstats <gui | reset | debug>";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender arg0, String[] arg1) throws CommandException {

        if (arg1[0].equals("reset")) {
        	
            MainClass.totalCoins = 0;
            MainClass.totalKills = 0;
            MainClass.totalXP = 0;
            MainClass.totalKarma = 0;
            MainClass.totalWins = 0;
            MainClass.gamemode = "Not in Game";
            
            mc.thePlayer.addChatComponentMessage(new ChatComponentText(C.GREEN + C.BOLD + "Successfully reset stats!"));
            
        } else if (arg1[0].equals("gui")) {
            MainClass.displayGui = true;
        } else if (arg1.length < 0) {
            mc.thePlayer.addChatComponentMessage(new ChatComponentText(C.BOLD + C.RED + "Warning: The Arguments you entered are not correct, please use: '/skywarsstats <gui | reset>'"));
        } else if (arg1[0].equals("chroma")) {
        	if (MainClass.chromaEnabled == true) {
        		MainClass.chromaEnabled = false;
        		FileHandler.saveSettings();
                mc.thePlayer.addChatComponentMessage(new ChatComponentText(C.GREEN + "Chroma Disabled."));
        	}else if (MainClass.chromaEnabled == false) {
        		MainClass.chromaEnabled = true;
        		FileHandler.saveSettings();
                mc.thePlayer.addChatComponentMessage(new ChatComponentText(C.GREEN + "Chroma Enabled"));

        	}
        			
        }

    }
    
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (List<String>)((args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, new String[] { "reset", "gui", "chroma"}) : null);
    }
}
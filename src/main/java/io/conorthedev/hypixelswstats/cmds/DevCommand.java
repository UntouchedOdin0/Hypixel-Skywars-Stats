package io.conorthedev.hypixelswstats.cmds;

import java.util.List;
import java.util.Random;

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
public class DevCommand extends CommandBase {

    Minecraft mc = Minecraft.getMinecraft();
    private static boolean display = false;
    private MainClass mod;
    
    Random r = new Random();
    int Low = 10;
    int High = 100;
    int ErrorCode = r.nextInt(High-Low) + Low;

    @Override
    public String getCommandName() {

        return "skywarsstatsdev";
    }

    @Override
    public String getCommandUsage(ICommandSender arg0) {
        return "/skywarsstatsdev <debug | print>";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender arg0, String[] arg1) throws CommandException {

        if (arg1[0].equals("debug")) {
            if (MainClass.debugEnabled == false) {
                MainClass.debugEnabled = true;
                FileHandler.saveSettings();
                mc.thePlayer.addChatComponentMessage(new ChatComponentText(C.GREEN + C.BOLD + "Developer DEBUG Enabled!"));
            } else if (MainClass.debugEnabled == true) {
                MainClass.debugEnabled = false;
                FileHandler.saveSettings();
                mc.thePlayer.addChatComponentMessage(new ChatComponentText(C.RED + C.BOLD + "Developer DEBUG Disabled!"));
            }
        } else if (arg1[0].equals("print")) {
            mc.thePlayer.addChatComponentMessage(new ChatComponentText(C.RED + "Error. contact ConorTheDev. " + C.RED + C.BOLD + "Error Code: " + ErrorCode));
        }

    }
    
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (List<String>)((args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, new String[] { "debug", "print"}) : null);
    }
}
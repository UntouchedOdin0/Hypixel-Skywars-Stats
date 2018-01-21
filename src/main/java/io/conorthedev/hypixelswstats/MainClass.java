package io.conorthedev.hypixelswstats;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

import io.conorthedev.hypixelswstats.cmds.DevCommand;
import io.conorthedev.hypixelswstats.cmds.MainCommand;
import io.conorthedev.hypixelswstats.config.FileHandler;
import io.conorthedev.hypixelswstats.gui.GuiHandler;
import io.conorthedev.hypixelswstats.gui.SettingsGui;
import io.conorthedev.hypixelswstats.utils.C;
import io.conorthedev.hypixelswstats.utils.ChatUtils;
import net.hypixel.api.util.GameType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.command.ICommand;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@SuppressWarnings("unused")
@Mod(modid = MainClass.MODID, version = MainClass.VERSION)
public class MainClass {
	
    public static int totalKarma;
    public static int rainbow;
    public static String hypixelIP = "*.hypixel.net";
    public static boolean displayGui = false;
    public static boolean debugEnabled;
    private boolean isHypixel = false;
    private boolean tasksStarted = false;
    public static int totalCoins;
    public static int totalXP;
    public static int totalKills;
    public static int totalWins;
    public static String gamemode;
	public static boolean chromaEnabled;
    public String name = Minecraft.getMinecraft().getSession().getProfile().getName();
    Minecraft mc = Minecraft.getMinecraft();
    FontRenderer fRender = Minecraft.getMinecraft().fontRendererObj;
	private int index;
	private long x;
    public static final String MODID = "Hypixel Skywars Stats by ConorTheDev";
    public static final String VERSION = "3";

    @SuppressWarnings("deprecation")
    @EventHandler
    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(this);
        //load config
        FileHandler.loadSettings();
        //register handlers
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new GuiHandler());
        //reset values
        totalKarma = 0;
        totalCoins = 0;
        totalXP = 0;
        totalKills = 0;
        totalWins = 0;
        gamemode = "Not in Game";
        debugEnabled = false;
        //register command
        try {
            ClientCommandHandler.instance.registerCommand(new MainCommand());
        } catch (Exception e) {
            System.out.println("error");
        }
        try {
            ClientCommandHandler.instance.registerCommand(new DevCommand());
        } catch (Exception e) {
            System.out.println("error");
        }
    }


    public MainClass() {
        this.index = 0;
        this.x = 0L;
    }
    
    //every tick do 
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (displayGui == true && mc.inGameHasFocus == true) {

            mc.displayGuiScreen(new SettingsGui());
            displayGui = false;
            mc.inGameHasFocus = false;
        } else if (displayGui == true && mc.inGameHasFocus == false) {
        	
        	mc.inGameHasFocus = true;
        	mc.thePlayer.closeScreen();
        	displayGui = false;
        }
    }
    
    public static Color rainbowEffect(final float f, final float fade) {
        final float hue = (System.nanoTime() + f) / 4.0E9f % 1.0f;
        final long color = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, 1.0f, 1.0f))), 16);
        final Color c = new Color((int)color);
        return new Color(c.getRed() / 255.0f * fade, c.getGreen() / 255.0f * fade, c.getBlue() / 255.0f * fade, c.getAlpha() / 255.0f);
    }

    /*
    @SubscribeEvent
    public void onClientCnctServer(ClientConnectedToServerEvent event) {
    	if(Minecraft.getMinecraft().getCurrentServerData().serverIP.equalsIgnoreCase(hypixelIP)){
    		//state that mod is in beta
    		mc.thePlayer.addChatComponentMessage(new ChatComponentText(C.BOLD + C.RED + "Warning: The Hypixel Stats Mod is currently in Beta. Bugs and other incomplete features may be experienced. Current version: " + VERSION));
    	}
    }
    
    */

    // kills
    @SubscribeEvent
    public void kills(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        //message is the message which the client receives.
        if (message.startsWith("+") && message.contains("coins") && message.contains("Kill")) {
            totalKills = totalKills + 1;

            if (debugEnabled == true) {
                mc.thePlayer.addChatComponentMessage(new ChatComponentText(C.GREEN + C.BOLD + "+1 Kill"));
            }
        }
    }

    // experience
    @SubscribeEvent
    public void xp(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        //message is the message which the client receives.
        if (message.contains("Experience")) {
            //Checks if the coin message you got isn't the tip message 
            String[] splittedMessage = message.split("Experience");
            message = splittedMessage[0].replace("+", "");
            message = message.replace(" ", "");
            int xp = Integer.parseInt(message);
            totalXP = totalXP + xp;

            if (debugEnabled == true) {
                mc.thePlayer.addChatComponentMessage(new ChatComponentText(C.GREEN + C.BOLD + "+" + xp + " Experience Earned"));
            }
        }
    }

    @SubscribeEvent
    public void coins(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        //message is the message which the client receives.
        if (message.startsWith("+") && message.contains("coins!")) {
            //Checks if the coin message you got isn't the tip message 
            String[] splittedMessage = message.split("coins");
            message = splittedMessage[0].replace("+", "");
            message = message.replace(" ", "");
            int coins = Integer.parseInt(message);
            totalCoins = totalCoins + coins;

            if (debugEnabled == true) {
                mc.thePlayer.addChatComponentMessage(new ChatComponentText(C.GREEN + C.BOLD + "+" + coins + " Coins Earned"));
            }
        }
    }

    @SubscribeEvent
    public void karma(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        //message is the message which the client receives.
        if (message.startsWith("+") && message.contains("Karma!")) {
            //Checks if the coin message you got isn't the tip message 
            String[] splittedMessage = message.split("Karma");
            message = splittedMessage[0].replace("+", "");
            message = message.replace(" ", "");
            int karma = Integer.parseInt(message);
            totalKarma = totalKarma + karma;

            if (debugEnabled == true) {
                mc.thePlayer.addChatComponentMessage(new ChatComponentText(C.GREEN + C.BOLD + "+" + karma + " Karma Earned"));
            }
        }
    }

    // gamemode <solo | teams>
    @SubscribeEvent
    public void gamemode(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        //message is the message which the client receives.
        if (message.equals("Teaming is not allowed on Solo mode!")) {
            gamemode = "Solo";
            if (debugEnabled == true) {
                mc.thePlayer.addChatComponentMessage(new ChatComponentText(C.GREEN + C.BOLD + gamemode + ": " + "Current Gamemode"));
            }
        } else if (message.equals("Cross Teaming / Teaming with other teams is not allowed!")) {
            gamemode = "Teams";
            if (debugEnabled == true) {
                mc.thePlayer.addChatComponentMessage(new ChatComponentText(C.GREEN + C.BOLD + gamemode + ": " + "Current Gamemode"));
            }
        }else if (message.startsWith("Teaming is not allowed on Ranked")) {
        	gamemode = "Ranked";
            if (debugEnabled == true) {
                mc.thePlayer.addChatComponentMessage(new ChatComponentText(C.GREEN + C.BOLD + gamemode + ": " + "Current Gamemode"));
            }
        }
    }

    //wins 
    @SubscribeEvent
    public void wins(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        //message is the message which the client receives.
        if (message.startsWith("+") && message.contains("coins") && message.contains("Win")) {
            totalWins = totalWins + 1;

            if (debugEnabled == true) {
                mc.thePlayer.addChatComponentMessage(new ChatComponentText(C.GREEN + C.BOLD + "+1 Win"));
            }
        }
    }

    @SubscribeEvent
    public void render(RenderGameOverlayEvent event) {
        if (event.isCancelable() || event.type != ElementType.EXPERIENCE) {
            return;
        }
		rainbow = rainbowEffect(this.index + this.x * 2000.0f, 1.0f).getRGB();
		
		if(chromaEnabled == true) {
			this.mc.fontRendererObj.drawString("Coins: " + totalCoins, 5, 5, rainbow);
			this.mc.fontRendererObj.drawString("XP: " + totalXP, 5, 17, rainbow);
			this.mc.fontRendererObj.drawString("Kills: " + totalKills, 5, 41, rainbow);
			this.mc.fontRendererObj.drawString("Karma: " + totalKarma, 5, 29, rainbow);
			this.mc.fontRendererObj.drawString("Gamemode: " + gamemode, 5, 53, rainbow);
			this.mc.fontRendererObj.drawString("Wins: " + totalWins, 5, 65, rainbow);
			if(debugEnabled == true) {
				this.mc.fontRendererObj.drawString("DEBUG MODE", 5, 77, rainbow);
			}
		}else {
			Minecraft.getMinecraft().fontRendererObj.drawString(C.GREEN + "Coins: " + EnumChatFormatting.WHITE + totalCoins, 5, 5, 0);
        	Minecraft.getMinecraft().fontRendererObj.drawString(C.BLUE + "XP: " + EnumChatFormatting.WHITE + totalXP, 5, 17, 0);
        	Minecraft.getMinecraft().fontRendererObj.drawString(C.RED + "Kills: " + EnumChatFormatting.WHITE + totalKills, 5, 29, 0);
        	Minecraft.getMinecraft().fontRendererObj.drawString(C.DARK_PURPLE + "Karma: " + EnumChatFormatting.WHITE + totalKarma, 5, 41, 0);
        	Minecraft.getMinecraft().fontRendererObj.drawString(C.GOLD + "Gamemode: " + EnumChatFormatting.WHITE + gamemode, 5, 53, 0);
        	Minecraft.getMinecraft().fontRendererObj.drawString(C.AQUA + "Wins: " + EnumChatFormatting.WHITE + totalWins, 5, 65, 0);
        	if (debugEnabled == true) {
        		Minecraft.getMinecraft().fontRendererObj.drawString(C.RED + C.BOLD + C.UNDERLINE + "DEBUG MODE", 5, 77, 0);
        	}
		}
    }
}

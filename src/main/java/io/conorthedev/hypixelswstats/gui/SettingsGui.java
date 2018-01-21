package io.conorthedev.hypixelswstats.gui;

import java.awt.Color;
import java.io.IOException;

import io.conorthedev.hypixelswstats.MainClass;
import io.conorthedev.hypixelswstats.config.FileHandler;
import io.conorthedev.hypixelswstats.utils.C;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings("unused")
public class SettingsGui extends GuiScreen {

	public static final int ID = 0;
	
    private GuiButton a;
    private GuiButton b;
    private GuiButton c;
	private GuiButton d;
    
    Minecraft mc = Minecraft.getMinecraft();
    FontRenderer frenderer = Minecraft.getMinecraft().fontRendererObj;

    public boolean doesGuiPauseGame() {
        return false;
    }

    public boolean doesGuiClose() {
        return true;
    }

    public void drawScreen(int mouseX, int mouseY, float partials) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partials);
    }

    @Override
    public void initGui() {
        this.buttonList.add(this.a = new GuiButton(0, this.width / 2 - 100, this.height / 2 + 30, "Open Developer Settings"));
        this.buttonList.add(this.b = new GuiButton(1, this.width / 2 - 100, this.height / 2 + 50, "Reset Values"));
        this.buttonList.add(this.c = new GuiButton(2, this.width / 2 - 100, this.height / 2 + 70, "Close GUI"));
        this.buttonList.add(this.d = new GuiButton(3, this.width / 2 - 100, this.height / 2 + 10, (MainClass.chromaEnabled ? "Disable" : "Enable") + " Chroma"));
    }

	@Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
        	mc.inGameHasFocus = true;
        	mc.thePlayer.closeScreen();
        	MainClass.displayGui = false;
        }
        if (keyCode == 32) {
        	mc.inGameHasFocus = true;
        	mc.thePlayer.closeScreen();
        	MainClass.displayGui = false;
        	mc.thePlayer.addChatComponentMessage(new ChatComponentText(C.RED + C.BOLD + "Developer Settings Temp Disabled!"));
        }
    }
    
    @SubscribeEvent
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button == this.a) {
        	mc.inGameHasFocus = true;
        	mc.thePlayer.closeScreen();
        	MainClass.displayGui = false;
        }
        if (button == this.b) {
            MainClass.totalCoins = 0;
            MainClass.totalKills = 0;
            MainClass.totalXP = 0;
            MainClass.totalKarma = 0;
            MainClass.totalWins = 0;
            MainClass.gamemode = "Not in Game";
            
        	mc.inGameHasFocus = true;
        	mc.thePlayer.closeScreen();
        	MainClass.displayGui = false;
        	
            mc.thePlayer.addChatComponentMessage(new ChatComponentText(C.GREEN + C.BOLD + "Successfully reset stats!"));
        }
        if (button == this.c) {
        	mc.inGameHasFocus = true;
        	mc.thePlayer.closeScreen();
        	MainClass.displayGui = false;

        }
        if (button == this.d) {
        	 this.d.displayString = ((MainClass.chromaEnabled = (MainClass.chromaEnabled ? false : true)) ? "Disable" : "Enable") + " Chroma";
        }
    }
}
package io.conorthedev.hypixelswstats.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler{

	@Override
	public Object getClientGuiElement(int id, EntityPlayer arg1, World arg2, int arg3, int arg4, int arg5) {
			if(SettingsGui.ID == id) {
				return new SettingsGui();
			}
		return null;
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer arg1, World arg2, int arg3, int arg4, int arg5) {
		// TODO Auto-generated method stub
		return null;
	}

}

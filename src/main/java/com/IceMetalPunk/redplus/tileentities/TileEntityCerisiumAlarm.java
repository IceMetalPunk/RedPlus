package com.IceMetalPunk.redplus.tileentities;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCerisiumAlarm extends TileEntity {

	private ItemStack linkedMonitor = null;
	private boolean isActive = false;

	public TileEntityCerisiumAlarm() {
		super();
	}

	public void linkMonitor(ItemStack stack) {
		if (linkedMonitor == null) {
			if (!this.worldObj.isRemote) {
				linkedMonitor = stack;
			}
			else {
				Minecraft.getMinecraft().ingameGUI.func_110326_a("Alarm successfully linked!", false);
			}
		}
		else if (this.worldObj.isRemote) {
			Minecraft.getMinecraft().ingameGUI.func_110326_a("Alarm is already linked!", false);
		}
	}

	public void setAlarm(boolean on) {
		isActive = on;
	}

	@Override
	public void updateEntity() {
		//I am just going to write random things to cause an error here to remind me that this is NOT finished!
	}

}

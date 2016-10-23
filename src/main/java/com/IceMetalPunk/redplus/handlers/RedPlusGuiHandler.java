package com.IceMetalPunk.redplus.handlers;

import com.IceMetalPunk.redplus.tileentities.TileEntityBreaker;
import com.IceMetalPunk.redplus.tileentities.TileEntityPlacer;
import com.IceMetalPunk.redplus.tileentities.containers.ContainerBreaker;
import com.IceMetalPunk.redplus.tileentities.containers.ContainerPlacer;
import com.IceMetalPunk.redplus.tileentities.gui.GuiBreaker;
import com.IceMetalPunk.redplus.tileentities.gui.GuiPlacer;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class RedPlusGuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 0) {
			return new ContainerPlacer(player.inventory, (TileEntityPlacer) world.getTileEntity(x, y, z));
		}
		else if (ID == 1) {
			return new ContainerBreaker(player.inventory, (TileEntityBreaker) world.getTileEntity(x, y, z));
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 0) {
			TileEntityPlacer tileEntity = (TileEntityPlacer) world.getTileEntity(x, y, z);
			return new GuiPlacer(new ContainerPlacer(player.inventory, tileEntity), tileEntity);
		}
		else if (ID == 1) {
			TileEntityBreaker tileEntity = (TileEntityBreaker) world.getTileEntity(x, y, z);
			return new GuiBreaker(new ContainerBreaker(player.inventory, tileEntity), tileEntity);
		}
		return null;
	}

}

package com.IceMetalPunk.redplus.tileentities.gui;

import org.lwjgl.opengl.GL11;

import com.IceMetalPunk.redplus.tileentities.TileEntityPlacer;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiPlacer extends GuiContainer {

	TileEntityPlacer tilePlacer = null;
	private static final ResourceLocation placerGuiTextures = new ResourceLocation(
			"textures/gui/container/dispenser.png");

	public GuiPlacer(Container container, TileEntityPlacer tileEntity) {
		super(container);
		this.tilePlacer = tileEntity;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int p1, int p2) {
		String s = this.tilePlacer.hasCustomInventoryName() ? this.tilePlacer.getInventoryName()
				: I18n.format(this.tilePlacer.getInventoryName(), new Object[0]);
		this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2,
				4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p1, int p2, int p3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(placerGuiTextures);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}

}

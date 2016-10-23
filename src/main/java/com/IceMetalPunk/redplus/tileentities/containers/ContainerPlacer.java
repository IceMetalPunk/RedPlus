package com.IceMetalPunk.redplus.tileentities.containers;

import com.IceMetalPunk.redplus.tileentities.TileEntityPlacer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerPlacer extends Container {
	private TileEntityPlacer tilePlacer;

	public ContainerPlacer(IInventory playerInventory, TileEntityPlacer placerTE) {
		this.tilePlacer = placerTE;
		int i;
		int j;

		for (i = 0; i < 3; ++i) {
			for (j = 0; j < 3; ++j) {
				this.addSlotToContainer(new BlockSlot(placerTE, j + i * 3, 62 + j * 18, 17 + i * 18));
			}
		}

		for (i = 0; i < 3; ++i) {
			for (j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 142));
		}
	}

	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return this.tilePlacer.isUseableByPlayer(p_75145_1_);
	}

	/**
	 * Called when a player shift-clicks on a slot. You must override this or
	 * you will crash when someone does that.
	 */
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNum) {
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(slotNum);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (slotNum < 9) {
				if (!this.mergeItemStack(itemstack1, 9, 45, true)) {
					return null;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 0, 9, false)) {
				return null;
			}

			if (itemstack1.stackSize == 0) {
				slot.putStack((ItemStack) null);
			}
			else {
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(player, itemstack1);
		}

		return itemstack;
	}

}
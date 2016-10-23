package com.IceMetalPunk.redplus.blocks;

import java.lang.reflect.Field;

import net.minecraft.block.BlockRailPowered;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class BlockRailItemizer extends BlockRailPowered {
	public BlockRailItemizer() {
		super();
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		if (world.isRemote || !(entity instanceof EntityMinecart)) {
			return;
		}
		if (entity.isDead) {
			return;
		}
		if ((world.getBlockMetadata(x, y, z) & 8) == 0) {
			return;
		}
		NBTTagCompound cartTags = new NBTTagCompound();
		entity.writeToNBT(cartTags);
		ItemStack cartItem = ((EntityMinecart) entity).getCartItem();
		cartItem.stackSize = 1;
		cartItem.stackTagCompound = cartTags;
		EntityItem droppedItem = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, cartItem);
		droppedItem.motionX = 0.0;
		droppedItem.motionY = 0.0;
		droppedItem.motionZ = 0.0;

		if (entity instanceof EntityMinecartContainer) {
			try {
				Field dropConts = EntityMinecartContainer.class.getDeclaredField("dropContentsWhenDead");
				dropConts.setAccessible(true);
				dropConts.setBoolean(entity, false);
			}
			catch (NoSuchFieldException e) {
				System.out.println("No such field!");
			}
			catch (SecurityException e) {
				System.out.println("Security exception!");
			}
			catch (IllegalArgumentException e) {
				System.out.println("Illegal Argument!");
			}
			catch (IllegalAccessException e) {
				System.out.println("Illegal Access Exception!");
			}
		}

		entity.setDead();
		world.spawnEntityInWorld(droppedItem);
	}

}

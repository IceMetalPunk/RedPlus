package com.IceMetalPunk.redplus.handlers;

import com.IceMetalPunk.redplus.blocks.BlockCerisiumEndstone;
import com.IceMetalPunk.redplus.entity.EntityQuartzGolem;
import com.IceMetalPunk.redplus.properties.RedPlusEntityProperties;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.BlockRailBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;

public class RedPlusEventHandler {

	@SubscribeEvent
	public void constructEntities(EntityConstructing event) {
		if (RedPlusEntityProperties.get(event.entity) == null) {
			event.entity.registerExtendedProperties(RedPlusEntityProperties.EXT_PROP_NAME, new RedPlusEntityProperties(event.entity, event.entity.worldObj));
		}
	}

	/* Below is for the spawning of Quartz Golems */
	@SubscribeEvent
	public boolean handlePumpkinSpawning(BlockEvent.PlaceEvent event) {
		if (event.world.isRemote) {
			return false;
		}
		// Quartz golems can only be spawned in the Nether!
		if (event.world.provider.dimensionId == -1 && event.block instanceof BlockPumpkin) {
			if (event.world.getBlock(event.x, event.y - 1, event.z) == Blocks.quartz_block && event.world.getBlock(event.x, event.y - 2, event.z) == Blocks.quartz_block) {
				boolean flag = event.world.getBlock(event.x - 1, event.y - 1, event.z) == Blocks.quartz_block && event.world.getBlock(event.x + 1, event.y - 1, event.z) == Blocks.quartz_block;
				boolean flag1 = event.world.getBlock(event.x, event.y - 1, event.z - 1) == Blocks.quartz_block && event.world.getBlock(event.x, event.y - 1, event.z + 1) == Blocks.quartz_block;

				if (flag || flag1) {
					event.world.setBlock(event.x, event.y, event.z, Blocks.air, 0, 2);
					event.world.setBlock(event.x, event.y - 1, event.z, Blocks.air, 0, 2);
					event.world.setBlock(event.x, event.y - 2, event.z, Blocks.air, 0, 2);

					if (flag) {
						event.world.setBlock(event.x - 1, event.y - 1, event.z, Blocks.air, 0, 2);
						event.world.setBlock(event.x + 1, event.y - 1, event.z, Blocks.air, 0, 2);
					}
					else {
						event.world.setBlock(event.x, event.y - 1, event.z - 1, Blocks.air, 0, 2);
						event.world.setBlock(event.x, event.y - 1, event.z + 1, Blocks.air, 0, 2);
					}

					EntityQuartzGolem entityquartzgolem = new EntityQuartzGolem(event.world);
					entityquartzgolem.setPlayerCreated(true);
					entityquartzgolem.setLocationAndAngles((double) event.x + 0.5D, (double) event.y - 1.95D, (double) event.z + 0.5D, 0.0F, 0.0F);
					event.world.spawnEntityInWorld(entityquartzgolem);

					for (int l = 0; l < 120; ++l) {
						event.world.spawnParticle("snowballpoof", (double) event.x + event.world.rand.nextDouble(), (double) (event.y - 2) + event.world.rand.nextDouble() * 3.9D, (double) event.z + event.world.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
					}

					event.world.notifyBlockChange(event.x, event.y, event.z, Blocks.air);
					event.world.notifyBlockChange(event.x, event.y - 1, event.z, Blocks.air);
					event.world.notifyBlockChange(event.x, event.y - 2, event.z, Blocks.air);

					if (flag) {
						event.world.notifyBlockChange(event.x - 1, event.y - 1, event.z, Blocks.air);
						event.world.notifyBlockChange(event.x + 1, event.y - 1, event.z, Blocks.air);
					}
					else {
						event.world.notifyBlockChange(event.x, event.y - 1, event.z - 1, Blocks.air);
						event.world.notifyBlockChange(event.x, event.y - 1, event.z + 1, Blocks.air);
					}
				}
			}
		}
		return true;
	}

	/*
	 * Below handles the active Cerisium End Stone behavior, preventing Endermen
	 * standing on it from teleporting
	 */
	@SubscribeEvent
	public boolean handleEndermanTelepots(EnderTeleportEvent event) {
		EntityLivingBase entity = event.entityLiving;
		World world = entity.worldObj;
		int bX = MathHelper.floor_double(entity.posX);
		int bY = MathHelper.floor_double(entity.posY);
		int bZ = MathHelper.floor_double(entity.posZ);
		if (entity instanceof EntityEnderman && bY >= 1 && world.getBlock(bX, bY - 1, bZ) instanceof BlockCerisiumEndstone && world.getBlockMetadata(bX, bY - 1, bZ) == 1) {
			event.setCanceled(true);
			return false;
		}
		return true;
	}

	/* Bone meal upgrades, including working on cactus and Nether wart */
	@SubscribeEvent
	public boolean handleBonemeal(PlayerInteractEvent event) {
		World world = event.entityPlayer.worldObj;
		if (world.isRemote || event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
			return false;
		}
		ItemStack item = event.entityPlayer.getCurrentEquippedItem();
		if (item == null) {
			return false;
		}
		if (item.getItem() == Items.dye && item.getItemDamage() == 15) {
			Block block = world.getBlock(event.x, event.y, event.z);
			if (block == Blocks.nether_wart) {
				int l = world.getBlockMetadata(event.x, event.y, event.z);
				if (l < 3) {
					world.setBlockMetadataWithNotify(event.x, event.y, event.z, ++l, 2);
				}
			}
			else if (block == Blocks.cactus) {
				boolean found = false;
				Block foundBlock = world.getBlock(event.x, event.y, event.z);
				for (int yy = event.y + 1; yy < world.getActualHeight() && foundBlock == Blocks.cactus; ++yy) {
					foundBlock = world.getBlock(event.x, yy, event.z);
					if (world.isAirBlock(event.x, yy, event.z)) {
						world.setBlock(event.x, yy, event.z, Blocks.cactus);
						world.setBlockMetadataWithNotify(event.x, yy - 1, event.z, 0, 4);
						Blocks.cactus.onNeighborBlockChange(world, event.x, yy, event.z, Blocks.cactus);
						if (!event.entityPlayer.capabilities.isCreativeMode) {
							--item.stackSize;
						}
						world.playAuxSFX(2005, event.x, event.y, event.z, 0);
						world.playAuxSFX(2005, event.x, yy, event.z, 0);
						return true;
					}
				}
			}
		}
		return false;
	}

	/*
	 * The handleCarts() method handles placing minecarts with custom data tags
	 */
	@SubscribeEvent
	public boolean handleCarts(PlayerInteractEvent event) {
		World world = event.entityPlayer.worldObj;
		if (world.isRemote || event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
			return false;
		}
		ItemStack item = event.entityPlayer.getCurrentEquippedItem();
		if (item == null) {
			return false;
		}
		if (item.getItem() instanceof ItemMinecart && ((ItemMinecart) (item.getItem())).minecartType > 0) {
			if (item.stackTagCompound != null) {
				if (BlockRailBase.func_150051_a(world.getBlock(event.x, event.y, event.z))) {
					EntityMinecart entityminecart = EntityMinecart.createMinecart(world, (double) ((float) event.x + 0.5F), (double) ((float) event.y + 0.5F), (double) ((float) event.z + 0.5F), ((ItemMinecart) (item.getItem())).minecartType);

					entityminecart.readFromNBT(item.stackTagCompound);
					if (item.hasDisplayName()) {
						entityminecart.setMinecartName(item.getDisplayName());
					}

					entityminecart.posX = (double) ((float) event.x + 0.5F);
					entityminecart.posY = (double) ((float) event.y + 0.5F);
					entityminecart.posZ = (double) ((float) event.z + 0.5F);
					entityminecart.motionX = 0.0;
					entityminecart.motionY = 0.0;
					entityminecart.motionZ = 0.0;

					world.spawnEntityInWorld(entityminecart);

					--item.stackSize;
					event.setCanceled(true);
					return true;
				}
				else {
					return false;
				}
			}
		}
		return true;
	}

	/*
	 * Used for dispensers, to simulate player bonemealing of non-vanilla things
	 */
	public boolean fakeBonemeal(World world, int x, int y, int z, ItemStack item) {
		if (world.isRemote) {
			return false;
		}
		if (item.getItem() == Items.dye && item.getItemDamage() == 15) {
			Block block = world.getBlock(x, y - 1, z);
			Block upBlock = world.getBlock(x, y, z);
			if (upBlock == Blocks.nether_wart) {
				int l = world.getBlockMetadata(x, y, z);
				if (l < 3) {
					world.setBlockMetadataWithNotify(x, y, z, ++l, 2);
					--item.stackSize;
					world.playAuxSFX(2005, x, y, z, 0);
				}
			}
			if (block == Blocks.cactus) {
				boolean found = false;
				Block foundBlock = world.getBlock(x, y - 1, z);
				for (int yy = y; yy < world.getActualHeight() && foundBlock == Blocks.cactus; ++yy) {
					foundBlock = world.getBlock(x, yy, z);
					if (world.isAirBlock(x, yy, z)) {
						world.setBlock(x, yy, z, Blocks.cactus);
						world.setBlockMetadataWithNotify(x, yy - 1, z, 0, 4);
						Blocks.cactus.onNeighborBlockChange(world, x, yy, z, Blocks.cactus);
						--item.stackSize;
						world.playAuxSFX(2005, x, y, z, 0);
						world.playAuxSFX(2005, x, yy, z, 0);
						return true;
					}
				}
			}
		}
		return false;
	}
}

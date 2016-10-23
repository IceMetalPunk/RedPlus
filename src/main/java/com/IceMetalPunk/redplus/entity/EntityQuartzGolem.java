package com.IceMetalPunk.redplus.entity;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class EntityQuartzGolem extends EntityIronGolem {
	/** decrements, and a distance-to-home check is done at 0 */
	private int attackTimer;

	public EntityQuartzGolem(World p_i1694_1_) {
		super(p_i1694_1_);
		this.isImmuneToFire = true;
		this.setSize(1.4F, 2.9F);
		this.getNavigator().setAvoidsWater(true);
		this.tasks.addTask(1, new EntityAIAttackOnCollide(this, 1.0D, true));
		this.tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
		this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(6, new EntityAIWander(this, 0.6D));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, false, true, IMob.mobSelector));
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(31, Byte.valueOf((byte) 0));
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	@Override
	public boolean isAIEnabled() {
		return true;
	}

	/**
	 * main AI tick function, replaces updateEntityActionState
	 */
	@Override
	protected void updateAITick() {
		super.updateAITick();
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
	}

	/**
	 * Decrements the entity's air supply when underwater
	 */
	@Override
	protected int decreaseAirSupply(int p_70682_1_) {
		return p_70682_1_;
	}

	@Override
	protected void collideWithEntity(Entity p_82167_1_) {
		if (p_82167_1_ instanceof IMob && this.getRNG().nextInt(20) == 0) {
			this.setAttackTarget((EntityLivingBase) p_82167_1_);
		}

		super.collideWithEntity(p_82167_1_);
	}

	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		List players = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(this.posX - 15, this.posY - 3, this.posZ - 15, this.posX + 15, this.posY + 3, this.posZ + 15));

		EntityPlayer player = null;
		for (int p = 0; p < players.size(); ++p) {
			player = (EntityPlayer) players.get(p);
			if (!player.capabilities.isCreativeMode && this.canEntityBeSeen(player)) {
				this.setAttackTarget(player);
				this.setTarget(player);
			}
		}

		if (this.attackTimer > 0) {
			--this.attackTimer;
		}

		if (this.motionX * this.motionX + this.motionZ * this.motionZ > 2.500000277905201E-7D && this.rand.nextInt(5) == 0) {
			int i = MathHelper.floor_double(this.posX);
			int j = MathHelper.floor_double(this.posY - 0.20000000298023224D - (double) this.yOffset);
			int k = MathHelper.floor_double(this.posZ);
			Block block = this.worldObj.getBlock(i, j, k);

			if (block.getMaterial() != Material.air) {
				this.worldObj.spawnParticle("blockcrack_" + Block.getIdFromBlock(block) + "_" + this.worldObj.getBlockMetadata(i, j, k), this.posX + ((double) this.rand.nextFloat() - 0.5D) * (double) this.width, this.boundingBox.minY + 0.1D, this.posZ + ((double) this.rand.nextFloat() - 0.5D) * (double) this.width, 4.0D * ((double) this.rand.nextFloat() - 0.5D), 0.5D, ((double) this.rand.nextFloat() - 0.5D) * 4.0D);
			}
		}
	}

	/**
	 * Returns true if this entity can attack entities of the specified class.
	 */
	public boolean canAttackClass(Class p_70686_1_) {
		return EntityCreeper.class != p_70686_1_ && EntityGhast.class != p_70686_1_;
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setBoolean("PlayerCreated", this.isPlayerCreated());
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		this.setPlayerCreated(p_70037_1_.getBoolean("PlayerCreated"));
	}

	public boolean attackEntityAsMob(Entity p_70652_1_) {
		this.attackTimer = 10;
		this.worldObj.setEntityState(this, (byte) 4);
		boolean flag = p_70652_1_.attackEntityFrom(DamageSource.causeMobDamage(this), (float) (7 + this.rand.nextInt(15)));

		if (flag) {
			p_70652_1_.motionY += 0.4000000059604645D;
		}

		this.playSound("mob.irongolem.throw", 1.0F, 1.0F);
		return flag;
	}

	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte p_70103_1_) {
		if (p_70103_1_ == 4) {
			this.attackTimer = 10;
			this.playSound("mob.irongolem.throw", 1.0F, 1.0F);
		}
		else if (p_70103_1_ != 11) {
			super.handleHealthUpdate(p_70103_1_);
		}
	}

	@SideOnly(Side.CLIENT)
	public int getAttackTimer() {
		return this.attackTimer;
	}

	public void setHoldingRose(boolean p_70851_1_) {
		this.worldObj.setEntityState(this, (byte) 11);
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "mob.irongolem.hit";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "mob.irongolem.death";
	}

	protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_) {
		this.playSound("mob.irongolem.walk", 1.0F, 1.0F);
	}

	/**
	 * Drop 0-2 items of this living's type. @param par1 - Whether this entity
	 * has recently been hit by a player. @param par2 - Level of Looting used to
	 * kill this mob.
	 */
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		int j = this.rand.nextInt(3);
		int k;

		for (k = 0; k < j; ++k) {
			this.func_145778_a(Items.quartz, 1, 0.0F);
		}

		k = 4 + this.rand.nextInt(3);

		for (int l = 0; l < k; ++l) {
			this.dropItem(Item.getItemFromBlock(Blocks.quartz_block), 1);
		}
	}

	public boolean isPlayerCreated() {
		return (this.dataWatcher.getWatchableObjectByte(31) & 1) != 0;
	}

	public void setPlayerCreated(boolean p_70849_1_) {
		byte b0 = this.dataWatcher.getWatchableObjectByte(31);

		if (p_70849_1_) {
			this.dataWatcher.updateObject(31, Byte.valueOf((byte) (b0 | 1)));
		}
		else {
			this.dataWatcher.updateObject(31, Byte.valueOf((byte) (b0 & -2)));
		}
	}

	/**
	 * Called when the mob's health reaches 0.
	 */
	public void onDeath(DamageSource p_70645_1_) {
		if (ForgeHooks.onLivingDeath(this, p_70645_1_)) return;
		Entity entity = p_70645_1_.getEntity();
		EntityLivingBase entitylivingbase = this.func_94060_bK();

		if (this.scoreValue >= 0 && entitylivingbase != null) {
			entitylivingbase.addToPlayerScore(this, this.scoreValue);
		}

		if (entity != null) {
			entity.onKillEntity(this);
		}

		this.dead = true;
		this.func_110142_aN().func_94549_h();

		if (!this.worldObj.isRemote) {
			int i = 0;

			if (entity instanceof EntityPlayer) {
				i = EnchantmentHelper.getLootingModifier((EntityLivingBase) entity);
			}

			captureDrops = true;
			capturedDrops.clear();
			int j = 0;

			if (this.func_146066_aG() && this.worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot")) {
				this.dropFewItems(this.recentlyHit > 0, i);
				this.dropEquipment(this.recentlyHit > 0, i);

				if (this.recentlyHit > 0) {
					j = this.rand.nextInt(200) - i;

					if (j < 5) {
						this.dropRareDrop(j <= 0 ? 1 : 0);
					}
				}
			}

			captureDrops = false;

			if (!ForgeHooks.onLivingDrops(this, p_70645_1_, capturedDrops, i, recentlyHit > 0, j)) {
				for (EntityItem item : capturedDrops) {
					worldObj.spawnEntityInWorld(item);
				}
			}
		}

		this.worldObj.setEntityState(this, (byte) 3);
	}
}
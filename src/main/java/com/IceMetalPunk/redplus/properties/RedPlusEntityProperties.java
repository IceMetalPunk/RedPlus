package com.IceMetalPunk.redplus.properties;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class RedPlusEntityProperties implements IExtendedEntityProperties {

	public Entity myEntity;
	public double lastX, lastY, lastZ;
	public float lastPitch, lastYaw;

	public final static String EXT_PROP_NAME = "RedPlusProps";

	public RedPlusEntityProperties(Entity entity, World world) {
		this.init(entity, world);
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		compound.setDouble("lastX", this.myEntity.posX);
		compound.setDouble("lastY", this.myEntity.posY);
		compound.setDouble("lastZ", this.myEntity.posZ);
		compound.setFloat("lastPitch", this.myEntity.rotationPitch);
		compound.setFloat("lastYaw", this.myEntity.rotationYaw);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		this.lastX = compound.getDouble("lastX");
		this.lastY = compound.getDouble("lasY");
		this.lastZ = compound.getDouble("lastZ");
		this.lastPitch = compound.getFloat("lastPitch");
		this.lastYaw = compound.getFloat("lastYaw");
	}

	@Override
	public void init(Entity entity, World world) {
		this.myEntity = entity;
		this.lastX = entity.posX;
		this.lastY = entity.posY;
		this.lastZ = entity.posZ;
		this.lastPitch = entity.rotationPitch;
		this.lastYaw = entity.rotationYaw;
	}

	public static final RedPlusEntityProperties get(Entity entity) {
		return (RedPlusEntityProperties) entity.getExtendedProperties(EXT_PROP_NAME);
	}

	public void update() {
		this.lastX = this.myEntity.posX;
		this.lastY = this.myEntity.posY;
		this.lastZ = this.myEntity.posZ;
		this.lastPitch = this.myEntity.rotationPitch;
		this.lastYaw = this.myEntity.rotationYaw;
	}

}

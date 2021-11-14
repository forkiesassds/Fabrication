package com.unascribed.fabrication.mixin.c_tweaks.no_trample;

import com.unascribed.fabrication.support.EligibleIf;
import com.unascribed.fabrication.support.MixinConfigPlugin;

import com.unascribed.fabrication.support.ConfigPredicates;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmlandBlock.class)
@EligibleIf(anyConfigAvailable={"*.feather_falling_no_trample", "*.no_trample"})
public abstract class MixinFarmBlock extends Block {

	public MixinFarmBlock(Settings settings) {
		super(settings);
	}

	@Inject(method= "onLandedUpon(Lnet/minecraft/world/World;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;F)V",
			at=@At("HEAD"), cancellable=true)
	public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance, CallbackInfo ci) {
		if (!(MixinConfigPlugin.isEnabled("*.no_trample") || MixinConfigPlugin.isEnabled("*.feather_falling_no_trample"))) return;
		if (entity instanceof LivingEntity && ConfigPredicates.shouldRun("*.no_trample", (LivingEntity)entity)) {
			super.onLandedUpon(world, state, pos, entity, fallDistance);
			ci.cancel();
		}
	}
}
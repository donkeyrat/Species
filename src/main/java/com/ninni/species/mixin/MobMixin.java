package com.ninni.species.mixin;

import com.ninni.species.registry.SpeciesDataMaps;
import com.ninni.species.server.entity.mob.update_1.Stackatick;
import com.ninni.species.server.item.util.HasImportantInteraction;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {

    @Shadow @Nullable public abstract <T extends Mob> T convertTo(EntityType<?> entityType, boolean transferInventory);

    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public void setCustomName(Component name) {
        String string = ChatFormatting.stripFormatting(name.getString());
        if ("Dinnerbone".equals(string) || "Grumm".equals(string)) {
            EntityType<?> newType = this.getType().builtInRegistryHolder().getData(SpeciesDataMaps.EntityTypes.UPSIDE_DOWN_CONVERSION);
            if (newType != null) {
                this.convertTo(newType, false);
                return;
            }
        }

        super.setCustomName(name);
    }

    @Inject(method = "checkAndHandleImportantInteractions", at = @At("HEAD"), cancellable = true)
    private void checkAndHandleImportantInteractions(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        InteractionResult result;
        ItemStack stack = player.getItemInHand(hand);

        if (stack.getItem() instanceof HasImportantInteraction && (result = stack.interactLivingEntity(player, this, hand)).consumesAction()) {
            cir.setReturnValue(result);
        }
    }

    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    private void interact(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (this.getVehicle() instanceof Stackatick && player.isSecondaryUseActive()) {
            this.stopRiding();
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }

}

package com.ninni.species.mixin.client;

import com.ninni.species.SpeciesDevelopers;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerSkin.class)
public abstract class AbstractClientPlayerMixin {

    @Shadow @Final private PlayerSkin.Model model;

    //??????? Maybe???

    @Inject(at = @At("HEAD"), method = "capeTexture", cancellable = true)
    private void getCapeLocation(CallbackInfoReturnable<ResourceLocation> cir) {
        if (SpeciesDevelopers.developerUUIDS.containsKey(this.model.id())) {
            cir.setReturnValue(SpeciesDevelopers.developerUUIDS.get(this.model.id()).getCapeTexture());
        }
    }

    @Inject(at = @At("HEAD"), method = "elytraTexture", cancellable = true)
    private void getElytraLocation(CallbackInfoReturnable<ResourceLocation> cir) {
        if (SpeciesDevelopers.developerUUIDS.containsKey(this.model.id())) {
            cir.setReturnValue(SpeciesDevelopers.developerUUIDS.get(this.model.id()).getCapeTexture());
        }
    }

}

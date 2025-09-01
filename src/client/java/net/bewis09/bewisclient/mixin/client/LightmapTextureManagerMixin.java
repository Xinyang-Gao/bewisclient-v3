package net.bewis09.bewisclient.mixin.client;

import net.bewis09.bewisclient.impl.settings.functionalities.FullbrightSettings;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LightmapTextureManager.class)
abstract
class LightmapTextureManagerMixin {
    @Redirect(method = "update", at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F", ordinal = 1))
    private float invokeGamma(Double instance) {
        if (!FullbrightSettings.INSTANCE.getEnabled().get()) {
            return instance.floatValue();
        }
        return FullbrightSettings.INSTANCE.getBrightness().get();
    }

    @Redirect(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/dimension/DimensionType;ambientLight()F"))
    private float redirectAmbientLight(net.minecraft.world.dimension.DimensionType instance) {
        if (!FullbrightSettings.INSTANCE.getNightVision().get() || !FullbrightSettings.INSTANCE.getEnabled().get()) {
            return instance.ambientLight();
        }
        return 1.0f;
    }
}
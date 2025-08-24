package net.bewis09.bewisclient.mixin.client;

import net.bewis09.bewisclient.impl.functionalities.held_item_info.HeldItemTooltip;
import net.bewis09.bewisclient.impl.settings.functionalities.HeldItemTooltipSettings;
import net.bewis09.bewisclient.impl.settings.functionalities.ScoreboardSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
abstract
class InGameHudMixin {
    @Shadow public abstract TextRenderer getTextRenderer();

    @Shadow private int heldItemTooltipFade;

    @Shadow private ItemStack currentStack;

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "renderHeldItemTooltip", at = @At("HEAD"), cancellable = true)
    private void bewisclient$renderHeldItemTooltip(DrawContext drawContext, CallbackInfo ci) {
        if (HeldItemTooltipSettings.INSTANCE.getEnabled().get()) {
            HeldItemTooltip.INSTANCE.render(drawContext, getTextRenderer(), heldItemTooltipFade, currentStack);
            ci.cancel();
        }
    }

    @Inject(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V", at = @At("HEAD"))
    private void bewisclient$renderScoreboardSidebar(DrawContext context, ScoreboardObjective objective, CallbackInfo ci) {
        float scale = ScoreboardSettings.INSTANCE.getEnabled().get() ? ScoreboardSettings.INSTANCE.getScale().get() : 1.0f;

        context.getMatrices().pushMatrix();
        context.getMatrices().scale(scale, scale);
        context.getMatrices().translate((float)(-client.getWindow().getScaledWidth()) * (1.0f - 1 / scale), (float)(-client.getWindow().getScaledHeight()) * (1.0f - 1 / scale) / 2.0f);
    }

    @Inject(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V", at = @At("RETURN"))
    private void bewisclient$renderScoreboardSidebarReturn(DrawContext context, ScoreboardObjective objective, CallbackInfo ci) {
        context.getMatrices().popMatrix();
    }
}
package com.ninni.species.compat.jei;

import com.ninni.species.Species;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GooberGooConversionCategory implements IRecipeCategory<GooberGooConversionRecipe> {

    public static final ResourceLocation ID = Species.of("goober_goo_conversion");
    public static final ResourceLocation TEXTURE = Species.of("textures/gui/jei/goober_goo.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable slotDrawable;
    private final IDrawable goober;
    private final IDrawable sparkles;
    private final IDrawable resultArrow;

    public GooberGooConversionCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(150, 40);
        this.goober = guiHelper.createDrawable(TEXTURE, 0, 48, 61, 29);
        this.icon = guiHelper.createDrawable(TEXTURE, 0, 0, 16, 16);
        this.slotDrawable = guiHelper.getSlotDrawable();
        this.sparkles = guiHelper.createDrawable(TEXTURE, 0, 28, 18, 13);
        this.resultArrow = guiHelper.createDrawable(TEXTURE, 0, 16, 21, 12);
    }

    @Override
    public RecipeType<GooberGooConversionRecipe> getRecipeType() {
        return SpeciesJEI.GOOBER_GOO_CONVERSION;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.species.goober_goo");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GooberGooConversionRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 67, 12).addItemStack(new ItemStack(recipe.input().asItem()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 110, 12).addItemStack(new ItemStack(recipe.output().asItem()));
    }

    @Override
    public void draw(GooberGooConversionRecipe recipe, IRecipeSlotsView view, GuiGraphics stack, double mouseX, double mouseY) {
        slotDrawable.draw(stack, 66, 11);
        slotDrawable.draw(stack, 109, 11);
        goober.draw(stack, 3, 4);
        sparkles.draw(stack, 109, -2);
        resultArrow.draw(stack, 86, 14);
    }

}

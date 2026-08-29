package com.ninni.species.compat.jei;

import com.ninni.species.Species;
import com.ninni.species.registry.SpeciesDataMaps;
import com.ninni.species.server.CruncherHunting;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
@JeiPlugin
public class SpeciesJEI implements IModPlugin {

    public static final RecipeType<GooberGooConversionRecipe> GOOBER_GOO_CONVERSION = new RecipeType<>(GooberGooConversionCategory.ID, GooberGooConversionRecipe.class);
    public static final RecipeType<CruncherHunting> CRUNCHER_HUNTING = new RecipeType<>(CruncherHuntingCategory.ID, CruncherHunting.class);

    @Override
    public ResourceLocation getPluginUid() {
        return Species.of("jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new GooberGooConversionCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CruncherHuntingCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;

        Registry<Block> registry = level.registryAccess().registry(Registries.BLOCK).orElseThrow();
        List<GooberGooConversionRecipe> gooberGooConversionRecipes = registry.getDataMap(SpeciesDataMaps.Blocks.GOOBER_GOO_CONVERSION).entrySet().stream()
            .map(entry -> new GooberGooConversionRecipe(registry.get(entry.getKey()), entry.getValue()))
            .toList();
        registration.addRecipes(new RecipeType<>(GooberGooConversionCategory.ID, GooberGooConversionRecipe.class), gooberGooConversionRecipes);

        List<CruncherHunting> cruncherPelletRecipe = CruncherHunting.getData(level).stream().toList();
        registration.addRecipes(new RecipeType<>(CruncherHuntingCategory.ID, CruncherHunting.class), cruncherPelletRecipe);
    }

}
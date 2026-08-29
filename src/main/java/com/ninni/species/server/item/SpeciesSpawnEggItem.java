package com.ninni.species.server.item;

import com.ninni.species.SpeciesDevelopers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class SpeciesSpawnEggItem extends SpawnEggItem {

    public SpeciesDevelopers.SpeciesDeveloperNames developer;

    public SpeciesSpawnEggItem(EntityType<? extends Mob> defaultType, int backgroundColor, int highlightColor, SpeciesDevelopers.SpeciesDeveloperNames developer, Properties properties) {
        super(defaultType, backgroundColor, highlightColor, properties);
        this.developer = developer;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag flag) {
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("species.developer.made_by").withStyle(ChatFormatting.GRAY));
        list.add(Component.translatable("species.developer.contribution_level." + developer.getContributionLevel().getContributionLevelName()).withStyle(developer.getFormatting()).append(Component.translatable(developer.getName()).withStyle(developer.getFormatting())));

        super.appendHoverText(stack, context, list, flag);
    }

}

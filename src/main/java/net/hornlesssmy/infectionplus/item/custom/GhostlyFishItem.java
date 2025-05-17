package net.hornlesssmy.infectionplus.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GhostlyFishItem extends Item {

    public GhostlyFishItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ItemStack result = super.finishUsing(stack, world, user);

        if (!world.isClient && user instanceof PlayerEntity player) {
            ItemStack ghostlyFish = new ItemStack(this);
            if (!player.getInventory().insertStack(ghostlyFish)) {
                player.dropItem(ghostlyFish, true);
            }
        }
        return result;
    }
}

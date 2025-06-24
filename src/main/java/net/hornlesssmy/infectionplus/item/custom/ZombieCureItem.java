package net.hornlesssmy.infectionplus.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public class ZombieCureItem extends Item {
    public ZombieCureItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient) {
            ServerPlayerEntity player = (ServerPlayerEntity) user;
        }
        return super.finishUsing(stack, world, user);
    }
}
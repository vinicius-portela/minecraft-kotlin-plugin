package net.minecraft.server;

import java.util.function.Predicate;

public class ItemBow extends ItemProjectileWeapon {

    public ItemBow(Item.Info item_info) {
        super(item_info);
        this.a(new MinecraftKey("pull"), (itemstack, world, entityliving) -> {
            return entityliving == null ? 0.0F : (entityliving.dl().getItem() != Items.BOW ? 0.0F : (float) (itemstack.k() - entityliving.dm()) / 20.0F);
        });
        this.a(new MinecraftKey("pulling"), (itemstack, world, entityliving) -> {
            return entityliving != null && entityliving.isHandRaised() && entityliving.dl() == itemstack ? 1.0F : 0.0F;
        });
    }

    @Override
    public void a(ItemStack itemstack, World world, EntityLiving entityliving, int i) {
        if (entityliving instanceof EntityHuman) {
            EntityHuman entityhuman = (EntityHuman) entityliving;
            boolean flag = entityhuman.abilities.canInstantlyBuild || EnchantmentManager.getEnchantmentLevel(Enchantments.ARROW_INFINITE, itemstack) > 0;
            ItemStack itemstack1 = entityhuman.f(itemstack);

            if (!itemstack1.isEmpty() || flag) {
                if (itemstack1.isEmpty()) {
                    itemstack1 = new ItemStack(Items.ARROW);
                }

                int j = this.f_(itemstack) - i;
                float f = a(j);

                if ((double) f >= 0.1D) {
                    boolean flag1 = flag && itemstack1.getItem() == Items.ARROW;

                    if (!world.isClientSide) {
                        ItemArrow itemarrow = (ItemArrow) ((ItemArrow) (itemstack1.getItem() instanceof ItemArrow ? itemstack1.getItem() : Items.ARROW));
                        EntityArrow entityarrow = itemarrow.a(world, itemstack1, (EntityLiving) entityhuman);

                        entityarrow.a(entityhuman, entityhuman.pitch, entityhuman.yaw, 0.0F, f * 3.0F, 1.0F);
                        if (f == 1.0F) {
                            entityarrow.setCritical(true);
                        }

                        int k = EnchantmentManager.getEnchantmentLevel(Enchantments.ARROW_DAMAGE, itemstack);

                        if (k > 0) {
                            entityarrow.setDamage(entityarrow.getDamage() + (double) k * 0.5D + 0.5D);
                        }

                        int l = EnchantmentManager.getEnchantmentLevel(Enchantments.ARROW_KNOCKBACK, itemstack);

                        if (l > 0) {
                            entityarrow.setKnockbackStrength(l);
                        }

                        if (EnchantmentManager.getEnchantmentLevel(Enchantments.ARROW_FIRE, itemstack) > 0) {
                            entityarrow.setOnFire(100);
                        }

                        itemstack.damage(1, entityhuman, (entityhuman1) -> {
                            entityhuman1.d(entityhuman.getRaisedHand());
                        });
                        if (flag1 || entityhuman.abilities.canInstantlyBuild && (itemstack1.getItem() == Items.SPECTRAL_ARROW || itemstack1.getItem() == Items.TIPPED_ARROW)) {
                            entityarrow.fromPlayer = EntityArrow.PickupStatus.CREATIVE_ONLY;
                        }

                        world.addEntity(entityarrow);
                    }

                    world.a((EntityHuman) null, entityhuman.locX, entityhuman.locY, entityhuman.locZ, SoundEffects.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (ItemBow.i.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    if (!flag1 && !entityhuman.abilities.canInstantlyBuild) {
                        itemstack1.subtract(1);
                        if (itemstack1.isEmpty()) {
                            entityhuman.inventory.f(itemstack1);
                        }
                    }

                    entityhuman.b(StatisticList.ITEM_USED.b(this));
                }
            }
        }
    }

    public static float a(int i) {
        float f = (float) i / 20.0F;

        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    @Override
    public int f_(ItemStack itemstack) {
        return 72000;
    }

    @Override
    public EnumAnimation e_(ItemStack itemstack) {
        return EnumAnimation.BOW;
    }

    @Override
    public InteractionResultWrapper<ItemStack> a(World world, EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);
        boolean flag = !entityhuman.f(itemstack).isEmpty();

        if (!entityhuman.abilities.canInstantlyBuild && !flag) {
            return flag ? new InteractionResultWrapper<>(EnumInteractionResult.PASS, itemstack) : new InteractionResultWrapper<>(EnumInteractionResult.FAIL, itemstack);
        } else {
            entityhuman.c(enumhand);
            return new InteractionResultWrapper<>(EnumInteractionResult.SUCCESS, itemstack);
        }
    }

    @Override
    public Predicate<ItemStack> b() {
        return ItemBow.a;
    }
}

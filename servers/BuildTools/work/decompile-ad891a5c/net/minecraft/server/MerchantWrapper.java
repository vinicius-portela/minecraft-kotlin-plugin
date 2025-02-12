package net.minecraft.server;

import javax.annotation.Nullable;

public class MerchantWrapper implements IMerchant {

    private final InventoryMerchant a;
    private final EntityHuman b;
    private MerchantRecipeList c = new MerchantRecipeList();
    private int d;

    public MerchantWrapper(EntityHuman entityhuman) {
        this.b = entityhuman;
        this.a = new InventoryMerchant(this);
    }

    @Nullable
    @Override
    public EntityHuman getTrader() {
        return this.b;
    }

    @Override
    public void setTradingPlayer(@Nullable EntityHuman entityhuman) {}

    @Override
    public MerchantRecipeList getOffers() {
        return this.c;
    }

    @Override
    public void a(MerchantRecipe merchantrecipe) {
        merchantrecipe.increaseUses();
    }

    @Override
    public void i(ItemStack itemstack) {}

    @Override
    public World getWorld() {
        return this.b.world;
    }

    @Override
    public int dV() {
        return this.d;
    }

    @Override
    public void q(int i) {
        this.d = i;
    }

    @Override
    public boolean dZ() {
        return true;
    }
}

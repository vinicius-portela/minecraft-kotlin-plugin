package net.minecraft.server;

public class MerchantRecipe {

    public ItemStack buyingItem1;
    public ItemStack buyingItem2;
    public final ItemStack sellingItem;
    public int uses;
    public int maxUses;
    public boolean rewardExp;
    private int g;
    private int h;
    public float priceMultiplier;
    public int xp;

    public MerchantRecipe(NBTTagCompound nbttagcompound) {
        this.rewardExp = true;
        this.xp = 1;
        this.buyingItem1 = ItemStack.a(nbttagcompound.getCompound("buy"));
        this.buyingItem2 = ItemStack.a(nbttagcompound.getCompound("buyB"));
        this.sellingItem = ItemStack.a(nbttagcompound.getCompound("sell"));
        this.uses = nbttagcompound.getInt("uses");
        if (nbttagcompound.hasKeyOfType("maxUses", 99)) {
            this.maxUses = nbttagcompound.getInt("maxUses");
        } else {
            this.maxUses = 4;
        }

        if (nbttagcompound.hasKeyOfType("rewardExp", 1)) {
            this.rewardExp = nbttagcompound.getBoolean("rewardExp");
        }

        if (nbttagcompound.hasKeyOfType("xp", 3)) {
            this.xp = nbttagcompound.getInt("xp");
        }

        if (nbttagcompound.hasKeyOfType("priceMultiplier", 5)) {
            this.priceMultiplier = nbttagcompound.getFloat("priceMultiplier");
        }

        this.g = nbttagcompound.getInt("specialPrice");
        this.h = nbttagcompound.getInt("demand");
    }

    public MerchantRecipe(ItemStack itemstack, ItemStack itemstack1, int i, int j, float f) {
        this(itemstack, ItemStack.a, itemstack1, i, j, f);
    }

    public MerchantRecipe(ItemStack itemstack, ItemStack itemstack1, ItemStack itemstack2, int i, int j, float f) {
        this(itemstack, itemstack1, itemstack2, 0, i, j, f);
    }

    public MerchantRecipe(ItemStack itemstack, ItemStack itemstack1, ItemStack itemstack2, int i, int j, int k, float f) {
        this.rewardExp = true;
        this.xp = 1;
        this.buyingItem1 = itemstack;
        this.buyingItem2 = itemstack1;
        this.sellingItem = itemstack2;
        this.uses = i;
        this.maxUses = j;
        this.xp = k;
        this.priceMultiplier = f;
    }

    public ItemStack a() {
        return this.buyingItem1;
    }

    public ItemStack getBuyItem1() {
        int i = this.buyingItem1.getCount();
        ItemStack itemstack = this.buyingItem1.cloneItemStack();
        int j = Math.max(0, MathHelper.d((float) (i * this.h) * this.priceMultiplier));

        itemstack.setCount(MathHelper.clamp(i + j + this.g, 1, this.buyingItem1.getItem().getMaxStackSize()));
        return itemstack;
    }

    public ItemStack getBuyItem2() {
        return this.buyingItem2;
    }

    public ItemStack getSellingItem() {
        return this.sellingItem;
    }

    public void e() {
        this.h = this.h + this.uses - (this.maxUses - this.uses);
    }

    public ItemStack f() {
        return this.sellingItem.cloneItemStack();
    }

    public int g() {
        return this.uses;
    }

    public void h() {
        this.uses = 0;
    }

    public int i() {
        return this.maxUses;
    }

    public void increaseUses() {
        ++this.uses;
    }

    public void increaseUses(int i) {
        this.g += i;
    }

    public void k() {
        this.g = 0;
    }

    public int l() {
        return this.g;
    }

    public void b(int i) {
        this.g = i;
    }

    public float m() {
        return this.priceMultiplier;
    }

    public int n() {
        return this.xp;
    }

    public boolean isFullyUsed() {
        return this.uses >= this.maxUses;
    }

    public void p() {
        this.uses = this.maxUses;
    }

    public boolean q() {
        return this.rewardExp;
    }

    public NBTTagCompound r() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        nbttagcompound.set("buy", this.buyingItem1.save(new NBTTagCompound()));
        nbttagcompound.set("sell", this.sellingItem.save(new NBTTagCompound()));
        nbttagcompound.set("buyB", this.buyingItem2.save(new NBTTagCompound()));
        nbttagcompound.setInt("uses", this.uses);
        nbttagcompound.setInt("maxUses", this.maxUses);
        nbttagcompound.setBoolean("rewardExp", this.rewardExp);
        nbttagcompound.setInt("xp", this.xp);
        nbttagcompound.setFloat("priceMultiplier", this.priceMultiplier);
        nbttagcompound.setInt("specialPrice", this.g);
        nbttagcompound.setInt("demand", this.h);
        return nbttagcompound;
    }

    public boolean a(ItemStack itemstack, ItemStack itemstack1) {
        return this.c(itemstack, this.getBuyItem1()) && itemstack.getCount() >= this.getBuyItem1().getCount() && this.c(itemstack1, this.buyingItem2) && itemstack1.getCount() >= this.buyingItem2.getCount();
    }

    private boolean c(ItemStack itemstack, ItemStack itemstack1) {
        if (itemstack1.isEmpty() && itemstack.isEmpty()) {
            return true;
        } else {
            ItemStack itemstack2 = itemstack.cloneItemStack();

            if (itemstack2.getItem().usesDurability()) {
                itemstack2.setDamage(itemstack2.getDamage());
            }

            return ItemStack.c(itemstack2, itemstack1) && (!itemstack1.hasTag() || itemstack2.hasTag() && GameProfileSerializer.a(itemstack1.getTag(), itemstack2.getTag(), false));
        }
    }

    public boolean b(ItemStack itemstack, ItemStack itemstack1) {
        if (!this.a(itemstack, itemstack1)) {
            return false;
        } else {
            itemstack.subtract(this.getBuyItem1().getCount());
            if (!this.getBuyItem2().isEmpty()) {
                itemstack1.subtract(this.getBuyItem2().getCount());
            }

            return true;
        }
    }
}

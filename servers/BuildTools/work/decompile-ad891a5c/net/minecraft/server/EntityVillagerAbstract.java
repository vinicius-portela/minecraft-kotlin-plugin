package net.minecraft.server;

import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;

public abstract class EntityVillagerAbstract extends EntityAgeable implements NPC, IMerchant {

    @Nullable
    private EntityHuman tradingPlayer;
    @Nullable
    protected MerchantRecipeList trades;
    private final InventorySubcontainer inventory = new InventorySubcontainer(8);
    private int bC;

    public EntityVillagerAbstract(EntityTypes<? extends EntityVillagerAbstract> entitytypes, World world) {
        super(entitytypes, world);
    }

    @Override
    public int dV() {
        return 0;
    }

    @Override
    protected float b(EntityPose entitypose, EntitySize entitysize) {
        return this.isBaby() ? 0.81F : 1.62F;
    }

    @Override
    public void setTradingPlayer(@Nullable EntityHuman entityhuman) {
        this.tradingPlayer = entityhuman;
    }

    @Nullable
    @Override
    public EntityHuman getTrader() {
        return this.tradingPlayer;
    }

    public boolean dX() {
        return this.tradingPlayer != null;
    }

    @Override
    public MerchantRecipeList getOffers() {
        if (this.trades == null) {
            this.trades = new MerchantRecipeList();
            this.ef();
        }

        return this.trades;
    }

    @Override
    public void q(int i) {}

    @Override
    public void a(MerchantRecipe merchantrecipe) {
        merchantrecipe.increaseUses();
        this.e = -this.A();
        this.a(this.ea(), this.getSoundVolume(), this.cU());
        this.b(merchantrecipe);
        if (this.tradingPlayer instanceof EntityPlayer) {
            CriterionTriggers.s.a((EntityPlayer) this.tradingPlayer, this, merchantrecipe.getSellingItem());
        }

    }

    protected abstract void b(MerchantRecipe merchantrecipe);

    @Override
    public boolean dZ() {
        return true;
    }

    @Override
    public void i(ItemStack itemstack) {
        if (!this.world.isClientSide && this.e > -this.A() + 20) {
            this.e = -this.A();
            this.a(this.r(!itemstack.isEmpty()), this.getSoundVolume(), this.cU());
        }

    }

    protected SoundEffect ea() {
        return SoundEffects.ENTITY_VILLAGER_YES;
    }

    protected SoundEffect r(boolean flag) {
        return flag ? SoundEffects.ENTITY_VILLAGER_YES : SoundEffects.ENTITY_VILLAGER_NO;
    }

    public void eb() {
        this.a(SoundEffects.ENTITY_VILLAGER_CELEBRATE, this.getSoundVolume(), this.cU());
    }

    @Override
    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        MerchantRecipeList merchantrecipelist = this.getOffers();

        if (!merchantrecipelist.isEmpty()) {
            nbttagcompound.set("Offers", merchantrecipelist.a());
        }

        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.inventory.getSize(); ++i) {
            ItemStack itemstack = this.inventory.getItem(i);

            if (!itemstack.isEmpty()) {
                nbttaglist.add(itemstack.save(new NBTTagCompound()));
            }
        }

        nbttagcompound.set("Inventory", nbttaglist);
    }

    @Override
    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        if (nbttagcompound.hasKeyOfType("Offers", 10)) {
            this.trades = new MerchantRecipeList(nbttagcompound.getCompound("Offers"));
        }

        NBTTagList nbttaglist = nbttagcompound.getList("Inventory", 10);

        for (int i = 0; i < nbttaglist.size(); ++i) {
            ItemStack itemstack = ItemStack.a(nbttaglist.getCompound(i));

            if (!itemstack.isEmpty()) {
                this.inventory.a(itemstack);
            }
        }

    }

    @Override
    public boolean a(EntityHuman entityhuman) {
        return false;
    }

    public int ec() {
        return this.bC;
    }

    public void r(int i) {
        this.bC = i;
    }

    public InventorySubcontainer getInventory() {
        return this.inventory;
    }

    @Override
    public boolean a_(int i, ItemStack itemstack) {
        if (super.a_(i, itemstack)) {
            return true;
        } else {
            int j = i - 300;

            if (j >= 0 && j < this.inventory.getSize()) {
                this.inventory.setItem(j, itemstack);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    protected abstract void ef();

    protected void a(MerchantRecipeList merchantrecipelist, VillagerTrades.IMerchantRecipeOption[] avillagertrades_imerchantrecipeoption, int i) {
        Set<Integer> set = Sets.newHashSet();

        if (avillagertrades_imerchantrecipeoption.length > i) {
            while (set.size() < i) {
                set.add(this.random.nextInt(avillagertrades_imerchantrecipeoption.length));
            }
        } else {
            for (int j = 0; j < avillagertrades_imerchantrecipeoption.length; ++j) {
                set.add(j);
            }
        }

        Iterator iterator = set.iterator();

        while (iterator.hasNext()) {
            Integer integer = (Integer) iterator.next();
            VillagerTrades.IMerchantRecipeOption villagertrades_imerchantrecipeoption = avillagertrades_imerchantrecipeoption[integer];
            MerchantRecipe merchantrecipe = villagertrades_imerchantrecipeoption.a(this, this.random);

            if (merchantrecipe != null) {
                merchantrecipelist.add(merchantrecipe);
            }
        }

    }
}

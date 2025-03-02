package net.minecraft.server;

import javax.annotation.Nullable;

public class EntityDamageSourceIndirect extends EntityDamageSource {

    private final Entity owner;

    public EntityDamageSourceIndirect(String s, Entity entity, @Nullable Entity entity1) {
        super(s, entity);
        this.owner = entity1;
    }

    @Nullable
    @Override
    public Entity j() {
        return this.x;
    }

    @Nullable
    @Override
    public Entity getEntity() {
        return this.owner;
    }

    @Override
    public IChatBaseComponent getLocalizedDeathMessage(EntityLiving entityliving) {
        IChatBaseComponent ichatbasecomponent = this.owner == null ? this.x.getScoreboardDisplayName() : this.owner.getScoreboardDisplayName();
        ItemStack itemstack = this.owner instanceof EntityLiving ? ((EntityLiving) this.owner).getItemInMainHand() : ItemStack.a;
        String s = "death.attack." + this.translationIndex;
        String s1 = s + ".item";

        return !itemstack.isEmpty() && itemstack.hasName() ? new ChatMessage(s1, new Object[] { entityliving.getScoreboardDisplayName(), ichatbasecomponent, itemstack.B()}) : new ChatMessage(s, new Object[] { entityliving.getScoreboardDisplayName(), ichatbasecomponent});
    }
}

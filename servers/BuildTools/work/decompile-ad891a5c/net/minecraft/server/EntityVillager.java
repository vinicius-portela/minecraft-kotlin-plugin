package net.minecraft.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

public class EntityVillager extends EntityVillagerAbstract implements ReputationHandler, VillagerDataHolder {

    private static final DataWatcherObject<VillagerData> bC = DataWatcher.a(EntityVillager.class, DataWatcherRegistry.q);
    public static final Map<Item, Integer> bA = ImmutableMap.of(Items.BREAD, 4, Items.POTATO, 1, Items.CARROT, 1, Items.BEETROOT, 1);
    private static final Set<Item> bD = ImmutableSet.of(Items.BREAD, Items.POTATO, Items.CARROT, Items.WHEAT, Items.WHEAT_SEEDS, Items.BEETROOT, new Item[] { Items.BEETROOT_SEEDS});
    private int bE;
    private boolean bF;
    @Nullable
    private EntityHuman bG;
    @Nullable
    private UUID bH;
    private long bI;
    private byte bL;
    private final Reputation bM;
    private long bN;
    private int bO;
    private long bP;
    private static final ImmutableList<MemoryModuleType<?>> bQ = ImmutableList.of(MemoryModuleType.HOME, MemoryModuleType.JOB_SITE, MemoryModuleType.MEETING_POINT, MemoryModuleType.MOBS, MemoryModuleType.VISIBLE_MOBS, MemoryModuleType.VISIBLE_VILLAGER_BABIES, MemoryModuleType.NEAREST_PLAYERS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.WALK_TARGET, MemoryModuleType.LOOK_TARGET, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.BREED_TARGET, new MemoryModuleType[] { MemoryModuleType.PATH, MemoryModuleType.INTERACTABLE_DOORS, MemoryModuleType.NEAREST_BED, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.NEAREST_HOSTILE, MemoryModuleType.SECONDARY_JOB_SITE, MemoryModuleType.GOLEM_SPAWN_CONDITIONS, MemoryModuleType.HIDING_PLACE, MemoryModuleType.HEARD_BELL_TIME});
    private static final ImmutableList<SensorType<? extends Sensor<? super EntityVillager>>> bR = ImmutableList.of(SensorType.b, SensorType.c, SensorType.d, SensorType.e, SensorType.f, SensorType.g, SensorType.h, SensorType.i);
    public static final Map<MemoryModuleType<GlobalPos>, BiPredicate<EntityVillager, VillagePlaceType>> bB = ImmutableMap.of(MemoryModuleType.HOME, (entityvillager, villageplacetype) -> {
        return villageplacetype == VillagePlaceType.q;
    }, MemoryModuleType.JOB_SITE, (entityvillager, villageplacetype) -> {
        return entityvillager.getVillagerData().getProfession().b() == villageplacetype;
    }, MemoryModuleType.MEETING_POINT, (entityvillager, villageplacetype) -> {
        return villageplacetype == VillagePlaceType.r;
    });

    public EntityVillager(EntityTypes<? extends EntityVillager> entitytypes, World world) {
        this(entitytypes, world, VillagerType.c);
    }

    public EntityVillager(EntityTypes<? extends EntityVillager> entitytypes, World world, VillagerType villagertype) {
        super(entitytypes, world);
        this.bI = Long.MIN_VALUE;
        this.bM = new Reputation();
        ((Navigation) this.getNavigation()).a(true);
        this.getNavigation().d(true);
        this.setCanPickupLoot(true);
        this.setVillagerData(this.getVillagerData().withType(villagertype).withProfession(VillagerProfession.NONE));
        this.br = this.a(new Dynamic(DynamicOpsNBT.a, new NBTTagCompound()));
    }

    @Override
    public BehaviorController<EntityVillager> getBehaviorController() {
        return super.getBehaviorController();
    }

    @Override
    protected BehaviorController<?> a(Dynamic<?> dynamic) {
        BehaviorController<EntityVillager> behaviorcontroller = new BehaviorController<>(EntityVillager.bQ, EntityVillager.bR, dynamic);

        this.a(behaviorcontroller);
        return behaviorcontroller;
    }

    public void a(WorldServer worldserver) {
        BehaviorController<EntityVillager> behaviorcontroller = this.getBehaviorController();

        behaviorcontroller.b(worldserver, this);
        this.br = behaviorcontroller.f();
        this.a(this.getBehaviorController());
    }

    private void a(BehaviorController<EntityVillager> behaviorcontroller) {
        VillagerProfession villagerprofession = this.getVillagerData().getProfession();
        float f = (float) this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue();

        if (this.isBaby()) {
            behaviorcontroller.a(Schedule.c);
            behaviorcontroller.a(Activity.d, Behaviors.a(f));
        } else {
            behaviorcontroller.a(Schedule.d);
            behaviorcontroller.a(Activity.c, Behaviors.b(villagerprofession, f), (Set) ImmutableSet.of(Pair.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_PRESENT)));
        }

        behaviorcontroller.a(Activity.a, Behaviors.a(villagerprofession, f));
        behaviorcontroller.a(Activity.f, Behaviors.d(villagerprofession, f), (Set) ImmutableSet.of(Pair.of(MemoryModuleType.MEETING_POINT, MemoryStatus.VALUE_PRESENT)));
        behaviorcontroller.a(Activity.e, Behaviors.c(villagerprofession, f));
        behaviorcontroller.a(Activity.b, Behaviors.e(villagerprofession, f));
        behaviorcontroller.a(Activity.g, Behaviors.f(villagerprofession, f));
        behaviorcontroller.a(Activity.i, Behaviors.g(villagerprofession, f));
        behaviorcontroller.a(Activity.h, Behaviors.h(villagerprofession, f));
        behaviorcontroller.a(Activity.j, Behaviors.i(villagerprofession, f));
        behaviorcontroller.a((Set) ImmutableSet.of(Activity.a));
        behaviorcontroller.b(Activity.b);
        behaviorcontroller.a(Activity.b);
        behaviorcontroller.a(this.world.getDayTime(), this.world.getTime());
    }

    @Override
    protected void l() {
        super.l();
        if (this.world instanceof WorldServer) {
            this.a((WorldServer) this.world);
        }

    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.5D);
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(48.0D);
    }

    @Override
    protected void mobTick() {
        this.world.getMethodProfiler().enter("brain");
        this.getBehaviorController().a((WorldServer) this.world, (EntityLiving) this);
        this.world.getMethodProfiler().exit();
        if (!this.dX() && this.bE > 0) {
            --this.bE;
            if (this.bE <= 0) {
                if (this.bF) {
                    this.populateTrades();
                    this.bF = false;
                }

                this.addEffect(new MobEffect(MobEffects.REGENERATION, 200, 0));
            }
        }

        if (this.bG != null && this.world instanceof WorldServer) {
            ((WorldServer) this.world).a(ReputationEvent.e, (Entity) this.bG, (ReputationHandler) this);
            this.world.broadcastEntityEffect(this, (byte) 14);
            this.bG = null;
        }

        if (!this.isNoAI() && this.random.nextInt(100) == 0) {
            Raid raid = ((WorldServer) this.world).c_(new BlockPosition(this));

            if (raid != null && raid.v() && !raid.a()) {
                this.world.broadcastEntityEffect(this, (byte) 42);
            }
        }

        super.mobTick();
    }

    public void eg() {
        this.setTradingPlayer((EntityHuman) null);
        this.et();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.ec() > 0) {
            this.r(this.ec() - 1);
        }

    }

    @Override
    public boolean a(EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);
        boolean flag = itemstack.getItem() == Items.NAME_TAG;

        if (flag) {
            itemstack.a(entityhuman, (EntityLiving) this, enumhand);
            return true;
        } else if (itemstack.getItem() != Items.VILLAGER_SPAWN_EGG && this.isAlive() && !this.dX() && !this.isSleeping()) {
            if (this.isBaby()) {
                this.es();
                return super.a(entityhuman, enumhand);
            } else {
                boolean flag1 = this.getOffers().isEmpty();

                if (enumhand == EnumHand.MAIN_HAND) {
                    if (flag1) {
                        this.es();
                    }

                    entityhuman.a(StatisticList.TALKED_TO_VILLAGER);
                }

                if (flag1) {
                    return super.a(entityhuman, enumhand);
                } else {
                    if (!this.world.isClientSide && !this.trades.isEmpty()) {
                        this.f(entityhuman);
                    }

                    return true;
                }
            }
        } else {
            return super.a(entityhuman, enumhand);
        }
    }

    private void es() {
        this.r(40);
        if (!this.world.e()) {
            this.a(SoundEffects.ENTITY_VILLAGER_NO, this.getSoundVolume(), this.cU());
        }

    }

    private void f(EntityHuman entityhuman) {
        this.g(entityhuman);
        this.setTradingPlayer(entityhuman);
        this.openTrade(entityhuman, this.getScoreboardDisplayName(), this.getVillagerData().getLevel());
    }

    public void ei() {
        Iterator iterator = this.getOffers().iterator();

        while (iterator.hasNext()) {
            MerchantRecipe merchantrecipe = (MerchantRecipe) iterator.next();

            merchantrecipe.e();
            merchantrecipe.h();
        }

        this.bP = this.world.getDayTime() % 24000L;
    }

    private void g(EntityHuman entityhuman) {
        int i = this.bM.a(entityhuman.getUniqueID(), (reputationtype) -> {
            return reputationtype != ReputationType.GOLEM;
        });

        if (i != 0) {
            Iterator iterator = this.getOffers().iterator();

            while (iterator.hasNext()) {
                MerchantRecipe merchantrecipe = (MerchantRecipe) iterator.next();

                merchantrecipe.increaseUses(-MathHelper.d((float) i * merchantrecipe.m()));
            }
        }

        if (entityhuman.hasEffect(MobEffects.HERO_OF_THE_VILLAGE)) {
            MobEffect mobeffect = entityhuman.getEffect(MobEffects.HERO_OF_THE_VILLAGE);
            int j = mobeffect.getAmplifier();
            Iterator iterator1 = this.getOffers().iterator();

            while (iterator1.hasNext()) {
                MerchantRecipe merchantrecipe1 = (MerchantRecipe) iterator1.next();
                double d0 = 0.3D + 0.0625D * (double) j;
                int k = (int) Math.floor(d0 * (double) merchantrecipe1.a().getCount());

                merchantrecipe1.increaseUses(-Math.max(k, 1));
            }
        }

    }

    private void et() {
        Iterator iterator = this.getOffers().iterator();

        while (iterator.hasNext()) {
            MerchantRecipe merchantrecipe = (MerchantRecipe) iterator.next();

            merchantrecipe.k();
        }

    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntityVillager.bC, new VillagerData(VillagerType.c, VillagerProfession.NONE, 1));
    }

    @Override
    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.set("VillagerData", (NBTBase) this.getVillagerData().a(DynamicOpsNBT.a));
        nbttagcompound.setByte("FoodLevel", this.bL);
        nbttagcompound.set("Gossips", (NBTBase) this.bM.a((DynamicOps) DynamicOpsNBT.a).getValue());
        nbttagcompound.setInt("Xp", this.bO);
        nbttagcompound.setLong("LastRestock", this.bP);
        if (this.bH != null) {
            nbttagcompound.a("BuddyGolem", this.bH);
        }

    }

    @Override
    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        if (nbttagcompound.hasKeyOfType("VillagerData", 10)) {
            this.setVillagerData(new VillagerData(new Dynamic(DynamicOpsNBT.a, nbttagcompound.get("VillagerData"))));
        }

        if (nbttagcompound.hasKeyOfType("Offers", 10)) {
            this.trades = new MerchantRecipeList(nbttagcompound.getCompound("Offers"));
        }

        if (nbttagcompound.hasKeyOfType("FoodLevel", 1)) {
            this.bL = nbttagcompound.getByte("FoodLevel");
        }

        NBTTagList nbttaglist = nbttagcompound.getList("Gossips", 10);

        this.bM.a(new Dynamic(DynamicOpsNBT.a, nbttaglist));
        if (nbttagcompound.hasKeyOfType("Xp", 3)) {
            this.bO = nbttagcompound.getInt("Xp");
        } else {
            int i = this.getVillagerData().getLevel();

            if (VillagerData.d(i)) {
                this.bO = VillagerData.b(i);
            }
        }

        this.bP = nbttagcompound.getLong("LastRestock");
        if (nbttagcompound.b("BuddyGolem")) {
            this.bH = nbttagcompound.a("BuddyGolem");
        }

        this.setCanPickupLoot(true);
        this.a((WorldServer) this.world);
    }

    @Override
    public boolean isTypeNotPersistent(double d0) {
        return false;
    }

    @Nullable
    @Override
    protected SoundEffect getSoundAmbient() {
        return this.isSleeping() ? null : (this.dX() ? SoundEffects.ENTITY_VILLAGER_TRADE : SoundEffects.ENTITY_VILLAGER_AMBIENT);
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ENTITY_VILLAGER_HURT;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_VILLAGER_DEATH;
    }

    public void ej() {
        SoundEffect soundeffect = this.getVillagerData().getProfession().b().d();

        if (soundeffect != null) {
            this.a(soundeffect, this.getSoundVolume(), this.cU());
        }

    }

    public void setVillagerData(VillagerData villagerdata) {
        VillagerData villagerdata1 = this.getVillagerData();

        if (villagerdata1.getProfession() != villagerdata.getProfession()) {
            this.trades = null;
        }

        this.datawatcher.set(EntityVillager.bC, villagerdata);
    }

    @Override
    public VillagerData getVillagerData() {
        return (VillagerData) this.datawatcher.get(EntityVillager.bC);
    }

    @Override
    protected void b(MerchantRecipe merchantrecipe) {
        int i = 3 + this.random.nextInt(4);

        this.bO += merchantrecipe.n();
        this.bG = this.getTrader();
        if (this.eu()) {
            this.bE = 40;
            this.bF = true;
            i += 5;
        }

        if (merchantrecipe.q()) {
            this.world.addEntity(new EntityExperienceOrb(this.world, this.locX, this.locY + 0.5D, this.locZ, i));
        }

    }

    @Override
    public void setLastDamager(@Nullable EntityLiving entityliving) {
        if (entityliving != null && this.world instanceof WorldServer) {
            ((WorldServer) this.world).a(ReputationEvent.c, (Entity) entityliving, (ReputationHandler) this);
            if (this.isAlive() && entityliving instanceof EntityHuman) {
                this.world.broadcastEntityEffect(this, (byte) 13);
            }
        }

        super.setLastDamager(entityliving);
    }

    @Override
    public void die(DamageSource damagesource) {
        this.a(MemoryModuleType.HOME);
        this.a(MemoryModuleType.JOB_SITE);
        this.a(MemoryModuleType.MEETING_POINT);
        super.die(damagesource);
    }

    public void a(MemoryModuleType<GlobalPos> memorymoduletype) {
        if (this.world instanceof WorldServer) {
            MinecraftServer minecraftserver = ((WorldServer) this.world).getMinecraftServer();

            this.br.c(memorymoduletype).ifPresent((globalpos) -> {
                WorldServer worldserver = minecraftserver.getWorldServer(globalpos.a());
                VillagePlace villageplace = worldserver.B();
                Optional<VillagePlaceType> optional = villageplace.c(globalpos.b());
                BiPredicate<EntityVillager, VillagePlaceType> bipredicate = (BiPredicate) EntityVillager.bB.get(memorymoduletype);

                if (optional.isPresent() && bipredicate.test(this, optional.get())) {
                    villageplace.b(globalpos.b());
                    PacketDebug.c(worldserver, globalpos.b());
                }

            });
        }
    }

    public boolean canBreed() {
        return this.bL + this.ew() >= 12 && this.getAge() == 0;
    }

    public void en() {
        if (this.bL < 12 && this.ew() != 0) {
            for (int i = 0; i < this.getInventory().getSize(); ++i) {
                ItemStack itemstack = this.getInventory().getItem(i);

                if (!itemstack.isEmpty()) {
                    Integer integer = (Integer) EntityVillager.bA.get(itemstack.getItem());

                    if (integer != null) {
                        int j = itemstack.getCount();

                        for (int k = j; k > 0; --k) {
                            this.bL = (byte) (this.bL + integer);
                            this.getInventory().splitStack(i, 1);
                            if (this.bL >= 12) {
                                return;
                            }
                        }
                    }
                }
            }

        }
    }

    public void s(int i) {
        this.bL = (byte) (this.bL - i);
    }

    public void b(MerchantRecipeList merchantrecipelist) {
        this.trades = merchantrecipelist;
    }

    private boolean eu() {
        int i = this.getVillagerData().getLevel();

        return VillagerData.d(i) && this.bO >= VillagerData.c(i);
    }

    public void populateTrades() {
        this.setVillagerData(this.getVillagerData().withLevel(this.getVillagerData().getLevel() + 1));
        this.ef();
    }

    @Override
    public IChatBaseComponent getScoreboardDisplayName() {
        ScoreboardTeamBase scoreboardteambase = this.getScoreboardTeam();
        IChatBaseComponent ichatbasecomponent = this.getCustomName();

        if (ichatbasecomponent != null) {
            return ScoreboardTeam.a(scoreboardteambase, ichatbasecomponent).a((chatmodifier) -> {
                chatmodifier.setChatHoverable(this.bJ()).setInsertion(this.getUniqueIDString());
            });
        } else {
            VillagerProfession villagerprofession = this.getVillagerData().getProfession();
            IChatBaseComponent ichatbasecomponent1 = (new ChatMessage(this.getEntityType().e() + '.' + IRegistry.VILLAGER_PROFESSION.getKey(villagerprofession).getKey(), new Object[0])).a((chatmodifier) -> {
                chatmodifier.setChatHoverable(this.bJ()).setInsertion(this.getUniqueIDString());
            });

            if (scoreboardteambase != null) {
                ichatbasecomponent1.a(scoreboardteambase.getColor());
            }

            return ichatbasecomponent1;
        }
    }

    @Nullable
    @Override
    public GroupDataEntity prepare(GeneratorAccess generatoraccess, DifficultyDamageScaler difficultydamagescaler, EnumMobSpawn enummobspawn, @Nullable GroupDataEntity groupdataentity, @Nullable NBTTagCompound nbttagcompound) {
        if (enummobspawn == EnumMobSpawn.BREEDING) {
            this.setVillagerData(this.getVillagerData().withProfession(VillagerProfession.NONE));
        }

        if (enummobspawn == EnumMobSpawn.COMMAND || enummobspawn == EnumMobSpawn.SPAWN_EGG || enummobspawn == EnumMobSpawn.SPAWNER) {
            this.setVillagerData(this.getVillagerData().withType(VillagerType.a(generatoraccess.getBiome(new BlockPosition(this)))));
        }

        return super.prepare(generatoraccess, difficultydamagescaler, enummobspawn, groupdataentity, nbttagcompound);
    }

    @Override
    public EntityVillager createChild(EntityAgeable entityageable) {
        double d0 = this.random.nextDouble();
        VillagerType villagertype;

        if (d0 < 0.5D) {
            villagertype = VillagerType.a(this.world.getBiome(new BlockPosition(this)));
        } else if (d0 < 0.75D) {
            villagertype = this.getVillagerData().getType();
        } else {
            villagertype = ((EntityVillager) entityageable).getVillagerData().getType();
        }

        EntityVillager entityvillager = new EntityVillager(EntityTypes.VILLAGER, this.world, villagertype);

        entityvillager.prepare(this.world, this.world.getDamageScaler(new BlockPosition(entityvillager)), EnumMobSpawn.BREEDING, (GroupDataEntity) null, (NBTTagCompound) null);
        return entityvillager;
    }

    @Override
    public void onLightningStrike(EntityLightning entitylightning) {
        EntityWitch entitywitch = (EntityWitch) EntityTypes.WITCH.a(this.world);

        entitywitch.setPositionRotation(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
        entitywitch.prepare(this.world, this.world.getDamageScaler(new BlockPosition(entitywitch)), EnumMobSpawn.CONVERSION, (GroupDataEntity) null, (NBTTagCompound) null);
        entitywitch.setNoAI(this.isNoAI());
        if (this.hasCustomName()) {
            entitywitch.setCustomName(this.getCustomName());
            entitywitch.setCustomNameVisible(this.getCustomNameVisible());
        }

        this.world.addEntity(entitywitch);
        this.die();
    }

    @Override
    protected void a(EntityItem entityitem) {
        ItemStack itemstack = entityitem.getItemStack();
        Item item = itemstack.getItem();
        VillagerProfession villagerprofession = this.getVillagerData().getProfession();

        if (EntityVillager.bD.contains(item) || villagerprofession.c().contains(item)) {
            if (villagerprofession == VillagerProfession.FARMER && item == Items.WHEAT) {
                int i = itemstack.getCount() / 3;

                if (i > 0) {
                    ItemStack itemstack1 = this.getInventory().a(new ItemStack(Items.BREAD, i));

                    itemstack.subtract(i * 3);
                    if (!itemstack1.isEmpty()) {
                        this.a(itemstack1, 0.5F);
                    }
                }
            }

            ItemStack itemstack2 = this.getInventory().a(itemstack);

            if (itemstack2.isEmpty()) {
                entityitem.die();
            } else {
                itemstack.setCount(itemstack2.getCount());
            }
        }

    }

    public boolean eo() {
        return this.ew() >= 24;
    }

    public boolean ep() {
        boolean flag = this.getVillagerData().getProfession() == VillagerProfession.FARMER;
        int i = this.ew();

        return flag ? i < 60 : i < 12;
    }

    private int ew() {
        InventorySubcontainer inventorysubcontainer = this.getInventory();

        return EntityVillager.bA.entrySet().stream().mapToInt((entry) -> {
            return inventorysubcontainer.a((Item) entry.getKey()) * (Integer) entry.getValue();
        }).sum();
    }

    public boolean eq() {
        InventorySubcontainer inventorysubcontainer = this.getInventory();

        return inventorysubcontainer.a((Set) ImmutableSet.of(Items.WHEAT_SEEDS, Items.POTATO, Items.CARROT, Items.BEETROOT_SEEDS));
    }

    @Override
    protected void ef() {
        VillagerData villagerdata = this.getVillagerData();
        Int2ObjectMap<VillagerTrades.IMerchantRecipeOption[]> int2objectmap = (Int2ObjectMap) VillagerTrades.a.get(villagerdata.getProfession());

        if (int2objectmap != null && !int2objectmap.isEmpty()) {
            VillagerTrades.IMerchantRecipeOption[] avillagertrades_imerchantrecipeoption = (VillagerTrades.IMerchantRecipeOption[]) int2objectmap.get(villagerdata.getLevel());

            if (avillagertrades_imerchantrecipeoption != null) {
                MerchantRecipeList merchantrecipelist = this.getOffers();

                this.a(merchantrecipelist, avillagertrades_imerchantrecipeoption, 2);
            }
        }
    }

    public void a(EntityVillager entityvillager, long i) {
        if ((i < this.bN || i >= this.bN + 1200L) && (i < entityvillager.bN || i >= entityvillager.bN + 1200L)) {
            boolean flag = this.a(i);

            if (this.a((Entity) this) || flag) {
                this.bM.a(this.getUniqueID(), ReputationType.GOLEM, ReputationType.GOLEM.i);
            }

            this.bM.a(entityvillager.bM, this.random, 10);
            this.bN = i;
            entityvillager.bN = i;
            if (flag) {
                this.ex();
            }

        }
    }

    private void ex() {
        VillagerData villagerdata = this.getVillagerData();

        if (villagerdata.getProfession() != VillagerProfession.NONE && villagerdata.getProfession() != VillagerProfession.NITWIT) {
            Optional<EntityVillager.a> optional = this.getBehaviorController().c(MemoryModuleType.GOLEM_SPAWN_CONDITIONS);

            if (optional.isPresent()) {
                if (((EntityVillager.a) optional.get()).c(this.world.getTime())) {
                    boolean flag = this.bM.a(ReputationType.GOLEM, (d0) -> {
                        return d0 > 30.0D;
                    }) >= 5L;

                    if (flag) {
                        AxisAlignedBB axisalignedbb = this.getBoundingBox().b(80.0D, 80.0D, 80.0D);
                        List<EntityVillager> list = (List) this.world.a(EntityVillager.class, axisalignedbb, this::a).stream().limit(5L).collect(Collectors.toList());
                        boolean flag1 = list.size() >= 5;

                        if (flag1) {
                            EntityIronGolem entityirongolem = this.ey();

                            if (entityirongolem != null) {
                                UUID uuid = entityirongolem.getUniqueID();

                                EntityVillager entityvillager;

                                for (Iterator iterator = list.iterator(); iterator.hasNext(); entityvillager.bH = uuid) {
                                    entityvillager = (EntityVillager) iterator.next();
                                    Iterator iterator1 = list.iterator();

                                    while (iterator1.hasNext()) {
                                        EntityVillager entityvillager1 = (EntityVillager) iterator1.next();

                                        entityvillager.bM.a(entityvillager1.getUniqueID(), ReputationType.GOLEM, -ReputationType.GOLEM.i);
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
    }

    private boolean a(Entity entity) {
        return this.bM.a(entity.getUniqueID(), (reputationtype) -> {
            return reputationtype == ReputationType.GOLEM;
        }) > 30;
    }

    private boolean a(long i) {
        if (this.bH == null) {
            return true;
        } else {
            if (this.bI < i + 1200L) {
                this.bI = i + 1200L;
                Entity entity = ((WorldServer) this.world).getEntity(this.bH);

                if (entity == null || !entity.isAlive() || this.h(entity) > 6400.0D) {
                    this.bH = null;
                    return true;
                }
            }

            return false;
        }
    }

    @Nullable
    private EntityIronGolem ey() {
        BlockPosition blockposition = new BlockPosition(this);

        for (int i = 0; i < 10; ++i) {
            BlockPosition blockposition1 = blockposition.b(this.world.random.nextInt(16) - 8, this.world.random.nextInt(6) - 3, this.world.random.nextInt(16) - 8);
            EntityIronGolem entityirongolem = (EntityIronGolem) EntityTypes.IRON_GOLEM.b(this.world, (NBTTagCompound) null, (IChatBaseComponent) null, (EntityHuman) null, blockposition1, EnumMobSpawn.MOB_SUMMONED, false, false);

            if (entityirongolem != null) {
                if (entityirongolem.a((GeneratorAccess) this.world, EnumMobSpawn.MOB_SUMMONED) && entityirongolem.a((IWorldReader) this.world)) {
                    this.world.addEntity(entityirongolem);
                    return entityirongolem;
                }

                entityirongolem.die();
            }
        }

        return null;
    }

    @Override
    public void a(ReputationEvent reputationevent, Entity entity) {
        if (reputationevent == ReputationEvent.a) {
            this.bM.a(entity.getUniqueID(), ReputationType.MAJOR_POSITIVE, 25);
        } else if (reputationevent == ReputationEvent.e) {
            this.bM.a(entity.getUniqueID(), ReputationType.TRADING, 2);
        } else if (reputationevent == ReputationEvent.c) {
            this.bM.a(entity.getUniqueID(), ReputationType.MINOR_NEGATIVE, 25);
        } else if (reputationevent == ReputationEvent.d) {
            this.bM.a(entity.getUniqueID(), ReputationType.MAJOR_NEGATIVE, 25);
        }

    }

    @Override
    public int dV() {
        return this.bO;
    }

    public void t(int i) {
        this.bO = i;
    }

    public long er() {
        return this.bP;
    }

    @Override
    protected void K() {
        super.K();
        PacketDebug.a(this);
    }

    @Override
    public void e(BlockPosition blockposition) {
        super.e(blockposition);
        EntityVillager.a entityvillager_a = (EntityVillager.a) this.getBehaviorController().c(MemoryModuleType.GOLEM_SPAWN_CONDITIONS).orElseGet(EntityVillager.a::new);

        entityvillager_a.b(this.world.getTime());
        this.br.a(MemoryModuleType.GOLEM_SPAWN_CONDITIONS, (Object) entityvillager_a);
    }

    public static final class a {

        private long a;
        private long b;

        public a() {}

        public void a(long i) {
            this.a = i;
        }

        public void b(long i) {
            this.b = i;
        }

        private boolean c(long i) {
            return i - this.b < 24000L && i - this.a < 36000L;
        }
    }
}

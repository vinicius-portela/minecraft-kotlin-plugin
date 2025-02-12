package net.minecraft.server;

import com.mojang.datafixers.DataFixer;
import it.unimi.dsi.fastutil.longs.Long2ByteMap;
import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class VillagePlace extends RegionFileSection<VillagePlaceSection> {

    private final VillagePlace.a a = new VillagePlace.a();

    public VillagePlace(File file, DataFixer datafixer) {
        super(file, VillagePlaceSection::new, VillagePlaceSection::new, datafixer, DataFixTypes.POI_CHUNK);
    }

    public void a(BlockPosition blockposition, VillagePlaceType villageplacetype) {
        ((VillagePlaceSection) this.e(SectionPosition.a(blockposition).v())).a(blockposition, villageplacetype);
    }

    public void a(BlockPosition blockposition) {
        ((VillagePlaceSection) this.e(SectionPosition.a(blockposition).v())).a(blockposition);
    }

    public long a(Predicate<VillagePlaceType> predicate, BlockPosition blockposition, int i, VillagePlace.Occupancy villageplace_occupancy) {
        return this.b(predicate, blockposition, i, villageplace_occupancy).count();
    }

    private Stream<VillagePlaceRecord> b(Predicate<VillagePlaceType> predicate, BlockPosition blockposition, int i, VillagePlace.Occupancy villageplace_occupancy) {
        int j = i * i;

        return ChunkCoordIntPair.a(new ChunkCoordIntPair(blockposition), Math.floorDiv(i, 16)).flatMap((chunkcoordintpair) -> {
            return this.a(predicate, chunkcoordintpair, villageplace_occupancy).filter((villageplacerecord) -> {
                return villageplacerecord.f().m(blockposition) <= (double) j;
            });
        });
    }

    public Stream<VillagePlaceRecord> a(Predicate<VillagePlaceType> predicate, ChunkCoordIntPair chunkcoordintpair, VillagePlace.Occupancy villageplace_occupancy) {
        return IntStream.range(0, 16).boxed().flatMap((integer) -> {
            return this.a(predicate, SectionPosition.a(chunkcoordintpair, integer).v(), villageplace_occupancy);
        });
    }

    private Stream<VillagePlaceRecord> a(Predicate<VillagePlaceType> predicate, long i, VillagePlace.Occupancy villageplace_occupancy) {
        return (Stream) this.d(i).map((villageplacesection) -> {
            return villageplacesection.a(predicate, villageplace_occupancy);
        }).orElseGet(Stream::empty);
    }

    public Optional<BlockPosition> a(Predicate<VillagePlaceType> predicate, Predicate<BlockPosition> predicate1, BlockPosition blockposition, int i, VillagePlace.Occupancy villageplace_occupancy) {
        return this.b(predicate, blockposition, i, villageplace_occupancy).map(VillagePlaceRecord::f).filter(predicate1).findFirst();
    }

    public Optional<BlockPosition> b(Predicate<VillagePlaceType> predicate, Predicate<BlockPosition> predicate1, BlockPosition blockposition, int i, VillagePlace.Occupancy villageplace_occupancy) {
        return this.b(predicate, blockposition, i, villageplace_occupancy).map(VillagePlaceRecord::f).sorted(Comparator.comparingDouble((blockposition1) -> {
            return blockposition1.m(blockposition);
        })).filter(predicate1).findFirst();
    }

    public Optional<BlockPosition> a(Predicate<VillagePlaceType> predicate, Predicate<BlockPosition> predicate1, BlockPosition blockposition, int i) {
        return this.b(predicate, blockposition, i, VillagePlace.Occupancy.HAS_SPACE).filter((villageplacerecord) -> {
            return predicate1.test(villageplacerecord.f());
        }).findFirst().map((villageplacerecord) -> {
            villageplacerecord.b();
            return villageplacerecord.f();
        });
    }

    public Optional<BlockPosition> b(Predicate<VillagePlaceType> predicate, Predicate<BlockPosition> predicate1, BlockPosition blockposition, int i) {
        return this.b(predicate, blockposition, i, VillagePlace.Occupancy.HAS_SPACE).sorted(Comparator.comparingDouble((villageplacerecord) -> {
            return villageplacerecord.f().m(blockposition);
        })).filter((villageplacerecord) -> {
            return predicate1.test(villageplacerecord.f());
        }).findFirst().map((villageplacerecord) -> {
            villageplacerecord.b();
            return villageplacerecord.f();
        });
    }

    public Optional<BlockPosition> a(Predicate<VillagePlaceType> predicate, Predicate<BlockPosition> predicate1, VillagePlace.Occupancy villageplace_occupancy, BlockPosition blockposition, int i, Random random) {
        List<VillagePlaceRecord> list = (List) this.b(predicate, blockposition, i, villageplace_occupancy).collect(Collectors.toList());

        Collections.shuffle(list, random);
        return list.stream().filter((villageplacerecord) -> {
            return predicate1.test(villageplacerecord.f());
        }).findFirst().map(VillagePlaceRecord::f);
    }

    public boolean b(BlockPosition blockposition) {
        return ((VillagePlaceSection) this.e(SectionPosition.a(blockposition).v())).c(blockposition);
    }

    public boolean a(BlockPosition blockposition, Predicate<VillagePlaceType> predicate) {
        return (Boolean) this.d(SectionPosition.a(blockposition).v()).map((villageplacesection) -> {
            return villageplacesection.a(blockposition, predicate);
        }).orElse(false);
    }

    public Optional<VillagePlaceType> c(BlockPosition blockposition) {
        VillagePlaceSection villageplacesection = (VillagePlaceSection) this.e(SectionPosition.a(blockposition).v());

        return villageplacesection.d(blockposition);
    }

    public int a(SectionPosition sectionposition) {
        this.a.a();
        return this.a.c(sectionposition.v());
    }

    private boolean f(long i) {
        return this.a(VillagePlaceType.a, i, VillagePlace.Occupancy.IS_OCCUPIED).count() > 0L;
    }

    @Override
    public void a(BooleanSupplier booleansupplier) {
        super.a(booleansupplier);
        this.a.a();
    }

    @Override
    protected void a(long i) {
        super.a(i);
        this.a.b(i, this.a.b(i), false);
    }

    @Override
    protected void b(long i) {
        this.a.b(i, this.a.b(i), false);
    }

    public void a(ChunkCoordIntPair chunkcoordintpair, ChunkSection chunksection) {
        SectionPosition sectionposition = SectionPosition.a(chunkcoordintpair, chunksection.getYPosition() >> 4);

        SystemUtils.a(this.d(sectionposition.v()), (villageplacesection) -> {
            villageplacesection.a((biconsumer) -> {
                if (a(chunksection)) {
                    this.a(chunksection, sectionposition, biconsumer);
                }

            });
        }, () -> {
            if (a(chunksection)) {
                VillagePlaceSection villageplacesection = (VillagePlaceSection) this.e(sectionposition.v());

                this.a(chunksection, sectionposition, villageplacesection::a);
            }

        });
    }

    private static boolean a(ChunkSection chunksection) {
        Stream stream = VillagePlaceType.e();

        chunksection.getClass();
        return stream.anyMatch(chunksection::a);
    }

    private void a(ChunkSection chunksection, SectionPosition sectionposition, BiConsumer<BlockPosition, VillagePlaceType> biconsumer) {
        sectionposition.w().forEach((blockposition) -> {
            IBlockData iblockdata = chunksection.getType(SectionPosition.b(blockposition.getX()), SectionPosition.b(blockposition.getY()), SectionPosition.b(blockposition.getZ()));

            VillagePlaceType.b(iblockdata).ifPresent((villageplacetype) -> {
                biconsumer.accept(blockposition, villageplacetype);
            });
        });
    }

    final class a extends LightEngineGraphSection {

        private final Long2ByteMap b = new Long2ByteOpenHashMap();

        protected a() {
            super(7, 16, 256);
            this.b.defaultReturnValue((byte) 7);
        }

        @Override
        protected int b(long i) {
            return VillagePlace.this.f(i) ? 0 : 7;
        }

        @Override
        protected int c(long i) {
            return this.b.get(i);
        }

        @Override
        protected void a(long i, int j) {
            if (j > 6) {
                this.b.remove(i);
            } else {
                this.b.put(i, (byte) j);
            }

        }

        public void a() {
            super.b(Integer.MAX_VALUE);
        }
    }

    public static enum Occupancy {

        HAS_SPACE(VillagePlaceRecord::d), IS_OCCUPIED(VillagePlaceRecord::e), ANY((villageplacerecord) -> {
            return true;
        });

        private final Predicate<? super VillagePlaceRecord> d;

        private Occupancy(Predicate predicate) {
            this.d = predicate;
        }

        public Predicate<? super VillagePlaceRecord> a() {
            return this.d;
        }
    }
}

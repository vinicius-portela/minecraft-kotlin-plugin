package net.minecraft.server;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;

public abstract class LightEngineLayer<M extends LightEngineStorageArray<M>, S extends LightEngineStorage<M>> extends LightEngineGraph implements LightEngineLayerEventListener {

    private static final EnumDirection[] d = EnumDirection.values();
    protected final ILightAccess a;
    protected final EnumSkyBlock b;
    protected final S c;
    private boolean e;
    private final BlockPosition.MutableBlockPosition f = new BlockPosition.MutableBlockPosition();
    private final long[] g = new long[2];
    private final IBlockAccess[] h = new IBlockAccess[2];

    public LightEngineLayer(ILightAccess ilightaccess, EnumSkyBlock enumskyblock, S s0) {
        super(16, 256, 8192);
        this.a = ilightaccess;
        this.b = enumskyblock;
        this.c = s0;
        this.c();
    }

    @Override
    protected void f(long i) {
        this.c.c();
        if (this.c.g(SectionPosition.e(i))) {
            super.f(i);
        }

    }

    @Nullable
    private IBlockAccess a(int i, int j) {
        long k = ChunkCoordIntPair.pair(i, j);

        for (int l = 0; l < 2; ++l) {
            if (k == this.g[l]) {
                return this.h[l];
            }
        }

        IBlockAccess iblockaccess = this.a.b(i, j);

        for (int i1 = 1; i1 > 0; --i1) {
            this.g[i1] = this.g[i1 - 1];
            this.h[i1] = this.h[i1 - 1];
        }

        this.g[0] = k;
        this.h[0] = iblockaccess;
        return iblockaccess;
    }

    private void c() {
        Arrays.fill(this.g, ChunkCoordIntPair.a);
        Arrays.fill(this.h, (Object) null);
    }

    protected VoxelShape a(long i, @Nullable AtomicInteger atomicinteger) {
        if (i == Long.MAX_VALUE) {
            if (atomicinteger != null) {
                atomicinteger.set(0);
            }

            return VoxelShapes.a();
        } else {
            int j = SectionPosition.a(BlockPosition.b(i));
            int k = SectionPosition.a(BlockPosition.d(i));
            IBlockAccess iblockaccess = this.a(j, k);

            if (iblockaccess == null) {
                if (atomicinteger != null) {
                    atomicinteger.set(16);
                }

                return VoxelShapes.b();
            } else {
                this.f.g(i);
                IBlockData iblockdata = iblockaccess.getType(this.f);
                boolean flag = iblockdata.o() && iblockdata.g();

                if (atomicinteger != null) {
                    atomicinteger.set(iblockdata.b(this.a.getWorld(), (BlockPosition) this.f));
                }

                return flag ? iblockdata.j(this.a.getWorld(), this.f) : VoxelShapes.a();
            }
        }
    }

    public static int a(IBlockAccess iblockaccess, IBlockData iblockdata, BlockPosition blockposition, IBlockData iblockdata1, BlockPosition blockposition1, EnumDirection enumdirection, int i) {
        boolean flag = iblockdata.o() && iblockdata.g();
        boolean flag1 = iblockdata1.o() && iblockdata1.g();

        if (!flag && !flag1) {
            return i;
        } else {
            VoxelShape voxelshape = flag ? iblockdata.j(iblockaccess, blockposition) : VoxelShapes.a();
            VoxelShape voxelshape1 = flag1 ? iblockdata1.j(iblockaccess, blockposition1) : VoxelShapes.a();

            return VoxelShapes.b(voxelshape, voxelshape1, enumdirection) ? 16 : i;
        }
    }

    @Override
    protected boolean a(long i) {
        return i == Long.MAX_VALUE;
    }

    @Override
    protected int a(long i, long j, int k) {
        return 0;
    }

    @Override
    protected int c(long i) {
        return i == Long.MAX_VALUE ? 0 : 15 - this.c.h(i);
    }

    protected int a(NibbleArray nibblearray, long i) {
        return 15 - nibblearray.a(SectionPosition.b(BlockPosition.b(i)), SectionPosition.b(BlockPosition.c(i)), SectionPosition.b(BlockPosition.d(i)));
    }

    @Override
    protected void a(long i, int j) {
        this.c.b(i, Math.min(15, 15 - j));
    }

    @Override
    protected int b(long i, long j, int k) {
        return 0;
    }

    public boolean a() {
        return this.b() || this.c.b() || this.c.a();
    }

    public int a(int i, boolean flag, boolean flag1) {
        if (!this.e) {
            if (this.c.b()) {
                i = this.c.b(i);
                if (i == 0) {
                    return i;
                }
            }

            this.c.a(this, flag, flag1);
        }

        this.e = true;
        if (this.b()) {
            i = this.b(i);
            this.c();
            if (i == 0) {
                return i;
            }
        }

        this.e = false;
        this.c.d();
        return i;
    }

    protected void a(long i, NibbleArray nibblearray) {
        this.c.a(i, nibblearray);
    }

    @Nullable
    @Override
    public NibbleArray a(SectionPosition sectionposition) {
        return this.c.a(sectionposition.v(), false);
    }

    @Override
    public int b(BlockPosition blockposition) {
        return this.c.d(blockposition.asLong());
    }

    public void a(BlockPosition blockposition) {
        long i = blockposition.asLong();

        this.f(i);
        EnumDirection[] aenumdirection = LightEngineLayer.d;
        int j = aenumdirection.length;

        for (int k = 0; k < j; ++k) {
            EnumDirection enumdirection = aenumdirection[k];

            this.f(BlockPosition.a(i, enumdirection));
        }

    }

    public void a(BlockPosition blockposition, int i) {}

    @Override
    public void a(SectionPosition sectionposition, boolean flag) {
        this.c.c(sectionposition.v(), flag);
    }

    public void a(ChunkCoordIntPair chunkcoordintpair, boolean flag) {
        long i = SectionPosition.f(SectionPosition.b(chunkcoordintpair.x, 0, chunkcoordintpair.z));

        this.c.c();
        this.c.b(i, flag);
    }
}

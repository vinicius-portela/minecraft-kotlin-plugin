package net.minecraft.server;

import com.google.common.collect.Queues;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BooleanSupplier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class IAsyncTaskHandler<R extends Runnable> implements Mailbox<R>, Executor {

    private final String b;
    private static final Logger LOGGER = LogManager.getLogger();
    private final Queue<R> d = Queues.newConcurrentLinkedQueue();
    private int e;

    protected IAsyncTaskHandler(String s) {
        this.b = s;
    }

    protected abstract R postToMainThread(Runnable runnable);

    protected abstract boolean canExecute(R r0);

    public boolean isMainThread() {
        return Thread.currentThread() == this.getThread();
    }

    protected abstract Thread getThread();

    protected boolean isNotMainThread() {
        return !this.isMainThread();
    }

    @Override
    public String bd() {
        return this.b;
    }

    private CompletableFuture<Object> executeFuture(Runnable runnable) {
        return CompletableFuture.supplyAsync(() -> {
            runnable.run();
            return null;
        }, this);
    }

    public void executeSync(Runnable runnable) {
        if (!this.isMainThread()) {
            this.executeFuture(runnable).join();
        } else {
            runnable.run();
        }

    }

    public void a(R r0) {
        this.d.add(r0);
        LockSupport.unpark(this.getThread());
    }

    public void execute(Runnable runnable) {
        if (this.isNotMainThread()) {
            this.a(this.postToMainThread(runnable));
        } else {
            runnable.run();
        }

    }

    protected void executeAll() {
        while (this.executeNext()) {
            ;
        }

    }

    protected boolean executeNext() {
        R r0 = (Runnable) this.d.peek();

        if (r0 == null) {
            return false;
        } else if (this.e == 0 && !this.canExecute(r0)) {
            return false;
        } else {
            this.executeTask((Runnable) this.d.remove());
            return true;
        }
    }

    public void awaitTasks(BooleanSupplier booleansupplier) {
        ++this.e;

        try {
            while (!booleansupplier.getAsBoolean()) {
                if (!this.executeNext()) {
                    LockSupport.parkNanos("waiting for tasks", 1000L);
                }
            }
        } finally {
            --this.e;
        }

    }

    protected void executeTask(R r0) {
        try {
            r0.run();
        } catch (Exception exception) {
            IAsyncTaskHandler.LOGGER.fatal("Error executing task on {}", this.bd(), exception);
        }

    }
}

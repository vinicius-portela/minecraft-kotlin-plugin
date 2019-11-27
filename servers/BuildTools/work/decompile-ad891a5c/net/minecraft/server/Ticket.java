package net.minecraft.server;

import java.util.Objects;

final class Ticket<T> implements Comparable<Ticket<?>> {

    private final TicketType<T> a;
    private final int b;
    private final T c;
    private final long d;

    Ticket(TicketType<T> tickettype, int i, T t0, long j) {
        this.a = tickettype;
        this.b = i;
        this.c = t0;
        this.d = j;
    }

    public int compareTo(Ticket<?> ticket) {
        int i = Integer.compare(this.b, ticket.b);

        if (i != 0) {
            return i;
        } else {
            int j = Integer.compare(System.identityHashCode(this.a), System.identityHashCode(ticket.a));

            return j != 0 ? j : this.a.a().compare(this.c, ticket.c);
        }
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof Ticket)) {
            return false;
        } else {
            Ticket<?> ticket = (Ticket) object;

            return this.b == ticket.b && Objects.equals(this.a, ticket.a) && Objects.equals(this.c, ticket.c);
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.a, this.b, this.c});
    }

    public String toString() {
        return "Ticket[" + this.a + " " + this.b + " (" + this.c + ")] at " + this.d;
    }

    public TicketType<T> getTicketType() {
        return this.a;
    }

    public int b() {
        return this.b;
    }

    public long getCreationTick() {
        return this.d;
    }
}

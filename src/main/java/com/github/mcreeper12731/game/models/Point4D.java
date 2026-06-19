package com.github.mcreeper12731.game.models;

import java.util.Objects;

public record Point4D(int l, int t, int x, int y) {

    public Point4D add(int l, int t, int x, int y) {
        return new Point4D(
                this.l + l,
                this.t + t,
                this.x + x,
                this.y + y
        );
    }

    public Point4D add(Point4D other) {
        return add(other.l, other.t, other.x, other.y);
    }

    public Point4D multiply(int n) {
        return new Point4D(this.l * n, this.t * n, this.x * n, this.y * n);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Point4D(int other_l, int other_t, int other_x, int other_y))) return false;

        return
                this.l == other_l &&
                this.t == other_t &&
                this.x == other_x &&
                this.y == other_y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.l, this.t, this.x, this.y);
    }

    public String toLongString() {
        return String.format("(%dT%d) %s%d", this.l, this.t, (char)('a' + this.x), this.y + 1);
    }

    @Override
    public String toString() {
        return String.format("(%d,%d;%d,%d)", this.l, this.t, this.x, this.y);
    }
}

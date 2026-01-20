package com.github.mcreeper12731.game.models;

import java.util.Objects;

public record Point4D(double timeline, int time, int x, int y) {

    public Point4D add(double timeline, int time, int x, int y) {
        return new Point4D(
                this.timeline + timeline,
                this.time + time,
                this.x + x,
                this.y + y
        );
    }

    public Point4D addTimeline(double timeline) {
        return add(timeline, 0, 0, 0);
    }

    public Point4D addTime(int time) {
        return add(0, time, 0, 0);
    }

    public Point4D addX(int x) {
        return add(0, 0, x, 0);
    }

    public Point4D addY(int y) {
        return add(0, 0,0, y);
    }

    public Point4D add(Point4D point) {
        return add(point.timeline, point.time, point.x, point.y);
    }

    public Point4D addCoordinate(int coordinateNumber, double value) {
        return switch (coordinateNumber) {
            case 0 -> addTimeline(value);
            case 1 -> addTime((int)value);
            case 2 -> addX((int)value);
            case 3 -> addY((int)value);
            default -> throw new RuntimeException("Coordinate number does not exist!");
        };
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Point4D other)) return false;

        return
                this.timeline == other.timeline &&
                this.time == other.time &&
                this.x == other.x &&
                this.y == other.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeline, time, x, y);
    }

    @Override
    public String toString() {
        return String.format("(%.0fT%d) %s%d", timeline, time, (char)('A' + x), y + 1);
    }
}

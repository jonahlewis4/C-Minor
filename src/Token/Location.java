package Token;

public class Location {

    public Position start;
    public Position end;

    public Location() {
        this.start = new Position(1,0);
        this.end = new Position(1,1);
    }

    public Location(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    public void addLine()   { end.addToLine(); }
    public void addCol()    { end.addToCol(); }

    public void resetStart() {
        start.line = end.line;
        start.column = end.column;
    }

    public Location copy() {
        Location loc = new Location(start.copy(), end.copy());
        this.start.addToCol();
        return loc;
    }

    @Override
    public String toString() {
        return start.toString() + " to " + end.toString();
    }
}

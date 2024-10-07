package Token;

public class Location {

    private Position start;
    private Position end;

    public Location() {
        this.start = new Position(1,1);
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

    public Location copy() { return new Location(start.copy(), end.copy()); }

    @Override
    public String toString() {
        return start.toString() + " to " + end.toString();
    }
}

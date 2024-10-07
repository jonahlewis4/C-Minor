package Token;

/*
                        Position
                   -------------------
This class keeps track of a position.
________________________________________________________
*/
public class Position implements Comparable<Position> {

    public int line;
    public int column;

    public Position() { line = column = 1; }

    public Position(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public void addToLine() {
        line += 1;
        column = 1;
    }

    public void addToCol() { column += 1; }

    public Position copy() { return new Position(line, column); }

    @Override
    public String toString() { return this.line + "." + this.column; }

    @Override
    public int compareTo(Position other) {
        return this.equals(other) ? 1 : 0;
    }

    public boolean equals(Position other) {
        if(this == other) return true;
        return this.line == other.line && this.column == other.column;
    }
}

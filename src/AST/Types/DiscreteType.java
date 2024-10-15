package AST.Types;

import AST.*;
import Token.*;
import Utilities.PokeVisitor;

// Leaf Node
public class DiscreteType extends Type {

    public static enum Discretes { INT, CHAR, BOOL };
    public static String[] names = { "Int", "Char", "Bool" };

    Discretes dType;

    public DiscreteType(Token t, Discretes d) {
        super(t);
        this.dType = d;
    }

    public boolean isDiscreteType() { return true; }

    public String typeName() { return names[dType.ordinal()]; }

    @Override
    public String toString() { return names[dType.ordinal()]; };

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsDiscreteType(this); }
}

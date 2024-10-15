package AST.Types;

import AST.*;
import Token.*;
import Utilities.PokeVisitor;

// Leaf Node
public class ScalarType extends Type {

    public static enum Scalars { STR, TEXT, REAL };
    public static String[] names = { "String", "Text", "Real" };

    Scalars sType;

    public ScalarType(Token t, Scalars s) {
        super(t);
        this.sType = s;
    }

    public boolean isScalarType() { return true; }

    public String typeName() { return names[sType.ordinal()]; };

    @Override
    public String toString() { return names[sType.ordinal()]; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsScalarType(this); }
}

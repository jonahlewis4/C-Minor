package AST;

import AST.*;
import Token.*;
import Utilities.PokeVisitor;

// Leaf Node
public class Modifier extends AST {

    public static enum Mods { ABSTR, FINAL, PROPERTY, PROTECTED, PUBLIC, PURE, RECURS, IN, OUT, INOUT, REF }
    public static String[] names = {"Abstract", "Final", "Property", "Protected", "Public", "Pure",
                                    "Recursive", "In", "Out", "Inout", "Ref" };

    private Mods myMod;

    public Modifier(Token t, Mods m) {
        super(t);
        this.myMod = m;
    }

    public Mods getModifier() { return myMod; }

    @Override
    public String toString() { return names[myMod.ordinal()]; }

    @Override
    public AST whosThatNode(PokeVisitor v){ return v.itsModifier(this); }
}

package AST;

import Utilities.PokeVisitor;

public class Compilation extends AST {
    //enums globals classes functions main

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsCompilation(this); }
}

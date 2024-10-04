package AST;

import Utilities.PokeVisitor;

public class Var extends AST {

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsVar(this); }
}

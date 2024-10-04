package AST.TopLevelDecls;

import AST.*;
import Utilities.PokeVisitor;

public class ConstantDecl extends TopLevelDecl {
    public ConstantDecl(Token t) {
        super(t);
    }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsConstantDecl(this); }
}

package AST.TopLevelDecls;

import AST.AST;
import Utilities.PokeVisitor;

public class ClassDecl extends TopLevelDecl {

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsClassDecl(this); }
}

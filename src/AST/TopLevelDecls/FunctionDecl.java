package AST.TopLevelDecls;

import AST.AST;
import Utilities.PokeVisitor;

public class FunctionDecl extends TopLevelDecl {

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsFunctionDecl(this); }
}

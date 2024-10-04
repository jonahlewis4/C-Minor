package AST.TopLevelDecls;

import AST.*;
import Utilities.PokeVisitor;

public class EnumDecl extends TopLevelDecl {

    public EnumDecl(Token t) { super(t); }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsEnumDecl(this); }
}

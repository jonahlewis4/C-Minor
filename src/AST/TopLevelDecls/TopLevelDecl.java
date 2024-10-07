package AST.TopLevelDecls;

import AST.*;
import Token.*;

public abstract class TopLevelDecl extends AST {
    public TopLevelDecl() { super(); }
    public TopLevelDecl(Token t) { super(t); }
    public TopLevelDecl(AST node) { super(node); }
}

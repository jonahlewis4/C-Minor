package AST.Expressions;

import AST.*;
import AST.Types.*;
import Token.*;

public abstract class Expression extends AST {
    public Type type = null;

    public Expression() { super(); }
    public Expression(Token t) { super(t); }
    public Expression(AST node) { super(node); }
    //TODO: Constant functions?
}

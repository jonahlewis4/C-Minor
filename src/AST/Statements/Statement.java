package AST.Statements;

import AST.*;
import Token.*;

public abstract class Statement extends AST {
    public Statement() { super(); }
    public Statement (Token t) { super(t); }
    public Statement(AST node) { super(node); }
}

package AST.Operators;

import AST.*;
import Token.*;

public abstract class Operator extends AST {
    public Operator(Token t) { super(t); }

    public boolean isBinaryOp() { return false; }
    public boolean isUnaryOp() { return false; }
    public boolean isAssignOp() { return false; }
}

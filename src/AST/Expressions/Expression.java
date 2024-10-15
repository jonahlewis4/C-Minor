package AST.Expressions;

import AST.*;
import AST.Types.*;
import Token.*;

public abstract class Expression extends AST {

    public Type type;

    public Expression(Token t) { super(t); }

    public boolean isBinaryExpr() { return false; }
    public boolean isCastExpr() { return false; }
    public boolean isInvocation() { return false; }
    public boolean isLiteral() { return false; }
    public boolean isListLiteral() { return false; }
    public boolean isNameExpr() { return false; }
    public boolean isNewExpr() { return false; }
    public boolean isInStmt() { return false; }
    public boolean isOutStmt() { return false; }
    public boolean isUnaryExpr() { return false; }
}

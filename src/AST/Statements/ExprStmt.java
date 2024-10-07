package AST.Statements;

import AST.*;
import AST.Expressions.*;
import Utilities.PokeVisitor;

public class ExprStmt extends Statement {

    Expression myExpr;

    public ExprStmt(Expression myExpr) {
        this.myExpr = myExpr;

        addChild(this.myExpr);
    }

    public Expression getMyExpr() { return myExpr; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsExprStmt(this); }
}

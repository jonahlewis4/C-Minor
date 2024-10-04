package AST.Statements;

import AST.*;
import AST.Expressions.*;
import Utilities.PokeVisitor;

public class AssignStmt extends Statement {

    Expression LHS;
    Expression RHS;
    AssignOp op;

    public AssignStmt(Expression LHS, Expression RHS, AssignOp op) {
        this.LHS = LHS;
        this.RHS = RHS;
        this.op = op;

        addChild(this.LHS);
        addChild(this.RHS);
        addChild(this.op);
        setParent();
    }

    public Expression getLHS() { return this.LHS; }
    public Expression getRHS() { return this.RHS; }
    public AssignOp getOp() { return this.op; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsAssignStmt(this); }
}

package AST.Statements;

import AST.*;
import AST.Expressions.*;
import AST.Operators.*;
import Token.*;
import Utilities.PokeVisitor;

public class AssignStmt extends Statement {

    private Expression LHS;
    private Expression RHS;
    private AssignOp op;

    public AssignStmt(Token t, Expression LHS, Expression RHS, AssignOp op) {
        super(t);
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

    public boolean isAssignStmt() { return true; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsAssignStmt(this); }
}

package AST.Expressions;

import AST.*;
import AST.Types.*;
import Token.*;
import Utilities.PokeVisitor;

public class NewExpr extends Expression {

    private ClassType cType;
    private Vector<Var> fields;

    public NewExpr(Token t, ClassType ct, Vector<Var> f) {
        super(t);
        this.cType = ct;
        this.fields = f;

        addChild(this.cType);
        addChild(this.fields);
        setParent();
    }

    public ClassType getClassType() { return cType; }
    public Vector<Var> getArguments() { return fields; }

    public boolean isNewExpr() { return true; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsNewExpr(this); }
}

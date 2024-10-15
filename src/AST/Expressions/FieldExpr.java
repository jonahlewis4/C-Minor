package AST.Expressions;

import AST.*;
import Token.*;
import Utilities.PokeVisitor;

public class FieldExpr extends Expression {

    private Expression target;
    private Name name;

    private boolean asCheck;

    public FieldExpr(Token t, Expression ft, Name fn, boolean ac) {
        super(t);
        this.target = ft;
        this.name = fn;

        this.asCheck = ac;

        addChild(this.target);
        addChild(this.name);
        setParent();
    }

    public Expression getFieldTarget() { return target; }
    public Name getName() { return name; }

    public boolean isAsCheck() { return asCheck; }
    public boolean isFieldExpr() { return true; }

    @Override
    public String toString() { return name.toString(); }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsFieldExpr(this); }
}

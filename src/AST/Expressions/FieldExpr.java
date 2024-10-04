package AST.Expressions;

import AST.*;
import Utilities.PokeVisitor;

public class FieldExpr extends Expression {

    public FieldExpr(Token t, Expression fieldTarget, Name fieldName) {
        super(t);
        addChild(fieldTarget);
        addChild(fieldName);
        childCount = 2;
    }

    public Expression getFieldTarget() { return (Expression) children.get(0); }
    public Name getFieldName() { return (Name) children.get(1); }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsFieldRef(this); }
}

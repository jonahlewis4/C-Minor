package AST.Expressions;

import AST.*;
import Utilities.PokeVisitor;

public class FieldExpr extends Expression {

    Expression target;
    Name name;

    public FieldExpr(Expression fieldTarget, Name fieldName) {
        target = fieldTarget;
        name = fieldName;

        addChild(target);
        addChild(name);
    }

    public Expression getFieldTarget() { return target; }
    public String getFieldID() { return name.getName(); }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsFieldExpr(this); }
}

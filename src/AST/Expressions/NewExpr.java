package AST.Expressions;

import AST.*;
import Utilities.PokeVisitor;

// class name (type) and argument list (expression)
public class NewExpr extends Expression {

    public NewExpr(Token t, Type className, Sequence arguments) {
        super(t);
        addChild(className);
        addChild(arguments);
        childCount = 2;
    }

    public Type getClassType() { return (Type) children.get(0); }
    public Sequence getArguments() { return (Sequence) children.get(1); }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsNewExpr(this); }
}

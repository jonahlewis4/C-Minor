package AST.Expressions;

import AST.*;
import AST.Types.*;
import Utilities.PokeVisitor;

// class name (type) and argument list (expression)
public class NewExpr extends Expression {

    ClassType myType;
    Sequence args;

    public NewExpr(ClassType className, Sequence arguments) {
        super(className);
        this.myType = className;
        this.args = arguments;

        addChild(this.myType);
        addChild(this.args);
    }

    public Type getClassType() { return this.myType; }
    public Sequence getArguments() { return this.args; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsNewExpr(this); }
}

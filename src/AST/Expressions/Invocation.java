package AST.Expressions;

import AST.*;
import AST.Types.*;
import Utilities.PokeVisitor;

public class Invocation extends Expression {

    Expression target;
    Name funcName;
    Sequence params;

    public Type targetType;

    public Invocation(Name funcName, Sequence params) { this(null, funcName, params); }

    public Invocation(Expression target, Name funcName, Sequence params) {
        this.target = target;
        this.funcName = funcName;
        this.params = params;

        addChild(this.target);
        addChild(this.funcName);
        addChild(this.params);
    }

    //TODO: IMPLEMENT MORE METHODS LATER
    
    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsInvocation(this); }
}

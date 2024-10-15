package AST.Expressions;

import AST.*;
import AST.Types.*;
import Token.*;
import Utilities.PokeVisitor;

public class Invocation extends Expression {

    public Type targetType;

    private Expression target;
    private Name name;
    private Vector<Expression> args;

    public Invocation(Token t, Name fn, Vector<Expression> p) { this(t,null, fn, p); }
    public Invocation(Token t, Expression e, Vector<Expression> p) { this(t,e,null,p); }

    public Invocation(Token t, Expression e, Name fn, Vector<Expression> p) {
        super(t);
        this.target = e;
        this.name = fn;
        this.args = p;

        addChild(this.target);
        addChild(this.name);
        addChild(this.args);
        setParent();
    }

    public Expression getTargetExpression() { return target; }
    public Name getName() { return name; }
    public Vector<Expression> getArguments() { return args; }

    public boolean isInvocation() { return true; }

    @Override
    public String toString() { return name.toString(); }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsInvocation(this); }
}

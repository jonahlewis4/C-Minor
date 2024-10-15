package AST.Expressions;

import AST.*;
import Token.*;
import Utilities.PokeVisitor;

public class NameExpr extends Expression {

    private Name name;

    public NameExpr(Token t, Name n) {
        super(t);
        this.name = n;

        addChild(this.name);
        setParent();
    }

    public Name getName() { return name; }
    public boolean isNameExpr() { return true; }

    @Override
    public String toString() { return name.toString(); }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsNameExpr(this); }
}

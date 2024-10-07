package AST;

import AST.Expressions.*;
import AST.Types.*;
import Utilities.PokeVisitor;

public class Var extends AST {

    private Type type;
    private Name name;
    private Expression init;

    public Var(Name name) { this(name,null); }

    public Var(Name name, Expression init) {
        this.name = name;
        this.init = init;

        addChild(this.name);
        addChild(this.init);
    }

    public String getID() { return name.getName(); }
    public Name getNameNode() { return name; }
    public Expression getInit() { return init;}
    public Type getType() { return type; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsVar(this); }
}

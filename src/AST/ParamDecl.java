package AST;

import AST.Types.*;
import Token.*;
import Utilities.PokeVisitor;

public class ParamDecl extends AST implements VarDecl {

    private Modifier mod;
    private Name name;
    private Type type;

    public ParamDecl(Token t, Modifier m, Name n, Type type) {
        super(t);
        this.mod = m;
        this.name = n;
        this.type = type;

        addChild(this.mod);
        addChild(this.name);
        addChild(this.type);
        setParent();
    }

    public Type getType() { return type; }
    @Override
    public String toString() { return name.toString(); }

    public boolean isClassType() { return type instanceof ClassType; }
    public boolean isConstant() { return false; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsParamDecl(this); }
}

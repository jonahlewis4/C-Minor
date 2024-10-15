package AST.Statements;

import AST.*;
import AST.Types.*;
import Token.*;
import Utilities.PokeVisitor;

public class LocalDecl extends Statement implements VarDecl {

    private Var myVar;
    private Type type;

    public LocalDecl(Token t, Var v, Type type) {
        super(t);
        this.myVar = v;
        this.type = type;

        addChild(this.myVar);
        addChild(this.type);
        setParent();
    }

    public Var getVar() { return myVar; }

    public Type getType() { return type; }
    public String toString() { return myVar.toString(); }

    public boolean isClassType() { return false; }
    public boolean isConstant() { return false;}

    public boolean isLocalDecl() { return true; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsLocalDecl(this); }
}

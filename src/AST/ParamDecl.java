package AST;

import AST.Types.*;
import Utilities.PokeVisitor;

public class ParamDecl extends AST implements VarDecl {

    private Type myType;
    private Name name;

    public ParamDecl(Name name, Type myType) {
        this.name = name;
        this.myType = myType;

        addChild(this.name);
        addChild(this.myType);
    }

    public Type getType() { return myType; }
    public String getID() { return name.getName(); }

    public boolean isClassType() { return myType instanceof ClassType; }


    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsParamDecl(this); }
}

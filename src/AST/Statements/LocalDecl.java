package AST.Statements;

import AST.*;
import AST.Types.*;
import Utilities.PokeVisitor;

//TODO: More helper methods?
public class LocalDecl extends Statement implements VarDecl {

    Var myVar;

    public LocalDecl(Var myVar) {
        this.myVar = myVar;

        addChild(myVar);
    }

    public Var getMyVar() { return myVar; }
    public String getID() { return myVar.getID(); }
    public Type getType() { return myVar.getType(); }

    public boolean isClassType() { return myVar.getType() instanceof ClassType; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsLocalDecl(this); }
}

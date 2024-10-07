package AST.ClassBodyDecls;

import AST.*;
import AST.Types.*;
import Utilities.PokeVisitor;

public class FieldDecl extends ClassBodyDecl implements VarDecl {
   // data_decl : ('property' | 'protected' | 'public') variable_decl ;
    Var myVar;

    public FieldDecl(Var myVar) {
        super();
        this.myVar = myVar;

        addChild(this.myVar);
    }
    public Type getType() { return myVar.getType(); }
    public String getID() { return myVar.getID(); }
    public boolean isClassType() { return myVar.getType() instanceof ClassType; }
    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsFieldDecl(this); }
}

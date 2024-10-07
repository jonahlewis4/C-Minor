package AST.TopLevelDecls;

import AST.*;
import AST.Types.*;
import Utilities.PokeVisitor;

public class ClassDecl extends TopLevelDecl {

    //TODO: Add symbol tables for NameChecking

    Name name;
    ClassType superClass;
    Sequence Class;


// class_type : ('abstr' | 'final') 'class' name type_params? super_class? class_body ;

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsClassDecl(this); }
}

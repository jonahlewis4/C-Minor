package AST.Types;

import AST.*;
import AST.TopLevelDecls.*;
import Utilities.PokeVisitor;

// Leaf Node
public class ClassType extends Type {
    //TODO: Maybe put more members in future
    public ClassDecl myClass;

    private Name name;

    public ClassType(Name n) {
        super(n);
        name = n;
    }

    @Override
    public String typeName() { return name.getName(); }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsClassType(this); }
}

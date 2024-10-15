package AST.Types;

import AST.*;
import AST.TopLevelDecls.*;
import Token.*;
import Utilities.PokeVisitor;

public class ClassType extends Type {
    public ClassDecl myClass;

    private Name name;
    private Vector<Type> templateTypes;

    public ClassType(Token t, Name n) { this(t,n,null); }

    public ClassType(Token t, Name n, Vector<Type> tt) {
        super(t);
        this.name = n;
        this.templateTypes = tt;

        addChild(this.name);
        addChild(this.templateTypes);
        setParent();
    }

    public Name getName() { return name; }
    public Vector<Type> getTemplateTypes() { return templateTypes; }

    public String typeName() { return name.toString(); }

    @Override
    public String toString() { return name.toString(); }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsClassType(this); }
}

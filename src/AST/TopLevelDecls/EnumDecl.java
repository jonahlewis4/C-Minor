package AST.TopLevelDecls;

import AST.*;
import AST.Types.*;
import Utilities.PokeVisitor;

public class EnumDecl extends TopLevelDecl {

    private Name name;
    private Type type;
    private Vector<Var> enumFields;

    public EnumDecl(Name name, Vector<Var> ef) { this(name, null, ef); }

    public EnumDecl(Name name, Type type, Vector<Var> ef) {
        this.name = name;
        this.type = type;
        this.enumFields = ef;

        addChild(this.name);
        addChild(this.type);
        addChild(this.enumFields);
        setParent();
    }

    public Name getName() { return name; }
    public Type getType() { return type; }
    public Vector<Var> getEnumFields() { return enumFields;}

    public boolean isEnumDecl() { return true; }

    @Override
    public String toString() { return name.toString(); }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsEnumDecl(this); }
}

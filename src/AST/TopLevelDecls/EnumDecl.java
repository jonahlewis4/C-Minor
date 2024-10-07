package AST.TopLevelDecls;

import AST.*;
import AST.Types.*;
import Utilities.PokeVisitor;

public class EnumDecl extends TopLevelDecl {

    private Name name;
    private Type type;
    private Sequence enumFields;

    public EnumDecl(Name name, Type type, Sequence enumFields) {
        this.name = name;
        this.type = type;
        this.enumFields = enumFields;
    }

    public String getName() { return name.getName(); }
    public Name getNameNode() { return name; }
    public String getType() { return type.typeName(); }
    public Sequence getEnumFields() { return enumFields;}

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsEnumDecl(this); }
}

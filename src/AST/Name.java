package AST;

import Token.*;
import Utilities.PokeVisitor;

// Leaf Node
public class Name extends AST {

    private String ID;

    public Name(Token t) {
        super(t);
        ID = t.getText();
    }

    public void setName(String newName) { ID = newName; }
    public String getName() { return ID; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsName(this); }
}

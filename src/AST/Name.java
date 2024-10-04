package AST;

import Utilities.PokeVisitor;

public class Name extends AST {

    public Name(Token t) {
        super(t);
    }

    public String getName() { return this.myToken.getText(); }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsName(this); }
}

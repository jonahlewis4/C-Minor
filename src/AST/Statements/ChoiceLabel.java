package AST.Statements;

import AST.*;
import Utilities.PokeVisitor;

public class ChoiceLabel extends AST {
    

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsChoiceLabel(this); }
}

package AST.Statements;

import AST.*;
import AST.Expressions.*;
import Utilities.PokeVisitor;

public class ForStmt extends Statement {

    

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsForStmt(this); }
}

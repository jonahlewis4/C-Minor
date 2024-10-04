package AST.Expressions;

import AST.*;
import Utilities.PokeVisitor;

public class Literal extends Expression {

    public enum literalType { STR, REAL, TEXT, INT, BOOL, CHAR }

    Token myToken;
    literalType litKind;


    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsLiteral(this); }
}

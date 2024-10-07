package AST.Expressions;

import AST.*;
import Token.*;
import Utilities.PokeVisitor;

public class Constant extends Expression {

    public enum ConstantKind { STR, TEXT, REAL, BOOL, INT, CHAR }

    private ConstantKind kind;
    private String text;

    public Constant(Token t, ConstantKind myKind) {
        super(t);
        this.kind = myKind;
        this.text = t.getText();
    }

    public String getText() { return text; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsConstant(this); }
}

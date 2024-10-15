package AST;

import AST.Expressions.*;
import Token.*;
import Utilities.PokeVisitor;

public class Label extends AST {

    private Literal lConstant;
    private boolean defaultLabel;

    public Label(Token t, Literal l) { this(t, l, false); }

    public Label(Token t, Literal l, boolean dl) {
        super(t);
        this.lConstant = l;
        this.defaultLabel = dl;

        addChild(this.lConstant);
        setParent();
    }

    public Literal getLabel() { return lConstant; }
    public boolean isDefault() { return defaultLabel; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsChoiceLabel(this); }
}

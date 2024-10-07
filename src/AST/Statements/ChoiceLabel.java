package AST.Statements;

import AST.*;
import AST.Expressions.*;
import Utilities.PokeVisitor;

public class ChoiceLabel extends AST {
    //TODO: CONSTANT INSTEAD OF EXPRESSION?
    private Expression label;
    private boolean defaultLabel;

    public ChoiceLabel(Expression label) { this(label, false); }

    public ChoiceLabel(Expression label, boolean defaultLabel) {
        this.label = label;
        this.defaultLabel = defaultLabel;

        addChild(this.label);
    }

    public Expression getLabel() { return label; }
    public boolean isDefault() { return defaultLabel; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsChoiceLabel(this); }
}

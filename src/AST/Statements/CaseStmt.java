package AST.Statements;

import AST.*;
import Token.*;
import Utilities.PokeVisitor;

public class CaseStmt extends Statement {

    private Label myLabel;
    private BlockStmt myBlock;

    public CaseStmt(Token t, Label l, BlockStmt b) {
        super(t);
        this.myLabel = l;
        this.myBlock = b;

        addChild(this.myLabel);
        addChild(this.myBlock);
        setParent();
    }

    public Label getChoiceLabel() { return myLabel; }
    public BlockStmt getBlockStmt() { return myBlock; }

    public boolean isCaseStmt() { return true; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsCaseStmt(this); }
}

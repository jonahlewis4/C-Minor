package AST.Statements;

import AST.*;
import Utilities.PokeVisitor;

public class CaseStmt extends Statement {

    ChoiceLabel myLabel;
    BlockStmt myBlock;

    public CaseStmt(ChoiceLabel myLabel, BlockStmt myBlock) {
        this.myLabel = myLabel;
        this.myBlock = myBlock;

        addChild(this.myLabel);
        addChild(this.myBlock);
    }

    public ChoiceLabel getChoiceLabel() { return myLabel; }
    public BlockStmt getBlockStmt() { return myBlock; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsCaseStmt(this); }
}

package AST.Statements;

import AST.*;
import AST.Token;
import Utilities.PokeVisitor;

/*
----------------------------------------------------------------------
                                STOPSTMT
----------------------------------------------------------------------
Fields:
    1. stop: Token representing stop keyword
*/
public class StopStmt extends Statement {

    Token stop;

    public StopStmt(Token stop) {
        this.stop = stop;
        //addChild(this.stop);
        setParent();
    }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsStopStmt(this); }
}

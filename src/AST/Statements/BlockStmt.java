package AST.Statements;

import AST.*;
import Token.*;
import Utilities.PokeVisitor;


public class BlockStmt extends Statement {

    //TODO: Add a SymbolTable object here

    Sequence varDecls;
    Sequence stmts;

    public BlockStmt(Token t, Sequence varDecls, Sequence stmts) {
        super(t);
        this.varDecls = varDecls;
        this.stmts = stmts;
    }

    public Sequence getVarDecls() { return varDecls; }
    public Sequence getStmts() { return stmts; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsBlockStmt(this); }
}

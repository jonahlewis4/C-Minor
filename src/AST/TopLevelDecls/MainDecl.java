package AST.TopLevelDecls;

import AST.*;
import AST.Statements.*;
import Utilities.PokeVisitor;

public class MainDecl extends TopLevelDecl {

    Sequence args;
    BlockStmt mainBody;

    public MainDecl(Sequence a, BlockStmt mb) {
        super();
        args = a;
        mainBody = mb;
    }

    public Sequence getArgs() { return args; }
    public BlockStmt getMainBody() { return mainBody; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsMainDecl(this); }
}

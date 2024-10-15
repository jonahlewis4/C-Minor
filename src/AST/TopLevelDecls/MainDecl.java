package AST.TopLevelDecls;

import AST.*;
import AST.Statements.*;
import AST.Types.*;
import Token.*;
import Utilities.PokeVisitor;

public class MainDecl extends TopLevelDecl {

    private Vector<ParamDecl> args;
    private Type retType;
    private BlockStmt body;

    public MainDecl(Token t, Vector<ParamDecl> a, Type rt, BlockStmt b) {
        super(t);
        this.args = a;
        this.retType = rt;
        this.body = body;

        addChild(this.args);
        addChild(this.retType);
        addChild(this.body);
        setParent();
    }

    public Vector<ParamDecl> getArgs() { return args; }
    public Type getReturnType() { return retType; }
    public BlockStmt getMainBody() { return body; }

    @Override
    public String toString() { return "Main"; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsMainDecl(this); }
}

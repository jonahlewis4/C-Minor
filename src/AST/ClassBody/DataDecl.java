package AST.ClassBody;

import AST.*;
import AST.Types.*;
import Token.*;
import Utilities.PokeVisitor;

public class DataDecl extends AST {

    private Modifier mod;
    private Vector<Var> vars;
    private Type type;

    public DataDecl(Token t, Modifier m, Vector<Var> v, Type type) {
        super(t);
        this.mod = m;
        this.vars = v;
        this.type = type;

        addChild(this.mod);
        addChild(this.vars);
        addChild(this.type);
        setParent();
    }

    public Modifier getModifier() { return mod; }
    public Vector<Var> getVars() { return vars; }
    public Type getType() { return type; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsDataDecl(this); }
}

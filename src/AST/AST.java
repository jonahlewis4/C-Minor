package AST;

import java.util.ArrayList;
import Token.*;
import Utilities.PokeVisitor;


public abstract class AST {

    protected AST parent;
    protected ArrayList<AST> children = new ArrayList<AST>();
    protected Token myToken;

    public AST() { myToken = null; }
    public AST(Token t) { myToken = t; }
    public AST(AST node) {
        if(node != null)
            myToken = node.myToken;
    }

    protected void addChild(AST node) { children.add(node); }

    protected void setParent() {
        for(AST n : children)
            n.parent = this;
    }

    /*
    ----------------------------------------------------------------------
                               Visitor Methods
    ----------------------------------------------------------------------
    */

    public abstract AST whosThatNode(PokeVisitor v);

    public AST visitChildren(PokeVisitor v) {
        for(AST child : children) {
            if(child != null)
                child.whosThatNode(v);
        }
        return null;
    }
}

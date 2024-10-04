package AST;

import java.util.ArrayList;
import Utilities.PokeVisitor;

/*
Every node in a C Minor AST will have 2 fields:
    1. parent: Stores a reference to the parent node
    2. children: List of references to the child nodes
*/
public abstract class AST {

    protected AST parent;
    protected ArrayList<AST> children = new ArrayList<AST>();
    protected Token myToken;

    public AST() {}
    public AST(Token t) { myToken = t; }
    public AST(AST node) {}

    protected void addChild(AST node) { children.add(node); }

    // Each time we create a new AST node, we will make sure all
    // of its children point to it
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

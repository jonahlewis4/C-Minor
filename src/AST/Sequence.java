package AST;
import Utilities.PokeVisitor;

public class Sequence extends AST {

    public Sequence() { super(); }
    public Sequence(AST node) {
        super(node);
        addChild(node);
    }

    public void append(AST node) { addChild(node); }

    public void merge(Sequence seq) {
        for(AST node : seq.children)
            this.append(node);
    }
    public void merge(AST node) { this.append(node); }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsSequence(this); }
}

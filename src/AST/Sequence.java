package AST;
import Utilities.PokeVisitor;

public class Sequence extends AST {

    public Sequence(AST node) {
        super(node);
        addChild(node);
        childCount = 1;
    }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsSequence(this); }
}

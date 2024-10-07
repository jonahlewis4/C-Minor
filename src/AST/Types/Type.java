package AST.Types;

import AST.*;
import Token.*;

//TODO: Fill out the helper methods for TypeChecker 
public abstract class Type extends AST {

    public Type(Token t) { super(t); }
    public Type(AST node) { super(node); }

    public abstract String typeName();
}

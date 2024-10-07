package AST;

import AST.Types.*;

public interface VarDecl {
    public Type getType();
    public String getID();

    public boolean isClassType();
}

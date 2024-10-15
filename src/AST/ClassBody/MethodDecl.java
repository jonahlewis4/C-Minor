package AST.ClassBody;

import AST.*;
import AST.Operators.*;
import AST.Statements.*;
import AST.Types.*;
import Token.*;
import Utilities.PokeVisitor;

public class MethodDecl extends AST {

   private Vector<Modifier> mods;
   private Name name;       // Set if we have a MethodDecl
   private Operator op;     // Set if we have a OperatorDecl
   private Vector<ParamDecl> params;
   private Type returnType;
   private BlockStmt block;

   private boolean isOverrode;

   public MethodDecl(Token t, Vector<Modifier> m, Name n, Operator o, Vector<ParamDecl> p, Type rt, BlockStmt b, boolean override) {
       super(t);
       this.mods = m;
       this.name = name;
       this.op = o;
       this.params = p;
       this.returnType = rt;
       this.block = b;

       this.isOverrode = override;

       addChild(this.mods);
       addChild(this.name);
       addChild(this.op);
       addChild(this.params);
       addChild(this.returnType);
       addChild(this.block);
       setParent();
   }

   public Vector<Modifier> getModifiers() { return mods; }
   public Name getName() { return name; }
   public Operator getOperator() { return op; }
   public Vector<ParamDecl> getParams() { return params; }
   public Type getReturnType() { return returnType; }
   public BlockStmt getBlock() { return block; }
    
   public boolean isOverriden() { return isOverrode; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsMethodDecl(this); }
}

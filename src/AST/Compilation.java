package AST;

import AST.TopLevelDecls.*;
import Token.*;
import Utilities.PokeVisitor;

public class Compilation extends AST {
    Vector<EnumDecl> enumDecls;
    Vector<GlobalDecl> globalDecls;
    Vector<ClassDecl> classDecls;
    Vector<FuncDecl> funcDecls;
    MainDecl main;

    public Compilation(Token t, Vector<EnumDecl> ed, Vector<GlobalDecl> gd,
                       Vector<ClassDecl> cd, Vector<FuncDecl> fd, MainDecl m) {
        super(t);
        enumDecls = ed;
        globalDecls = gd;
        classDecls = cd;
        funcDecls = fd;
        main = m;

        addChild(enumDecls);
        addChild(globalDecls);
        addChild(classDecls);
        addChild(funcDecls);
        addChild(main);
        setParent();
    }

    public Vector<EnumDecl> getEnumDecls() { return enumDecls; }
    public Vector<GlobalDecl> getGlobalDecls() { return globalDecls; }
    public Vector<ClassDecl> getClassDecls() { return classDecls; }
    public Vector<FuncDecl> getFuncDecls() { return funcDecls; }
    public MainDecl getMain() { return main; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsCompilation(this); }
}

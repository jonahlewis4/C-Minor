package AST;

import AST.TopLevelDecls.*;
import Utilities.PokeVisitor;

public class Compilation extends AST {
    Sequence enumDecls;
    Sequence globalDecls;
    Sequence classDecls;
    Sequence funcDecls;
    MainDecl main;

    public Compilation(Sequence ed, Sequence gd, Sequence cd, Sequence fd, MainDecl m) {
        enumDecls = ed;
        globalDecls = gd;
        classDecls = cd;
        funcDecls = fd;
        main = m;
    }

    public Sequence getEnumDecls() { return enumDecls; }
    public Sequence getGlobalDecls() { return globalDecls; }
    public Sequence getClassDecls() { return classDecls; }
    public Sequence getFuncDecls() { return funcDecls; }
    public MainDecl getMain() { return main; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsCompilation(this); }
}

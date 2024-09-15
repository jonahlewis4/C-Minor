package Parser;

import java.util.ArrayList;
import java.util.List;
import Lexer.Lexer;
import Lexer.Token;

/*
                            ERRORS
1. ref def ?
2. input-statement, output-statement, argument-list missing rules
3. operator-declaration, - ?
*/

public class Parser {
    private Lexer input;
    private int k;                 // k = # of lookaheads
    private int lookPos;
    private final Token[] lookaheads;

    public Parser(Lexer input) {
        this.input = input;
        this.k = 3;
        this.lookPos = 0;
        this.lookaheads = new Token[k];
        for(int i = 0; i < k; i++)
            consume();
    }

    private void consume() {
        lookaheads[lookPos] = this.input.nextToken();
        lookPos = (lookPos + 1) % k;
    }

    private boolean match(Lexer.TokenType expectedTok) {
        if(nextLA(0) == expectedTok) {
            consume();
            return true;
        }
        else throw new IllegalArgumentException("Warning! Expected " + expectedTok + ", but got " + nextLA(0));
    }

    // currentLA : Returns the current token we are checking
    private Token currentLA(int nextPos) { return lookaheads[(lookPos+nextPos)%k]; }

    // nextLA : Returns the next LA at the specified index
    private Lexer.TokenType nextLA(int nextPos) { return currentLA(nextPos).getTokenType(); }

    /*
    ------------------------------------------------------------
                          COMPILATION UNIT
    ------------------------------------------------------------
    */

    // 1. compilation ::= file-merge* enum-type* global-variable* class-type* function* main
    public void compilation() {
        while(nextLA(0) == Lexer.TokenType.ENUM)
            enumType();

        while(nextLA(0) == Lexer.TokenType.CONST || nextLA(0) == Lexer.TokenType.GLOBAL)
            globalVariable();

        while(nextLA(0) == Lexer.TokenType.ABSTR || nextLA(0) == Lexer.TokenType.FINAL)
            classType();

        while((nextLA(0) == Lexer.TokenType.DEF && nextLA(1) != Lexer.TokenType.MAIN) ||
               nextLA(0) == Lexer.TokenType.PURE ||
               nextLA(0) == Lexer.TokenType.RECURS)
                function();

        mainFunc();

        if(nextLA(0) != Lexer.TokenType.EOF)
            System.out.println("EOF Not Reached.");
    }

    /*
    ------------------------------------------------------------
                          ENUM DECLARATION
    ------------------------------------------------------------
    */

    // 2. enum-type ::= Enum ID { ID (, ID)* }
    private void enumType() {
        match(Lexer.TokenType.ENUM);
        match(Lexer.TokenType.ID);
        match(Lexer.TokenType.LBRACE);
        match(Lexer.TokenType.ID);

        while(nextLA(0) == Lexer.TokenType.COMMA) {
            match(Lexer.TokenType.COMMA);
            match(Lexer.TokenType.ID);
        }
        match(Lexer.TokenType.RBRACE);
    }

    /*
    ------------------------------------------------------------
              GLOBAL VARIABLES AND VARIABLE DECLARATIONS
    ------------------------------------------------------------
    */

    // 3. global-variable ::= (const | global) variable-decl
    private void globalVariable() {
        if(nextLA(0) == Lexer.TokenType.CONST)
            match(Lexer.TokenType.CONST);
        else if(nextLA(0) == Lexer.TokenType.GLOBAL)
            match(Lexer.TokenType.GLOBAL);
        else throw new IllegalStateException("Invalid Global Variable!");

        variableDecl();

    }

    // 4. variable-decl ::= def variable-decl-list
    private void variableDecl() {
        match(Lexer.TokenType.DEF);
        variableDeclList();
    }

    // 5. variable-decl-list ::= variable-decl-init (, variable-decl-init)*
    private void variableDeclList() {
        variableDeclInit();
        while(nextLA(0) == Lexer.TokenType.COMMA) {
            match(Lexer.TokenType.COMMA);
            variableDeclInit();
        }
    }

    // 6. variable-decl-init ::= ID:type = (expression | uninit)
    private void variableDeclInit() {
        match(Lexer.TokenType.ID);
        match(Lexer.TokenType.COLON);
        type();
        match(Lexer.TokenType.EQ);
        if(nextLA(0) == Lexer.TokenType.UNINIT)
            match(Lexer.TokenType.UNINIT);
        else expression();
    }

    /*
    ------------------------------------------------------------
                                TYPES
    ------------------------------------------------------------
    */

    // 7. type ::= scalar-type | class-name | List[type] | Tuple<type (, type)*>
    private void type() {
        if( nextLA(0) == Lexer.TokenType.STRING ||
            nextLA(0) == Lexer.TokenType.REAL ||
            nextLA(0) == Lexer.TokenType.BOOL ||
            nextLA(0) == Lexer.TokenType.INT ||
            nextLA(0) == Lexer.TokenType.CHAR ||
            (nextLA(0) == Lexer.TokenType.ID && (nextLA(1) == Lexer.TokenType.COLON ||
            nextLA(1) == Lexer.TokenType.LBRACK)))
                scalarType();

        else if(nextLA(0) == Lexer.TokenType.ID)
            className();

        else if(nextLA(0) == Lexer.TokenType.LIST) {
            match(Lexer.TokenType.LIST);
            match(Lexer.TokenType.LBRACK);
            type();
            match(Lexer.TokenType.RBRACK);
        }
        else if(nextLA(0) == Lexer.TokenType.TUPLE) {
            match(Lexer.TokenType.TUPLE);
            match(Lexer.TokenType.LT);
            type();
            while(nextLA(0) == Lexer.TokenType.COMMA) {
                match(Lexer.TokenType.COMMA);
                type();
            }
        }
        else throw new IllegalStateException("Error! Can not determine type...");
    }

    // 8. scalar-type ::= discrete-type
    //                  | String([INT-LITERAL])*
    //                  | Real (:INT-LITERAL)? ([INT-LITERAL])*
    private void scalarType() {
        if(nextLA(0) == Lexer.TokenType.STRING) {
            match(Lexer.TokenType.STRING);
            while(nextLA(0) == Lexer.TokenType.LBRACK) {
                match(Lexer.TokenType.LBRACK);
                match(Lexer.TokenType.INT_LIT);
                match(Lexer.TokenType.RBRACK);
            }
        }
        else if(nextLA(0) == Lexer.TokenType.REAL) {
            match(Lexer.TokenType.REAL);
            if(nextLA(0) == Lexer.TokenType.COLON) {
                match(Lexer.TokenType.COLON);
                match(Lexer.TokenType.INT_LIT);
            }
            while(nextLA(0) == Lexer.TokenType.LBRACK) {
                match(Lexer.TokenType.LBRACK);
                match(Lexer.TokenType.INT_LIT);
                match(Lexer.TokenType.RBRACK);
            }
        }
        else
            discreteType();
    }

    // 9. discrete-type ::= Bool([INT-LITERAL])*
    //                    | Int (: INT-LITERAL .. INT-LITERAL)? ([INT-LITERAL])*
    //                    | Char(: CHAR-LITERAL .. CHAR-LITERAL)? ([INT-LITERAL])*
    //                    | ID (: ID .. ID)? ([INT-LITERAL])*
    private void discreteType() {
        if(nextLA(0) == Lexer.TokenType.BOOL) {
            match(Lexer.TokenType.BOOL);
            while(nextLA(0) == Lexer.TokenType.LBRACK) {
                match(Lexer.TokenType.LBRACK);
                match(Lexer.TokenType.INT_LIT);
                match(Lexer.TokenType.RBRACK);
            }
        }
        else if(nextLA(0) == Lexer.TokenType.INT) {
            match(Lexer.TokenType.INT);
            if(nextLA(0) == Lexer.TokenType.COLON) {
                match(Lexer.TokenType.COLON);
                match(Lexer.TokenType.INT_LIT);
                match(Lexer.TokenType.INC);
                match(Lexer.TokenType.INT_LIT);
            }
            while(nextLA(0) == Lexer.TokenType.LBRACK) {
                match(Lexer.TokenType.LBRACK);
                match(Lexer.TokenType.INT_LIT);
                match(Lexer.TokenType.RBRACK);
            }
        }
        else if(nextLA(0) == Lexer.TokenType.CHAR) {
            match(Lexer.TokenType.CHAR);
            if(nextLA(0) == Lexer.TokenType.COLON) {
                match(Lexer.TokenType.COLON);
                match(Lexer.TokenType.CHAR_LIT);
                match(Lexer.TokenType.INC);
                match(Lexer.TokenType.CHAR_LIT);
            }
            while(nextLA(0) == Lexer.TokenType.LBRACK) {
                match(Lexer.TokenType.LBRACK);
                match(Lexer.TokenType.INT_LIT);
                match(Lexer.TokenType.RBRACK);
            }
        }
        else if(nextLA(0) == Lexer.TokenType.ID) {
            match(Lexer.TokenType.ID);
            if(nextLA(0) == Lexer.TokenType.COLON) {
                match(Lexer.TokenType.COLON);
                match(Lexer.TokenType.ID);
                match(Lexer.TokenType.INC);
                match(Lexer.TokenType.ID);
            }
            while(nextLA(0) == Lexer.TokenType.LBRACK) {
                match(Lexer.TokenType.LBRACK);
                match(Lexer.TokenType.INT_LIT);
                match(Lexer.TokenType.RBRACK);
            }
        }
        else throw new IllegalStateException("Invalid type entered!");
    }

    // 10. class-name ::= ID (<type (, type)*>)?
    private void className() {
        match(Lexer.TokenType.ID);
        if(nextLA(0) == Lexer.TokenType.LT) {
            match(Lexer.TokenType.LT);
            type();
            while(nextLA(0) == Lexer.TokenType.COMMA) {
                match(Lexer.TokenType.COMMA);
                type();
            }
            match(Lexer.TokenType.GT);
        }
    }

    /*
    ------------------------------------------------------------
                          CLASS DECLARATION
    ------------------------------------------------------------
    */

    // 11. class-type ::= (abstr | final) class ID type-params? super-class? body-decl
    private void classType() {
        if(nextLA(0) == Lexer.TokenType.ABSTR)
            match(Lexer.TokenType.ABSTR);
        else if(nextLA(0) == Lexer.TokenType.FINAL)
            match(Lexer.TokenType.FINAL);
        else throw new IllegalStateException("Invalid class modifier.");

        match(Lexer.TokenType.CLASS);
        match(Lexer.TokenType.ID);

        if(nextLA(0) == Lexer.TokenType.LT)
            typeParams();

        if(nextLA(0) == Lexer.TokenType.INHERITS)
            superClass();

        bodyDecl();
    }

    // 12. type-params ::= <type (, type)*>
    private void typeParams() {
        match(Lexer.TokenType.LT);
        type();
        while(nextLA(0) == Lexer.TokenType.COMMA) {
            match(Lexer.TokenType.COMMA);
            type();
        }
    }

    // 13. super-class ::= inherits ID type-params?
    private void superClass() {
        match(Lexer.TokenType.INHERITS);
        match(Lexer.TokenType.ID);
        if(nextLA(0) == Lexer.TokenType.LT)
            typeParams();
    }

    // 14. body-decl ::= protected {data-field* method*} public {data-field* method*}
    private void bodyDecl() {
        match(Lexer.TokenType.PROTECTED);
        while(nextLA(0) == Lexer.TokenType.REF || nextLA(0) == Lexer.TokenType.DEF)
            dataField();

        while(nextLA(0) == Lexer.TokenType.FINAL ||
              nextLA(0) == Lexer.TokenType.PURE ||
              nextLA(0) == Lexer.TokenType.RECURS ||
              nextLA(0) == Lexer.TokenType.OVERRIDE ||
              nextLA(0) == Lexer.TokenType.METHOD ||
              nextLA(0) == Lexer.TokenType.OPERATOR)
                method();

        match(Lexer.TokenType.PUBLIC);
        while(nextLA(0) == Lexer.TokenType.REF || nextLA(0) == Lexer.TokenType.DEF)
            dataField();

        while(nextLA(0) == Lexer.TokenType.FINAL ||
                nextLA(0) == Lexer.TokenType.PURE ||
                nextLA(0) == Lexer.TokenType.RECURS ||
                nextLA(0) == Lexer.TokenType.OVERRIDE ||
                nextLA(0) == Lexer.TokenType.METHOD ||
                nextLA(0) == Lexer.TokenType.OPERATOR)
            method();
    }

    /*
    ------------------------------------------------------------
                          FIELD DECLARATION
    ------------------------------------------------------------
    */

    // 15. data-field ::= ref? variable-decl
    private void dataField() {
        if(nextLA(0) == Lexer.TokenType.REF)
            match(Lexer.TokenType.REF);
        variableDecl();
    }

    // 16. method ::= method-class | operator-class
    private void method() {
        if((nextLA(0) == Lexer.TokenType.FINAL && nextLA(1) == Lexer.TokenType.OPERATOR) ||
            nextLA(0) == Lexer.TokenType.OPERATOR)
                operatorClass();
        else methodClass();
    }

    /*
    ------------------------------------------------------------
                          METHOD DECLARATION
    ------------------------------------------------------------
    */

    // 17. method-class ::= modifier* override? method method-header => return-type block-statement
    private void methodClass() {
        while(nextLA(0) == Lexer.TokenType.FINAL ||
              nextLA(0) == Lexer.TokenType.PURE ||
              nextLA(0) == Lexer.TokenType.RECURS)
                modifier();

        if(nextLA(0) == Lexer.TokenType.OVERRIDE)
            match(Lexer.TokenType.OVERRIDE);

        match(Lexer.TokenType.METHOD);

        methodHeader();

        match(Lexer.TokenType.EQ);
        match(Lexer.TokenType.GT);

        returnType();
        blockStatement();
    }

    // 18. method-header ::= ID (formal-params?)
    private void methodHeader() {
        match(Lexer.TokenType.ID);
        match(Lexer.TokenType.LPAREN);
        if( nextLA(0) == Lexer.TokenType.IN ||
            nextLA(0) == Lexer.TokenType.OUT ||
            nextLA(0) == Lexer.TokenType.INOUT ||
            nextLA(0) == Lexer.TokenType.REF)
                formalParams();
        match(Lexer.TokenType.RPAREN);
    }

    // 19. modifier ::= final | pure | recurs
    private void modifier() {
        if(nextLA(0) == Lexer.TokenType.FINAL)
            match(Lexer.TokenType.FINAL);
        else if(nextLA(0) == Lexer.TokenType.PURE)
            match(Lexer.TokenType.PURE);
        else if(nextLA(0) == Lexer.TokenType.RECURS)
            match(Lexer.TokenType.RECURS);
        else throw new IllegalStateException("Error: Invalid Method Modifier");
    }

    // 20. formal-params ::= (in | out | inout | in out | ref) type ID
    private void formalParams() {
        if(nextLA(0) == Lexer.TokenType.IN)
            match(Lexer.TokenType.IN);
        else if(nextLA(0) == Lexer.TokenType.OUT)
            match(Lexer.TokenType.OUT);
        else if(nextLA(0) == Lexer.TokenType.INOUT)
            match(Lexer.TokenType.INOUT);
        else if(nextLA(0) == Lexer.TokenType.REF)
            match(Lexer.TokenType.REF);
        else throw new IllegalStateException("Error: Invalid Function Parameter Type!");

        type();
        match(Lexer.TokenType.ID);
    }

    // 21. return-type ::= Void | type
    private void returnType() {
        if(nextLA(0) == Lexer.TokenType.VOID)
            match(Lexer.TokenType.VOID);
        else type();
    }

    // 22. operator-class ::= final? operator operator-header => return-type block-statement
    private void operatorClass() {
        if(nextLA(0) == Lexer.TokenType.FINAL)
            match(Lexer.TokenType.FINAL);

        match(Lexer.TokenType.OPERATOR);
        operatorHeader();

        match(Lexer.TokenType.EQ);
        match(Lexer.TokenType.GT);

        returnType();
        blockStatement();
    }

    // 23. operator-header ::= operator-declaration (formal-params?)
    private void operatorHeader() {
        operatorDeclaration();

        match(Lexer.TokenType.LPAREN);
        if( nextLA(0) == Lexer.TokenType.IN ||
            nextLA(0) == Lexer.TokenType.OUT ||
            nextLA(0) == Lexer.TokenType.INOUT ||
            nextLA(0) == Lexer.TokenType.REF)
            formalParams();
        match(Lexer.TokenType.RPAREN);
    }

    // 24. operator-declaration ::= binary-operator | unary-operator
    private void operatorDeclaration() {
        if(nextLA(0) == Lexer.TokenType.NOT)
            unaryOperator();
        else
            binaryOperator();
    }

    // 25. binary-operator ::= <= | < | > | >= | == | <> | <=> | <: | :> | + | - | * | / | % | **
    private void binaryOperator() {
        if(nextLA(0) == Lexer.TokenType.LTEQ)
            match(Lexer.TokenType.LTEQ);
        else if(nextLA(0) == Lexer.TokenType.LT)
            match(Lexer.TokenType.LT);
        else if(nextLA(0) == Lexer.TokenType.GT)
            match(Lexer.TokenType.GT);
        else if(nextLA(0) == Lexer.TokenType.GTEQ)
            match(Lexer.TokenType.GTEQ);
        else if(nextLA(0) == Lexer.TokenType.EQEQ)
            match(Lexer.TokenType.EQEQ);
        else if(nextLA(0) == Lexer.TokenType.LTGT)
            match(Lexer.TokenType.LTGT);
        else if(nextLA(0) == Lexer.TokenType.LTEQGT)
            match(Lexer.TokenType.LTEQGT);
        else if(nextLA(0) == Lexer.TokenType.MIN)
            match(Lexer.TokenType.MIN);
        else if(nextLA(0) == Lexer.TokenType.MAX)
            match(Lexer.TokenType.MAX);
        else if(nextLA(0) == Lexer.TokenType.PLUS)
            match(Lexer.TokenType.PLUS);
        else if(nextLA(0) == Lexer.TokenType.MINUS)
            match(Lexer.TokenType.MINUS);
        else if(nextLA(0) == Lexer.TokenType.MULT)
            match(Lexer.TokenType.MULT);
        else if(nextLA(0) == Lexer.TokenType.DIV)
            match(Lexer.TokenType.DIV);
        else if(nextLA(0) == Lexer.TokenType.MOD)
            match(Lexer.TokenType.MOD);
        else if(nextLA(0) == Lexer.TokenType.EXP)
            match(Lexer.TokenType.EXP);
        else throw new IllegalStateException("Error! Invalid Binary Operator");
    }

    // 26. unary-operator ::= - | not
    private void unaryOperator() {
        if(nextLA(0) == Lexer.TokenType.MINUS)
            match(Lexer.TokenType.MINUS);
        else if(nextLA(0) == Lexer.TokenType.NOT)
            match(Lexer.TokenType.NOT);
        else throw new IllegalStateException("Invalid Unary Operator!");
    }


    /*
    ------------------------------------------------------------
                        FUNCTION DECLARATION
    ------------------------------------------------------------
    */

    // 27. function ::= (pure | recurs)? def function-header => return-type block-statement
    private void function() {
        if(nextLA(0) == Lexer.TokenType.PURE)
            match(Lexer.TokenType.PURE);
        else if(nextLA(0) == Lexer.TokenType.RECURS)
            match(Lexer.TokenType.RECURS);

        match(Lexer.TokenType.DEF);

        functionHeader();

        match(Lexer.TokenType.EQ);
        match(Lexer.TokenType.GT);

        returnType();
        blockStatement();
    }

    // 28. function-header ::= ID function-type-params? (formal-params?)
    private void functionHeader() {
        match(Lexer.TokenType.ID);

        if(nextLA(0) == Lexer.TokenType.LT)
            functionTypeParams();

        match(Lexer.TokenType.LPAREN);

        if( nextLA(0) == Lexer.TokenType.IN ||
            nextLA(0) == Lexer.TokenType.OUT ||
            nextLA(0) == Lexer.TokenType.INOUT ||
            nextLA(0) == Lexer.TokenType.REF)
                formalParams();

        match(Lexer.TokenType.RPAREN);
    }

    // 29. function-type-params ::= <typeifier (, typeifier)*)>
    private void functionTypeParams() {
        match(Lexer.TokenType.LT);
        typeifier();

        while(nextLA(0) == Lexer.TokenType.COMMA) {
            match(Lexer.TokenType.COMMA);
            typeifier();
        }
        match(Lexer.TokenType.GT);
    }

    // 30. typeifier ::= (discr | scalar | class)? ID
    private void typeifier() {
        if(nextLA(0) == Lexer.TokenType.DISCR)
            match(Lexer.TokenType.DISCR);
        else if(nextLA(0) == Lexer.TokenType.SCALAR)
            match(Lexer.TokenType.SCALAR);
        else if(nextLA(0) == Lexer.TokenType.CLASS)
            match(Lexer.TokenType.CLASS);

        match(Lexer.TokenType.ID);
    }

    /*
    ------------------------------------------------------------
                          MAIN FUNCTION
    ------------------------------------------------------------
    */

    // 31. main ::= def main block-statement
    private void mainFunc() {
        match(Lexer.TokenType.DEF);
        match(Lexer.TokenType.MAIN);
        blockStatement();
    }

    // 32. block-statement ::= {declaration* statement*}
    private void blockStatement() {
        match(Lexer.TokenType.LBRACE);
        while(nextLA(0) == Lexer.TokenType.LOCAL || nextLA(0) == Lexer.TokenType.DEF)
            declaration();

        match(Lexer.TokenType.RBRACE);
    }

    // 33. declaration ::= local? variable-decl | ref variable-decl
    private void declaration() {
        if(nextLA(0) == Lexer.TokenType.REF) {
            match(Lexer.TokenType.REF);
            variableDecl();
        }
        else if(nextLA(0) == Lexer.TokenType.LOCAL)
            match(Lexer.TokenType.LOCAL);

        if(nextLA(0) == Lexer.TokenType.DEF) {
            match(Lexer.TokenType.DEF);
            variableDecl();
        }
        else throw new IllegalStateException("Error Invalid Declaration!");
    }

    /*
    ------------------------------------------------------------
                             STATEMENTS
    ------------------------------------------------------------
    */

    // 34. statement ::= stop | return-statement | assignment-statement | block-statment | if-statement
    //                 | iterator-statement | loop-statement | choice-statement | list-command-statement | input-output-statement
    private void statement() {

    }

    // 35. return-statement ::= return expression?
    private void returnStatement() {
        match(Lexer.TokenType.RETURN);
        // CALL expression
    }

    // 36. assignment-statement ::= reassign ID = argument-value
    private void assignmentStatement() {
        match(Lexer.TokenType.REASSIGN);
        match(Lexer.TokenType.ID);
        match(Lexer.TokenType.EQ);
        argumentValue();
    }

    // 37. argument-value ::= ref expression | expression
    private void argumentValue() {
        if(nextLA(0) == Lexer.TokenType.REF) {
            match(Lexer.TokenType.REF);
            expression();
        }
        else expression();
    }

    // 38. if-statement ::= if expression block-statement (elseif-statement)* (else block-statement)?
    private void ifStatement() {
        match(Lexer.TokenType.IF);

        expression();
        blockStatement();

        while(nextLA(0) == Lexer.TokenType.ELSE && nextLA(1) == Lexer.TokenType.IF)
            elseIfStatement();

        if(nextLA(0) == Lexer.TokenType.ELSE)
            blockStatement();
    }

    // 39. elseif-statement ::= else if expression block-statement
    private void elseIfStatement() {
        match(Lexer.TokenType.ELSE);
        match(Lexer.TokenType.IF);
        expression();
        blockStatement();
    }

    // 40. iterator-statement ::= for (range-iterator | array-iterator) block-statement
    private void iteratorStatement() {
        match(Lexer.TokenType.FOR);

        blockStatement();
    }

    // 41. range-iterator ::= ID in expression range-operator expression
    private void rangeIterator() {

    }

    // 42. array-iterator ::= ID (in | inrev) expression
    private void arrayIterator() {

    }

    // 43. range-operator ::= inclusive | exclusive-right | exclusive-left | exclusive
    private void rangeOperator() {
    }

    // 44. inclusive ::= ..
    private void inclusive() {

    }

    // 45. exclusive-right ::= ..<
    private void exclusiveRight() {

    }

    // 46. exclusive-left ::= <..
    private void exclusiveLeft() {

    }

    // 47. exclusive ::= <..<
    private void exclusive() {

    }

    // 48. loop-statement ::= loop { declaration* statement* until (expression) statement* }
    private void loopStatement() {
        match(Lexer.TokenType.LOOP);
        match(Lexer.TokenType.LBRACE);
        while(nextLA(0) == Lexer.TokenType.DEF ||
              nextLA(0) == Lexer.TokenType.REF ||
              nextLA(0) == Lexer.TokenType.LOCAL)
                declaration();

        // statements call
        match(Lexer.TokenType.UNTIL);
        match(Lexer.TokenType.LPAREN);
        expression();
        match(Lexer.TokenType.RPAREN);
        //statements call
        match(Lexer.TokenType.RBRACE);
    }

    // 49. choice-statement ::= choice expression { case-statement* other block-statement }
    private void choiceStatement() {
        match(Lexer.TokenType.CHOICE);
        expression();
        match(Lexer.TokenType.LBRACE);
        while(nextLA(0) == Lexer.TokenType.ON)
            caseStatement();
        match(Lexer.TokenType.OTHER);
        blockStatement();
        match(Lexer.TokenType.RBRACE);
    }

    // 50. case-statement ::= on label block-statement
    private void caseStatement() {
        match(Lexer.TokenType.ON);
        label();
        blockStatement();
    }

    // 51. label ::= constant (.. constant)?
    private void label() {
        constant();
        if(nextLA(0) == Lexer.TokenType.INC) {
            match(Lexer.TokenType.INC);
            constant();
        }
    }

    // 52. list-command-statement ::= append (argument-list) | remove (argument-list) | insert (argument-list)
    private void listCommandStatement() {
        if(nextLA(0) == Lexer.TokenType.APPEND) {
            match(Lexer.TokenType.APPEND);
            match(Lexer.TokenType.LPAREN);
            //argumentList();
        }
    }

    // 53. input-output-statement ::= input-statement | output-statement
    private void inputOutputStatement() {

    }

    /*
    ------------------------------------------------------------
                            EXPRESSIONS
    ------------------------------------------------------------
    */

    // 54. expression ::= and-expression ( or and-expression)*
    private void expression() {
        andExpression();
        while(nextLA(0) == Lexer.TokenType.OR) {
            match(Lexer.TokenType.OR);
            andExpression();
        }
    }

    // 55. and-expression ::= equal-expression ( and equal-expression)*
    private void andExpression() {
        equalExpression();
        while(nextLA(0) == Lexer.TokenType.AND) {
            match(Lexer.TokenType.AND);
            equalExpression();
        }
    }

    // 56. equal-expression ::= relational-expression ((== | <>) relational-expression)*
    private void equalExpression() {
        relationalExpression();
        while(nextLA(0) == Lexer.TokenType.EQEQ ||
              nextLA(0) == Lexer.TokenType.NEQ) {
                if(nextLA(0) == Lexer.TokenType.EQEQ) {
                    match(Lexer.TokenType.EQEQ);
                    relationalExpression();
                }
                else if(nextLA(0) == Lexer.TokenType.NEQ) {
                    match(Lexer.TokenType.NEQ);
                    relationalExpression();
                }
        }
    }

    // 57. relational-expression ::= minmax-expression (relational-operator minmax-expression)*
    private void relationalExpression() {
        minMaxExpression();
        while(nextLA(0) == Lexer.TokenType.LTEQ ||
              nextLA(0) == Lexer.TokenType.LT ||
              nextLA(0) == Lexer.TokenType.GT ||
              nextLA(0) == Lexer.TokenType.GTEQ ||
              nextLA(0) == Lexer.TokenType.LTEQGT ||
              nextLA(0) == Lexer.TokenType.INSTANCEOF) {
            relationalOperator();
            minMaxExpression();
        }
    }

    // 58. relational-operator ::= <= | < | > | >= | <=> | instanceof
    private void relationalOperator() {
        if(nextLA(0) == Lexer.TokenType.LTEQ)
            match(Lexer.TokenType.LTEQ);
        else if(nextLA(0) == Lexer.TokenType.LT)
            match(Lexer.TokenType.LT);
        else if(nextLA(0) == Lexer.TokenType.GT)
            match(Lexer.TokenType.GT);
        else if(nextLA(0) == Lexer.TokenType.GTEQ)
            match(Lexer.TokenType.GTEQ);
        else if(nextLA(0) == Lexer.TokenType.LTEQGT)
            match(Lexer.TokenType.LTEQGT);
        else if(nextLA(0) == Lexer.TokenType.INSTANCEOF)
            match(Lexer.TokenType.INSTANCEOF);
        else throw new IllegalStateException("Error! Invalid Relational Operator!");
    }

    // 59. minmax-expression ::= additive-expression ( (<: | :>) additive-expression)*
    private void minMaxExpression() {
        additiveExpression();
        while(nextLA(0) == Lexer.TokenType.MIN||
                nextLA(0) == Lexer.TokenType.MAX) {
            if(nextLA(0) == Lexer.TokenType.MIN) {
                match(Lexer.TokenType.MIN);
                additiveExpression();
            }
            else if(nextLA(0) == Lexer.TokenType.MAX) {
                match(Lexer.TokenType.MAX);
                additiveExpression();
            }
        }
    }

    // 60. additive-expression ::= multiplicative-expression ( (+ | -) multiplicative-expression)*
    private void additiveExpression() {
        multiplicativeExpression();
        while(nextLA(0) == Lexer.TokenType.PLUS ||
              nextLA(0) == Lexer.TokenType.MINUS) {
                if(nextLA(0) == Lexer.TokenType.PLUS) {
                    match(Lexer.TokenType.PLUS);
                    multiplicativeExpression();
                }
                else if(nextLA(0) == Lexer.TokenType.MINUS) {
                    match(Lexer.TokenType.MINUS);
                    multiplicativeExpression();
                }
        }
    }

    // 61. multiplicative-expression ::= power-expression ( (*|/|%) power-expression)*
    private void multiplicativeExpression() {
        powerExpression();
        while(nextLA(0) == Lexer.TokenType.MULT ||
              nextLA(0) == Lexer.TokenType.DIV  ||
              nextLA(0) == Lexer.TokenType.MOD) {
                if(nextLA(0) == Lexer.TokenType.MULT) {
                    match(Lexer.TokenType.MULT);
                    powerExpression();
                }
                else if(nextLA(0) == Lexer.TokenType.DIV) {
                    match(Lexer.TokenType.DIV);
                    powerExpression();
                }
                else if(nextLA(0) == Lexer.TokenType.MOD) {
                    match(Lexer.TokenType.MOD);
                    powerExpression();
                }
        }
    }

    // 62. power-expression ::= unary-expression (** unary-expression)*
    private void powerExpression() {
        unaryExpression();
        while(nextLA(0) == Lexer.TokenType.EXP) {
            match(Lexer.TokenType.EXP);
            unaryExpression();
        }
    }

    // 63. unary-expression ::= (- | not) unary-expression | factor-expression
    private void unaryExpression() {
        if(nextLA(0) == Lexer.TokenType.MINUS) {
            match(Lexer.TokenType.MINUS);
            unaryExpression();
        }
        else if(nextLA(0) == Lexer.TokenType.NOT) {
            match(Lexer.TokenType.NOT);
            unaryExpression();
        }
        else
            factorExpression();
    }

    // 64. factor-expression ::= call-expression | uninit? (expression) | length (expression)
    //                         | slice (expression, expression range-operator expression) | cast (type, expression)
    private void factorExpression() {
       // if(nextLA(0) == Lexer.TokenType.UNINIT || nextLA(0) == Lexer.TokenType.LPAREN)

        if(nextLA(0) == Lexer.TokenType.LENGTH) {
            match(Lexer.TokenType.LENGTH);
            match(Lexer.TokenType.LPAREN);
            expression();
            match(Lexer.TokenType.RPAREN);
        }
        else if(nextLA(0) == Lexer.TokenType.SLICE) {
            match(Lexer.TokenType.SLICE);
            match(Lexer.TokenType.LPAREN);

            expression();
            match(Lexer.TokenType.COMMA);

            expression();
            rangeOperator();
            expression();
            match(Lexer.TokenType.RPAREN);
        }
        else if(nextLA(0) == Lexer.TokenType.CAST) {
            match(Lexer.TokenType.CAST);
            match(Lexer.TokenType.LPAREN);
            type();

            match(Lexer.TokenType.COMMA);
            expression();

            match(Lexer.TokenType.RPAREN);
        }
    }

    // 65. call-expression ::= primary ( (argument-list?) | . ID | [expression])*
    private void callExpression() {
        primary();

    }

    // 66. primary ::= ID | constant | (expression)
    private void primary() {
        if(nextLA(0) == Lexer.TokenType.ID)
            match(Lexer.TokenType.ID);

        else if(nextLA(0) == Lexer.TokenType.LPAREN) {
            match(Lexer.TokenType.LPAREN);
            expression();
            match(Lexer.TokenType.RPAREN);
        }
    }

    /*
    ------------------------------------------------------------
                                LITERALS
    ------------------------------------------------------------
    */

    // 67. constant ::= object-constant | array-constant | list-constant | tuple-constant | scalar-constant
    private void constant() {
        if(nextLA(0) == Lexer.TokenType.ID)
            objectConstant();
        else if(nextLA(0) == Lexer.TokenType.ARRAY ||
                nextLA(0) == Lexer.TokenType.LBRACK)
            arrayConstant();
        else if(nextLA(0) == Lexer.TokenType.LIST ||
                nextLA(0) == Lexer.TokenType.LBRACE)
            listConstant();
        else if(nextLA(0) == Lexer.TokenType.TUPLE ||
                nextLA(0) == Lexer.TokenType.LPAREN)
            tupleConstant();
        else
            scalarConstant();
    }

    // 68. object-constant ::= ID ( object-field (, object-field)* )
    private void objectConstant() {
        match(Lexer.TokenType.ID);
        match(Lexer.TokenType.LPAREN);

        objectField();
        while(nextLA(0) == Lexer.TokenType.COMMA) {
            match(Lexer.TokenType.COMMA);
            objectField();
        }
        match(Lexer.TokenType.RPAREN);
    }

    // 69. object-field ::= ID = expression
    private void objectField() {
        match(Lexer.TokenType.ID);
        match(Lexer.TokenType.EQ);

        expression();
    }

    // 70. array-constant ::= Array ([expression])* (expression) | [expression (, expression)*]
    private void arrayConstant() {

    }

    // 71. list-constant ::= List ( (expression (, expression)*)? ) | {expression (, expression)*}
    private void listConstant() {

    }

    // 72. tuple-constant ::= Tuple ( (expression (, expression)*)? ) | (expression (, expression)*)
    private void tupleConstant() {

    }

    // 73. scalar-constant ::= discrete-constant | STRING-LITERAL | TEXT-LITERAL | REAL-LITERAL
    private void scalarConstant() {
        if(nextLA(0) == Lexer.TokenType.STR_LIT)
            match(Lexer.TokenType.STR_LIT);
        else if(nextLA(0) == Lexer.TokenType.TEXT_LIT)
            match(Lexer.TokenType.TEXT_LIT);
        else if(nextLA(0) == Lexer.TokenType.REAL_LIT)
            match(Lexer.TokenType.REAL_LIT);
        else
            discreteConstant();
    }

    // 74. discrete-constant ::= INT-LITERAL | CHAR-LITERAL | BOOL-LITERAL
    private void discreteConstant() {
        if(nextLA(0) == Lexer.TokenType.INT_LIT)
            match(Lexer.TokenType.INT_LIT);
        else if(nextLA(0) == Lexer.TokenType.CHAR_LIT)
            match(Lexer.TokenType.CHAR_LIT);
        else if(nextLA(0) == Lexer.TokenType.BOOL_LIT)
            match(Lexer.TokenType.BOOL_LIT);
        else throw new IllegalStateException("Error! Invalid Constant!");
    }

    /*
    ------------------------------------------------------------
                          COMPILER DIRECTIVES
    ------------------------------------------------------------
    */

    // 75. file-merge ::= #include filename choice? rename | #exclude choice
    private void fileMerge() {

    }

    // 76. filename ::= STRING-LITERAL
    private void fileName() {

    }

    // 77. choice ::= only {global-id (, global-id)*} | except {global_id (, global_id)*}
    private void choice() {

    }

    // 78. global-id ::= ID
    private void globalID() {

    }

    // 79. rename ::= rename {change-id (, change-id)*}
    private void rename() {

    }

    // 80. change-id ::= ID => ID
    private void changeID() {

    }
}

package Parser;

import Lexer.Lexer;
import Lexer.Lexer.TokenType;
import Lexer.Token;

/*
Grammar Changes

FIRST(expression) = {cast, length, slice, ID, (, [, Array, List, Tuple, STRING-Literal, Text-Literal,real,intlit,charlit,boollit}
                            ERRORS
*/

public class Parser {
    private final Lexer input;
    private final int k;              // k = # of lookaheads
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
        lookaheads[lookPos].asString();
        lookPos = (lookPos + 1) % k;
    }

    private void match(TokenType expectedTok) {
        if(nextLA(0) == expectedTok)
            consume();
        else throw new IllegalArgumentException("\nWarning! Expected " + expectedTok + ", but got " + nextLA(0));
    }

    // currentLA : Returns the current token we are checking
    private Token currentLA(int nextPos) { return lookaheads[(lookPos+nextPos)%k]; }

    // nextLA : Returns the next LA at the specified index
    private boolean nextLA(TokenType expectedTok, int nextPos) { return currentLA(nextPos).getTokenType() == expectedTok; }

    /*
    ------------------------------------------------------------
                          COMPILATION UNIT
    ------------------------------------------------------------
    */

    // 1. compilation ::= file-merge* enum-type* global-variable* class-type* function* main
    public void compilation() {
        while(nextLA(TokenType.INCLUDE,0) || nextLA(TokenType.EXCLUDE,0))
            fileMerge();


//        while(nextLA(0) == TokenType.ENUM)
//            enumType();

        while(nextLA(TokenType.DEF, 0) &&
             (nextLA(TokenType.CONST, 0) || nextLA(TokenType.GLOBAL, 0)))
            globalVariable();

        while(nextLA(0) == TokenType.ABSTR || nextLA(0) == TokenType.FINAL)
            classType();

        while((nextLA(0) == TokenType.DEF && nextLA(1) != TokenType.MAIN) ||
               nextLA(0) == TokenType.PURE ||
               nextLA(0) == TokenType.RECURS)
                function();

        mainFunc();

        if(nextLA(0) != TokenType.EOF)
            System.out.println("EOF Not Reached.");
    }

    /*
    ------------------------------------------------------------
                          ENUM DECLARATION
    ------------------------------------------------------------
    */


    //TODO: enum-type
    // 2. enum_type ::= 'def' ID type? 'type' '=' '{' enum_field (',', enum_field)* '}'
    private void enumType() {
        match(TokenType.DEF);
        match(TokenType.ID);

        match(TokenType.TYPE);
        match(TokenType.LBRACE);
        match(TokenType.RBRACE);

    }


    // 3. enum_field : 'ID' ( '=' constant )?
    private void  enumField() {
        match(TokenType.ID);
        if(nextLA(TokenType.EQ, 0)) {
            match(TokenType.EQ);
            constant();
        }
    }

    /*
    ------------------------------------------------------------
              GLOBAL VARIABLES AND VARIABLE DECLARATIONS
    ------------------------------------------------------------
    */


    // 3. global_variable ::= 'def' ('const' | 'global') variable_decl ;
    private void globalVariable() {
        match(TokenType.DEF);
        if(nextLA(TokenType.CONST,0))
            match(TokenType.CONST);
        else
            match(TokenType.GLOBAL);
        variableDecl();
    }


    // 4. variable_decl ::= variable_decl_list;
    private void variableDecl() { variableDeclList(); }


    // 5. variable_decl_list ::= variable_decl_init ( ',' variable_decl_init )* type ;
    private void variableDeclList() {
        variableDeclInit();
        while(nextLA(TokenType.COMMA, 0)) {
            match(TokenType.COMMA);
            variableDeclInit();
        }
        type();
    }


    // 6. variable_decl_init : ID ( '=' ( expression | 'uninit' ) )? ;
    private void variableDeclInit() {
        match(TokenType.ID);
        if(nextLA(TokenType.EQ,0)) {
            match(TokenType.EQ);
            if(nextLA(TokenType.UNINIT,0))
                match(TokenType.UNINIT);
            else
                expression();
        }
    }

    /*
    ------------------------------------------------------------
                                TYPES
    ------------------------------------------------------------
    */


    // 7. type ::= scalar-type | class-name | 'List' '[' type ']' | 'Tuple' '<' type ( ',' type )* '>'
    private void type() {

        if(nextLA(TokenType.ID, 0))
            className();
        else if(nextLA(TokenType.LIST,0)) {
            match(TokenType.LIST);
            match(TokenType.LBRACK);
            type();
            match(TokenType.RBRACK);
        }
        else if(nextLA(TokenType.TUPLE,0)) {
            match(TokenType.TUPLE);
            match(TokenType.LT);
            type();
            while(nextLA(TokenType.COMMA,0)) {
                match(TokenType.COMMA);
                type();
            }
            match(TokenType.GT);
        }
        else scalarType();
    }


    // 8. scalar-type ::= discrete-type
    //                  | 'String' ( '[' INT-LITERAL ']' )*
    //                  | 'Real' ( ':' INT-LITERAL )? ( '[' INT-LITERAL ']' )*
    private void scalarType() {
        if(nextLA(TokenType.STRING,0)) {
            match(TokenType.STRING);
            while(nextLA(TokenType.LBRACK,0)) {
                match(TokenType.LBRACK);
                match(TokenType.INT_LIT);
                match(TokenType.RBRACK);
            }
        }
        else if(nextLA(TokenType.REAL,0)) {
            match(TokenType.REAL);
            if(nextLA(TokenType.COLON,0)) {
                match(TokenType.COLON);
                match(TokenType.INT_LIT);
            }
            while(nextLA(TokenType.LBRACK,0)) {
                match(TokenType.LBRACK);
                match(TokenType.INT_LIT);
                match(TokenType.RBRACK);
            }
        }
        else discreteType();
    }


    // 9. discrete-type ::= 'Bool' ( '[' INT-LITERAL ']' )*
    //                    | 'Int' ( ':' INT-LITERAL '..' INT-LITERAL )? ( '[' INT-LITERAL ']' )*
    //                    | 'Char' ( ':' CHAR-LITERAL '..' CHAR-LITERAL )? ( '[' INT-LITERAL ']' )*
    private void discreteType() {
        if(nextLA(TokenType.BOOL,0)) {
            match(TokenType.BOOL);
            while(nextLA(TokenType.LBRACK,0)) {
                match(TokenType.LBRACK);
                match(TokenType.INT_LIT);
                match(TokenType.RBRACK);
            }
        }
        else if(nextLA(TokenType.INT,0)) {
            match(TokenType.INT);
            if(nextLA(TokenType.COLON,0)) {
                match(TokenType.COLON);
                match(TokenType.INT_LIT);
                match(TokenType.INC);
                match(TokenType.INT_LIT);
            }
            while(nextLA(TokenType.LBRACK,0)) {
                match(TokenType.LBRACK);
                match(TokenType.INT_LIT);
                match(TokenType.RBRACK);
            }
        }
        else if(nextLA(TokenType.CHAR,0)) {
            match(TokenType.CHAR);
            if(nextLA(TokenType.COLON,0)) {
                match(TokenType.COLON);
                match(TokenType.CHAR_LIT);
                match(TokenType.INC);
                match(TokenType.CHAR_LIT);
            }
            while(nextLA(TokenType.LBRACK,0)) {
                match(TokenType.LBRACK);
                match(TokenType.INT_LIT);
                match(TokenType.RBRACK);
            }
        }
        else throw new IllegalStateException("Error! Invalid type entered!");
    }


    // 10. class-name ::= ID ( '<' type ( ',' type )* '>' )?
    private void className() {
        match(TokenType.ID);
        if(nextLA(TokenType.LT,0)) {
            match(TokenType.LT);
            type();
            while(nextLA(TokenType.COMMA,0)) {
                match(TokenType.COMMA);
                type();
            }
            match(TokenType.GT);
        }
    }

    /*
    ------------------------------------------------------------
                          CLASS DECLARATION
    ------------------------------------------------------------
    */

    // 11. class_type ::= ( 'abstr' | 'final' ) 'class' ID type_params? super_class? class_body
    private void classType() {
        if(nextLA(TokenType.ABSTR,0))
            match(TokenType.ABSTR);
        else match(TokenType.FINAL);

        match(TokenType.CLASS);
        match(TokenType.ID);

        if(nextLA(TokenType.LT,0))
            typeParams();

        if(nextLA(TokenType.INHERITS,0))
            superClass();

        classBody();
    }

    // 12. type_params ::= '<' type ( ',' type )* '>'
    private void typeParams() {
        match(TokenType.LT);
        type();
        while(nextLA(TokenType.COMMA,0)) {
            match(TokenType.COMMA);
            type();
        }
        match(TokenType.GT);
    }

    // 13. super_class ::= 'inherits' ID type_params?
    private void superClass() {
        match(TokenType.INHERITS);
        match(TokenType.ID);
        if(nextLA(TokenType.LT,0))
            typeParams();
    }

    // 14. class_body ::= '{' data_decl* method_decl* '}'
    private void classBody() {
        match(TokenType.LBRACE);
        match(TokenType.RBRACE);
    }

    // 14. body-decl ::= protected {data-field* method*} public {data-field* method*}
//    private void bodyDecl() {
//        match(TokenType.PROTECTED);
//        match(TokenType.LBRACE);
//        while(nextLA(0) == TokenType.REF || nextLA(0) == TokenType.DEF)
//            dataField();
//
//        while(nextLA(0) == TokenType.FINAL ||
//              nextLA(0) == TokenType.PURE ||
//              nextLA(0) == TokenType.RECURS ||
//              nextLA(0) == TokenType.OVERRIDE ||
//              nextLA(0) == TokenType.METHOD ||
//              nextLA(0) == TokenType.OPERATOR)
//                method();
//        match(TokenType.RBRACE);
//
//        match(TokenType.PUBLIC);
//        match(TokenType.LBRACE);
//
//        while(nextLA(0) == TokenType.REF || nextLA(0) == TokenType.DEF)
//            dataField();
//
//        while(nextLA(0) == TokenType.FINAL ||
//                nextLA(0) == TokenType.PURE ||
//                nextLA(0) == TokenType.RECURS ||
//                nextLA(0) == TokenType.OVERRIDE ||
//                nextLA(0) == TokenType.METHOD ||
//                nextLA(0) == TokenType.OPERATOR)
//            method();
//        match(TokenType.RBRACE);
//    }

    /*
    ------------------------------------------------------------
                          FIELD DECLARATION
    ------------------------------------------------------------
    */

    // data_decl ::= ( 'property' | 'protected' | 'public' ) variable_decl
    private void dataDecl() {
        if(nextLA(TokenType.PROPERTY,0))
            match(TokenType.PROPERTY);
        else if(nextLA(TokenType.PROTECTED,0))
            match(TokenType.PROTECTED);
        else
            match(TokenType.PUBLIC);

        variableDecl();
    }

    /*
    ------------------------------------------------------------
                          METHOD DECLARATION
    ------------------------------------------------------------
    */
    // method_decl ::= method_class | operator_class
    private void methodDecl() {
        if(nextLA(TokenType.OPERATOR,1) || nextLA(TokenType.OPERATOR,2))
            operatorClass();
        else methodClass();
    }


    // method_class ::= method_modifier attribute* 'override'? 'method' method_header '=>' return_type block_statement
    private void methodClass() {
        methodModifier();

        while(nextLA(TokenType.FINAL,0) || nextLA(TokenType.PURE,0) || nextLA(TokenType.RECURS,0))
            attribute();

        if(nextLA(TokenType.OVERRIDE,1))
            match(TokenType.OVERRIDE);

        match(TokenType.METHOD);

        methodHeader();

        match(TokenType.EQ);
        match(TokenType.GT);

        returnType();
        blockStatement();
    }


    //  method_modifier : 'protected' | 'public' ;
    private void methodModifier() {
        if(nextLA(TokenType.PROTECTED,0))
            match(TokenType.PROTECTED);
        else if(nextLA(TokenType.PUBLIC,0))
            match(TokenType.PUBLIC);
        else throw new IllegalStateException("Error! Invalid method modifier.");
    }


    private void attribute() {
        if(nextLA(TokenType.FINAL,0))
            match(TokenType.FINAL);
        else if(nextLA(TokenType.PURE,0))
            match(TokenType.PURE);
        else if(nextLA(TokenType.RECURS,0))
            match(TokenType.RECURS);
        else throw new IllegalStateException("Error! Invalid attribute specified.");
    }


    // 18. method-header ::= ID '(' formal-params? ')'
    private void methodHeader() {
        match(TokenType.ID);
        match(TokenType.LPAREN);
        if( nextLA(TokenType.IN,0)    ||
            nextLA(TokenType.OUT,0)   ||
            nextLA(TokenType.INOUT,0) ||
            nextLA(TokenType.REF,0))
                formalParams();
        match(TokenType.RPAREN);
    }


    // formal_params : param_modifier Name type ( ',' param_modifier Name type)*
    private void formalParams() {
        paramModifier();
        match(TokenType.ID);
        type();

        while(nextLA(TokenType.COMMA,0)) {
            match(TokenType.COMMA);
            paramModifier();
            match(TokenType.ID);
            type();
        }
    }


    // param_modifier : 'in' | 'out' | 'inout' | 'ref' ;
    private void paramModifier() {
        if(nextLA(TokenType.IN,0))
            match(TokenType.IN);
        else if(nextLA(TokenType.OUT,0))
            match(TokenType.OUT);
        else if(nextLA(TokenType.INOUT,0))
            match(TokenType.INOUT);
        else if(nextLA(TokenType.IN,0) && nextLA(TokenType.OUT,0)) {
            match(TokenType.IN);
            match(TokenType.OUT);
        }
        else if(nextLA(TokenType.REF,0))
            match(TokenType.REF);
        else throw new IllegalStateException("Error! Invalid parameter type was given.");
    }


    // 21. return-type ::= Void | type
    private void returnType() {
        if(nextLA(TokenType.VOID,0))
            match(TokenType.VOID);
        else type();
    }


    // operator_class : operator_modifier 'final'? 'operator' operator_header '=>' return_type block_statement ;
    private void operatorClass() {
        methodModifier();

        if(nextLA(TokenType.FINAL,0))
            match(TokenType.FINAL);

        match(TokenType.OPERATOR);
        operatorHeader();

        match(TokenType.EQ);
        match(TokenType.GT);

        returnType();
        blockStatement();
    }


    // 23. operator-header ::= operator-declaration (formal-params?)
    private void operatorHeader() {
        operatorSymbol();

        match(TokenType.LPAREN);
        if( nextLA(TokenType.IN,0)    ||
            nextLA(TokenType.OUT,0)   ||
            nextLA(TokenType.INOUT,0) ||
            nextLA(TokenType.REF,0))
            formalParams();
        match(TokenType.RPAREN);
    }


    // 24. operator-declaration ::= binary-operator | unary-operator
    private void operatorSymbol() {
        if(nextLA(TokenType.TILDE,0) || nextLA(TokenType.NOT,0))
            unaryOperator();
        else
            binaryOperator();
    }


    // 25. binary-operator ::= <= | < | > | >= | == | <> | <=> | <: | :> | + | - | * | / | % | **
    private void binaryOperator() {
        if(nextLA(TokenType.LTEQ,0))
            match(TokenType.LTEQ);
        else if(nextLA(TokenType.LT,0))
            match(TokenType.LT);
        else if(nextLA(TokenType.GT,0))
            match(TokenType.GT);
        else if(nextLA(TokenType.GTEQ,0))
            match(TokenType.GTEQ);
        else if(nextLA(TokenType.EQEQ,0))
            match(TokenType.EQEQ);
        else if(nextLA(TokenType.LTGT,0))
            match(TokenType.LTGT);
        else if(nextLA(TokenType.LTEQGT,0))
            match(TokenType.LTEQGT);
        else if(nextLA(TokenType.MIN,0))
            match(TokenType.MIN);
        else if(nextLA(TokenType.MAX,0))
            match(TokenType.MAX);
        else if(nextLA(TokenType.PLUS,0))
            match(TokenType.PLUS);
        else if(nextLA(TokenType.MINUS,0))
            match(TokenType.MINUS);
        else if(nextLA(TokenType.MULT,0))
            match(TokenType.MULT);
        else if(nextLA(TokenType.DIV,0))
            match(TokenType.DIV);
        else if(nextLA(TokenType.MOD,0))
            match(TokenType.MOD);
        else if(nextLA(TokenType.EXP,0))
            match(TokenType.EXP);
        else throw new IllegalStateException("Error! Invalid Binary Operator");
    }


    // 26. unary-operator ::= ~ | not
    private void unaryOperator() {
        if(nextLA(TokenType.TILDE,0))
            match(TokenType.TILDE);
        else match(TokenType.NOT);
    }


    /*
    ------------------------------------------------------------
                        FUNCTION DECLARATION
    ------------------------------------------------------------
    */


    // 27. function ::= (pure | recurs)? def function-header => return-type block-statement
    private void function() {
        match(TokenType.DEF);

        if(nextLA(TokenType.PURE,0))
            match(TokenType.PURE);
        else if(nextLA(TokenType.RECURSE,0))
            match(TokenType.RECURS);

        functionHeader();

        match(TokenType.EQ);
        match(TokenType.GT);

        returnType();
        blockStatement();
    }


    // 28. function-header ::= ID function-type-params? (formal-params?)
    private void functionHeader() {
        match(TokenType.ID);

        if(nextLA(TokenType.LT,0))
            functionTypeParams();

        match(TokenType.LPAREN);

        if( nextLA(TokenType.IN,0)    ||
            nextLA(TokenType.OUT,0)   ||
            nextLA(TokenType.INOUT,0) ||
            nextLA(TokenType.REF,0))
                formalParams();

        match(TokenType.RPAREN);
    }


    // 29. function-type-params ::= <typeifier (, typeifier)*)>
    private void functionTypeParams() {
        match(TokenType.LT);
        typeifier();

        while(nextLA(TokenType.COMMA,0)) {
            match(TokenType.COMMA);
            typeifier();
        }
        match(TokenType.GT);
    }


    // 30. typeifier ::= (discr | scalar | class)? ID
    private void typeifier() {
        if(nextLA(TokenType.DISCR,0))
            match(TokenType.DISCR);
        else if(nextLA(TokenType.SCALAR,0))
            match(TokenType.SCALAR);
        else if(nextLA(TokenType.CLASS,0))
            match(TokenType.CLASS);

        match(TokenType.ID);
    }

    /*
    ------------------------------------------------------------
                          MAIN FUNCTION
    ------------------------------------------------------------
    */

    // 31. main ::= def main block-statement
    private void mainFunc() {
        match(TokenType.DEF);
        match(TokenType.MAIN);
        if(nextLA(TokenType.LPAREN,0))
            args();

        blockStatement();
    }


    private void args() {
        match(TokenType.LPAREN);
        if( nextLA(TokenType.IN,0)    ||
                nextLA(TokenType.OUT,0)   ||
                nextLA(TokenType.INOUT,0) ||
                nextLA(TokenType.REF,0))
            formalParams();
        match(TokenType.RPAREN);
    }


    // 32. block-statement ::= {declaration* statement*}
    private void blockStatement() {
        match(TokenType.LBRACE);
        while(nextLA(0) == TokenType.LOCAL || nextLA(0) == TokenType.DEF)
            declaration();

        while(nextLA(0) == TokenType.STOP ||
                nextLA(0) == TokenType.RETURN ||
                nextLA(0) == TokenType.SET ||
                nextLA(0) == TokenType.LBRACE ||
                nextLA(0) == TokenType.IF ||
                nextLA(0) == TokenType.FOR ||
                nextLA(0) == TokenType.LOOP ||
                nextLA(0) == TokenType.CHOICE ||
                nextLA(0) == TokenType.APPEND ||
                nextLA(0) == TokenType.REMOVE ||
                nextLA(0) == TokenType.INSERT ||
                nextLA(0) == TokenType.CIN ||
                nextLA(0) == TokenType.COUT)
            statement();

        match(TokenType.RBRACE);
    }

    // 33. declaration ::= local? variable-decl | ref variable-decl
    private void declaration() {
        match(TokenType.DEF);

        if (nextLA(TokenType.LOCAL, 0))
            match(TokenType.LOCAL);

        variableDecl();
    }


    /*
    ------------------------------------------------------------
                             STATEMENTS
    ------------------------------------------------------------
    */

    // 34. statement ::= stop | return-statement | assignment-statement | block-statement | if-statement
    //                 | iterator-statement | loop-statement | choice-statement | list-command-statement | input-output-statement
    private void statement() {
        if(nextLA(TokenType.STOP,0))
            match(TokenType.STOP);
        else if(nextLA(TokenType.RETURN,0))
            returnStatement();
        else if(nextLA(TokenType.SET,0))
            assignmentStatement();
        else if(nextLA(TokenType.LBRACE,0))
            blockStatement();
        else if(nextLA(TokenType.IF,0))
            ifStatement();
//        else if(nextLA(0) == TokenType.FOR)
//            iteratorStatement();
//        else if(nextLA(0) == TokenType.LOOP)
//            loopStatement();
        else if(nextLA(TokenType.CHOICE,0))
            choiceStatement();
//        else if(nextLA(0) == TokenType.APPEND ||
//                nextLA(0) == TokenType.REMOVE ||
//                nextLA(0) == TokenType.INSERT)
//            listCommandStatement();
        else if(nextLA(TokenType.CIN,0))
            inputStatement();
        else if(nextLA(TokenType.COUT,0))
            outputStatement();
        else throw new IllegalStateException("Error! Invalid Statement!");
    }

    // 35. return-statement ::= return expression?
    private void returnStatement() {
        match(TokenType.RETURN);
        // FIRST SET HERE
        if( nextLA(0) == TokenType.CAST ||
            nextLA(0) == TokenType.LENGTH ||
            nextLA(0) == TokenType.SLICE||
            nextLA(0) == TokenType.ID ||
            nextLA(0) == TokenType.LPAREN ||
            nextLA(0) == TokenType.LBRACK ||
            nextLA(0) == TokenType.ARRAY ||
            nextLA(0) == TokenType.LIST ||
            nextLA(0) == TokenType.TUPLE ||
            nextLA(0) == TokenType.STR_LIT ||
            nextLA(0) == TokenType.TEXT_LIT ||
            nextLA(0) == TokenType.REAL_LIT ||
            nextLA(0) == TokenType.INT_LIT ||
            nextLA(0) == TokenType.CHAR_LIT ||
            nextLA(0) == TokenType.BOOL_LIT)
                expression();
    }

    // 36. assignment-statement ::= set ID = argument-value
    private void assignmentStatement() {
        match(TokenType.SET);
        match(TokenType.ID);
        match(TokenType.EQ);
        argumentValue();
    }

    // 37. argument-value ::= ref expression | expression
    private void argumentValue() {
        if(nextLA(0) == TokenType.REF) {
            match(TokenType.REF);
            expression();
        }
        else expression();
    }

    // 38. if-statement ::= if expression block-statement (elseif-statement)* (else block-statement)?
    private void ifStatement() {
        match(TokenType.IF);

        expression();
        blockStatement();

        while(nextLA(0) == TokenType.ELSE && nextLA(1) == TokenType.IF)
            elseIfStatement();

        if(nextLA(0) == TokenType.ELSE) {
            match(TokenType.ELSE);
            blockStatement();
        }
    }

    // 39. elseif-statement ::= else if expression block-statement
    private void elseIfStatement() {
        match(TokenType.ELSE);
        match(TokenType.IF);
        expression();
        blockStatement();
    }

    // 40. iterator-statement ::= for (range-iterator | array-iterator) block-statement
    private void iteratorStatement() {
        match(TokenType.FOR);
        rangeIterator();
        // Choose between range/array iterators
        blockStatement();
    }
    // TODO: Figure out how to tell between range/array iterator
    // 41. range-iterator ::= ID in expression range-operator expression
    private void rangeIterator() {
        match(TokenType.ID);
        match(TokenType.IN);
        expression();
        rangeOperator();
        expression();
    }

    // 42. array-iterator ::= ID (in | inrev) expression
    private void arrayIterator() {
        match(TokenType.ID);
        if(nextLA(0) == TokenType.IN)
            match(TokenType.IN);
        else if(nextLA(0) == TokenType.INREV)
            match(TokenType.INREV);
        else throw new IllegalStateException("Error! Invalid syntax");
        expression();
    }

    // 43. range-operator ::= inclusive | exclusive-right | exclusive-left | exclusive
    private void rangeOperator() {
        if(nextLA(0) == TokenType.INC && nextLA(1) == TokenType.LT)
            exclusiveRight();
        else if(nextLA(0) == TokenType.INC)
            inclusive();
        else if(nextLA(0) == TokenType.LT && nextLA(1) == TokenType.INC
                && nextLA(2) == TokenType.LT)
            exclusive();
        else
            exclusiveLeft();
    }

    // 44. inclusive ::= ..
    private void inclusive() {
        match(TokenType.INC);
    }

    // 45. exclusive-right ::= ..<
    private void exclusiveRight() {
        match(TokenType.INC);
        match(TokenType.LT);
    }

    // 46. exclusive-left ::= <..
    private void exclusiveLeft() {
        match(TokenType.LT);
        match(TokenType.INC);
    }

    // 47. exclusive ::= <..<
    private void exclusive() {
        match(TokenType.LT);
        match(TokenType.INC);
        match(TokenType.LT);
    }

    // 48. loop-statement ::= loop { declaration* statement* until (expression) statement* }
    private void loopStatement() {
        match(TokenType.LOOP);
        match(TokenType.LBRACE);
        while(nextLA(0) == TokenType.DEF ||
              nextLA(0) == TokenType.REF ||
              nextLA(0) == TokenType.LOCAL)
                declaration();

        while(nextLA(0) == TokenType.STOP ||
                nextLA(0) == TokenType.RETURN ||
                nextLA(0) == TokenType.SET ||
                nextLA(0) == TokenType.LBRACE ||
                nextLA(0) == TokenType.IF ||
                nextLA(0) == TokenType.FOR ||
                nextLA(0) == TokenType.LOOP ||
                nextLA(0) == TokenType.CHOICE ||
                nextLA(0) == TokenType.APPEND ||
                nextLA(0) == TokenType.REMOVE ||
                nextLA(0) == TokenType.INSERT ||
                nextLA(0) == TokenType.CIN ||
                nextLA(0) == TokenType.COUT)
                statement();

        match(TokenType.UNTIL);
        match(TokenType.LPAREN);
        expression();
        match(TokenType.RPAREN);

        while(nextLA(0) == TokenType.STOP ||
                nextLA(0) == TokenType.RETURN ||
                nextLA(0) == TokenType.SET ||
                nextLA(0) == TokenType.LBRACE ||
                nextLA(0) == TokenType.IF ||
                nextLA(0) == TokenType.FOR ||
                nextLA(0) == TokenType.LOOP ||
                nextLA(0) == TokenType.CHOICE ||
                nextLA(0) == TokenType.APPEND ||
                nextLA(0) == TokenType.REMOVE ||
                nextLA(0) == TokenType.INSERT ||
                nextLA(0) == TokenType.CIN ||
                nextLA(0) == TokenType.COUT)
            statement();

        match(TokenType.RBRACE);
    }

    // 49. choice-statement ::= choice expression { case-statement* other block-statement }
    private void choiceStatement() {
        match(TokenType.CHOICE);
        expression();
        match(TokenType.LBRACE);
        while(nextLA(0) == TokenType.ON)
            caseStatement();
        match(TokenType.OTHER);
        blockStatement();
        match(TokenType.RBRACE);
    }

    // 50. case-statement ::= on label block-statement
    private void caseStatement() {
        match(TokenType.ON);
        label();
        blockStatement();
    }

    // 51. label ::= constant (.. constant)?
    private void label() {
        constant();
        if(nextLA(0) == TokenType.INC) {
            match(TokenType.INC);
            constant();
        }
    }

    // 52. list-command-statement ::= append (argument-list) | remove (argument-list) | insert (argument-list)
    private void listCommandStatement() {
        if(nextLA(0) == TokenType.APPEND) {
            match(TokenType.APPEND);
            match(TokenType.LPAREN);
            argumentList();
            match(TokenType.RPAREN);
        }
        else if(nextLA(0) == TokenType.REMOVE) {
            match(TokenType.REMOVE);
            match(TokenType.LPAREN);
            argumentList();
            match(TokenType.RPAREN);
        }
        else if(nextLA(0) == TokenType.INSERT) {
            match(TokenType.INSERT);
            match(TokenType.LPAREN);
            argumentList();
            match(TokenType.RPAREN);
        }
        else throw new IllegalStateException("Error! Invalid List command!");
    }

    // 53. argument-list ::= expression ( , expression )*
    private void argumentList() {
        expression();
        while (nextLA(0) == TokenType.COMMA) {
            match(TokenType.COMMA);
            expression();
        }
    }

    // 54. input-output-statement ::= input-statement | output-statement
    private void inputOutputStatement() {
        if(nextLA(0) == TokenType.CIN)
            inputStatement();
        else if(nextLA(0) == TokenType.COUT)
            outputStatement();
        else throw new IllegalStateException("Error! Invalid I/O Statement.");
    }

    // 55. input-statement ::= cin ( >> expression+ )+
    private void inputStatement() {
        match(TokenType.CIN);
        if(nextLA(0) == TokenType.SE) {
            while(nextLA(0) == TokenType.SE) {
                match(TokenType.SE);
                expression();
            }
        }
        else throw new IllegalStateException("Error! Invalid cin statement");
    }

    // 56. output-statement ::= cout (<< expression)+
    private void outputStatement() {
        match(TokenType.COUT);
        if(nextLA(0) == TokenType.SI) {
            while(nextLA(0) == TokenType.SI) {
                match(TokenType.SI);
                expression();
            }
        }
    }

    /*
    ------------------------------------------------------------
                            EXPRESSIONS
    ------------------------------------------------------------
    */

    // 54. expression ::= and-expression ( or and-expression)*
    private void expression() {
        andExpression();
        while(nextLA(0) == TokenType.OR) {
            match(TokenType.OR);
            andExpression();
        }
    }

    // 55. and-expression ::= equal-expression ( and equal-expression)*
    private void andExpression() {
        equalExpression();
        while(nextLA(0) == TokenType.AND) {
            match(TokenType.AND);
            equalExpression();
        }
    }

    // 56. equal-expression ::= relational-expression ((== | <>) relational-expression)*
    private void equalExpression() {
        relationalExpression();
        while(nextLA(0) == TokenType.EQEQ ||
              nextLA(0) == TokenType.NEQ) {
                if(nextLA(0) == TokenType.EQEQ) {
                    match(TokenType.EQEQ);
                    relationalExpression();
                }
                else if(nextLA(0) == TokenType.NEQ) {
                    match(TokenType.NEQ);
                    relationalExpression();
                }
        }
    }

    // 57. relational-expression ::= minmax-expression (relational-operator minmax-expression)*
    private void relationalExpression() {
        minMaxExpression();
        while(nextLA(0) == TokenType.LTEQ ||
              nextLA(0) == TokenType.LT ||
              nextLA(0) == TokenType.GT ||
              nextLA(0) == TokenType.GTEQ ||
              nextLA(0) == TokenType.LTEQGT ||
              nextLA(0) == TokenType.INSTANCEOF) {
            relationalOperator();
            minMaxExpression();
        }
    }

    // 58. relational-operator ::= <= | < | > | >= | <=> | instanceof
    private void relationalOperator() {
        if(nextLA(0) == TokenType.LTEQ)
            match(TokenType.LTEQ);
        else if(nextLA(0) == TokenType.LT)
            match(TokenType.LT);
        else if(nextLA(0) == TokenType.GT)
            match(TokenType.GT);
        else if(nextLA(0) == TokenType.GTEQ)
            match(TokenType.GTEQ);
        else if(nextLA(0) == TokenType.LTEQGT)
            match(TokenType.LTEQGT);
        else if(nextLA(0) == TokenType.INSTANCEOF)
            match(TokenType.INSTANCEOF);
        else throw new IllegalStateException("Error! Invalid Relational Operator!");
    }

    // 59. minmax-expression ::= additive-expression ( (<: | :>) additive-expression)*
    private void minMaxExpression() {
        additiveExpression();
        while(nextLA(0) == TokenType.MIN||
                nextLA(0) == TokenType.MAX) {
            if(nextLA(0) == TokenType.MIN) {
                match(TokenType.MIN);
                additiveExpression();
            }
            else if(nextLA(0) == TokenType.MAX) {
                match(TokenType.MAX);
                additiveExpression();
            }
        }
    }

    // 60. additive-expression ::= multiplicative-expression ( (+ | -) multiplicative-expression)*
    private void additiveExpression() {
        multiplicativeExpression();
        while(nextLA(0) == TokenType.PLUS ||
              nextLA(0) == TokenType.MINUS) {
                if(nextLA(0) == TokenType.PLUS) {
                    match(TokenType.PLUS);
                    multiplicativeExpression();
                }
                else if(nextLA(0) == TokenType.MINUS) {
                    match(TokenType.MINUS);
                    multiplicativeExpression();
                }
        }
    }

    // 61. multiplicative-expression ::= power-expression ( (*|/|%) power-expression)*
    private void multiplicativeExpression() {
        powerExpression();
        while(nextLA(0) == TokenType.MULT ||
              nextLA(0) == TokenType.DIV  ||
              nextLA(0) == TokenType.MOD) {
                if(nextLA(0) == TokenType.MULT) {
                    match(TokenType.MULT);
                    powerExpression();
                }
                else if(nextLA(0) == TokenType.DIV) {
                    match(TokenType.DIV);
                    powerExpression();
                }
                else if(nextLA(0) == TokenType.MOD) {
                    match(TokenType.MOD);
                    powerExpression();
                }
        }
    }

    // 62. power-expression ::= unary-expression (** unary-expression)*
    private void powerExpression() {
        unaryExpression();
        while(nextLA(0) == TokenType.EXP) {
            match(TokenType.EXP);
            unaryExpression();
        }
    }

    // 63. unary-expression ::= ( ~ | not ) unary-expression | cast-expression
    private void unaryExpression() {
        if(nextLA(0) == TokenType.TILDE ||
        nextLA(0) == TokenType.NOT) {
            if(nextLA(0) == TokenType.TILDE)
                match(TokenType.TILDE);
            else
                match(TokenType.NOT);
            unaryExpression();
        }
        else
            castExpression();
    }

    // 64. cast-expression ::= cast (type, expression) | list-expression
    private void castExpression() {
        if(nextLA(0) == TokenType.CAST) {
            match(TokenType.CAST);
            match(TokenType.LPAREN);
            type();
            match(TokenType.COMMA);
            expression();
            match(TokenType.RPAREN);
        }
        else
            listExpression();

    }

    // 65. list-expression ::= length (expression) | slice (expression, expression range-operator expression) | call-expression
    private void listExpression() {
        if(nextLA(0) == TokenType.LENGTH) {
            match(TokenType.LENGTH);
            match(TokenType.LPAREN);
            expression();
            match(TokenType.RPAREN);
        }
        else if(nextLA(0) == TokenType.SLICE) {
            match(TokenType.SLICE);
            match(TokenType.LPAREN);
            expression();
            match(TokenType.COMMA);
            expression();
            rangeOperator();
            expression();
            match(TokenType.RPAREN);
        }
        else
            callExpression();
    }


    // 75. call-expression ::= primary ( ( argument-list? ) | .ID | [ expression ] )*
    private void callExpression() {
            primary();
            while(nextLA(0) == TokenType.LPAREN ||
                    nextLA(0) == TokenType.PERIOD ||
                    nextLA(0) == TokenType.LBRACK) {
                if (nextLA(0) == TokenType.LPAREN) {
                    match(TokenType.LPAREN);
                    if( nextLA(0) == TokenType.CAST ||
                        nextLA(0) == TokenType.LENGTH ||
                        nextLA(0) == TokenType.SLICE||
                        nextLA(0) == TokenType.ID ||
                        nextLA(0) == TokenType.LPAREN ||
                        nextLA(0) == TokenType.LBRACK ||
                        nextLA(0) == TokenType.ARRAY ||
                        nextLA(0) == TokenType.LIST ||
                        nextLA(0) == TokenType.TUPLE ||
                        nextLA(0) == TokenType.STR_LIT ||
                        nextLA(0) == TokenType.TEXT_LIT ||
                        nextLA(0) == TokenType.REAL_LIT ||
                        nextLA(0) == TokenType.INT_LIT ||
                        nextLA(0) == TokenType.CHAR_LIT ||
                        nextLA(0) == TokenType.BOOL_LIT)
                        argumentList();
                    match(TokenType.RPAREN);
                } else if (nextLA(0) == TokenType.PERIOD) {
                    match(TokenType.PERIOD);
                    match(TokenType.ID);
                } else if (nextLA(0) == TokenType.LBRACK) {
                    match(TokenType.LBRACK);
                    expression();
                    match(TokenType.RBRACK);
                }
            }
    }

    // 67. primary ::= ID | constant | ( expression )
    private void primary() {
        if(nextLA(0) == TokenType.LPAREN) {
            match(TokenType.LPAREN);
            expression();
            match(TokenType.RPAREN);
        }
        else if(nextLA(0) == TokenType.ID)
            match(TokenType.ID);
        else
            constant();
    }

    /*
    ------------------------------------------------------------
                                LITERALS
    ------------------------------------------------------------
    */

    // 67. constant ::= object-constant | array-constant | list-constant | tuple-constant | scalar-constant
    private void constant() {
        if(nextLA(0) == TokenType.NEW)
            objectConstant();
        else if(nextLA(0) == TokenType.ARRAY ||
                nextLA(0) == TokenType.LBRACK)
            arrayConstant();
        else if(nextLA(0) == TokenType.LIST ||
                nextLA(0) == TokenType.LBRACE)
            listConstant();
        else if(nextLA(0) == TokenType.TUPLE ||
                nextLA(0) == TokenType.LPAREN)
            tupleConstant();
        else
            scalarConstant();
    }

    // 68. object-constant ::= new ID ( (object-field (, object-field)*)? )
    private void objectConstant() {
        match(TokenType.NEW);
        match(TokenType.ID);
        match(TokenType.LPAREN);

        if((nextLA(0) == TokenType.ID && nextLA(0) == TokenType.EQ) ||
                nextLA(0) == TokenType.CAST ||
                nextLA(0) == TokenType.LENGTH ||
                nextLA(0) == TokenType.SLICE||
                nextLA(0) == TokenType.ID ||
                nextLA(0) == TokenType.LPAREN ||
                nextLA(0) == TokenType.LBRACK ||
                nextLA(0) == TokenType.ARRAY ||
                nextLA(0) == TokenType.LIST ||
                nextLA(0) == TokenType.TUPLE ||
                nextLA(0) == TokenType.STR_LIT ||
                nextLA(0) == TokenType.TEXT_LIT ||
                nextLA(0) == TokenType.REAL_LIT ||
                nextLA(0) == TokenType.INT_LIT ||
                nextLA(0) == TokenType.CHAR_LIT ||
                nextLA(0) == TokenType.BOOL_LIT) {
                objectField();
            while(nextLA(0) == TokenType.COMMA) {
                match(TokenType.COMMA);
                objectField();
            }
        }
        match(TokenType.RPAREN);
    }

    // 69. object-field ::= (ID =)? expression
    private void objectField() {
        if(nextLA(0) == TokenType.ID) {
            match(TokenType.ID);
            match(TokenType.EQ);
        }

        expression();
    }

    // 70. array-constant ::= Array ([expression])* (expression) | [expression (, expression)*]
    private void arrayConstant() {
        if(nextLA(0) == TokenType.ARRAY) {
            match(TokenType.ARRAY);
            while(nextLA(0) == TokenType.LBRACK) {
                match(TokenType.LBRACK);
                expression();
                match(TokenType.RBRACK);
            }
            match(TokenType.LPAREN);
            expression();
            match(TokenType.RPAREN);
        }
        else {
            match(TokenType.LBRACK);
            expression();
            while(nextLA(0) == TokenType.COMMA) {
                match(TokenType.COMMA);
                expression();
            }
            match(TokenType.RBRACK);
        }
    }

    // 71. list-constant ::= List ( (expression (, expression)*)? ) | {expression (, expression)*}
    private void listConstant() {
        if(nextLA(0) == TokenType.LIST) {
            match(TokenType.LIST);
            match(TokenType.LPAREN);

            while(nextLA(0) == TokenType.LBRACK) {
                match(TokenType.LBRACK);
                expression();
                match(TokenType.RBRACK);
            }
            match(TokenType.LPAREN);
            expression();
            match(TokenType.RPAREN);
        }
        else {
            match(TokenType.LBRACE);
            expression();
            while(nextLA(0) == TokenType.COMMA) {
                match(TokenType.COMMA);
                expression();
            }
            match(TokenType.RBRACE);
        }
    }

    // 72. tuple-constant ::= Tuple ( (expression (, expression)*)? ) | (expression (, expression)*)
    private void tupleConstant() {
        if(nextLA(0) == TokenType.TUPLE) {
            match(TokenType.TUPLE);
            match(TokenType.LPAREN);

            while(nextLA(0) == TokenType.LBRACK) {
                match(TokenType.LBRACK);
                expression();
                match(TokenType.RBRACK);
            }
            match(TokenType.LPAREN);
            expression();
            match(TokenType.RPAREN);
        }
        else {
            match(TokenType.LPAREN);
            expression();
            while(nextLA(0) == TokenType.COMMA) {
                match(TokenType.COMMA);
                expression();
            }
            match(TokenType.RPAREN);
        }
    }

    // 73. scalar-constant ::= discrete-constant | STRING-LITERAL | TEXT-LITERAL | REAL-LITERAL
    private void scalarConstant() {
        if(nextLA(0) == TokenType.STR_LIT)
            match(TokenType.STR_LIT);
        else if(nextLA(0) == TokenType.TEXT_LIT)
            match(TokenType.TEXT_LIT);
        else if(nextLA(0) == TokenType.REAL_LIT)
            match(TokenType.REAL_LIT);
        else
            discreteConstant();
    }

    // 74. discrete-constant ::= INT-LITERAL | CHAR-LITERAL | BOOL-LITERAL
    private void discreteConstant() {
        if(nextLA(0) == TokenType.INT_LIT)
            match(TokenType.INT_LIT);
        else if(nextLA(0) == TokenType.CHAR_LIT)
            match(TokenType.CHAR_LIT);
        else if(nextLA(0) == TokenType.BOOL_LIT)
            match(TokenType.BOOL_LIT);
        else throw new IllegalStateException("Error! Invalid Constant!");
    }

    /*
    ------------------------------------------------------------
                          COMPILER DIRECTIVES
    ------------------------------------------------------------
    */

    // TODO: Is rename optional?
    // 75. file-merge ::= #include filename choice? rename | #exclude choice
    private void fileMerge() {
        if(nextLA(0) == TokenType.INCLUDE) {
            match(TokenType.INCLUDE);
            fileName();
            if(nextLA(0) == TokenType.ONLY)
                choice();
            //rename();
        }
        else if(nextLA(0) == TokenType.EXCLUDE) {
            match(TokenType.EXCLUDE);
            choice();
        }
    }

    // 76. filename ::= STRING-LITERAL
    private void fileName() {
        match(TokenType.STR_LIT);
    }

    // 77. choice ::= only {global-id (, global-id)*} | except {global_id (, global_id)*}
    private void choice() {
        if(nextLA(0) == TokenType.ONLY) {
            match(TokenType.ONLY);
            match(TokenType.LBRACE);
            globalID();
            while(nextLA(0) == TokenType.COMMA) {
                match(TokenType.COMMA);
                globalID();
            }
            match(TokenType.RBRACE);
        }
        else if(nextLA(0) == TokenType.EXCEPT) {
            match(TokenType.EXCEPT);
            match(TokenType.LBRACE);
            globalID();
            while(nextLA(0) == TokenType.COMMA) {
                match(TokenType.COMMA);
                globalID();
            }
            match(TokenType.RBRACE);
        }
    }

    // 78. global-id ::= ID
    private void globalID() {
        match(TokenType.ID);
    }

    // 79. rename ::= rename {change-id (, change-id)*}
    private void rename() {
        match(TokenType.RENAME);
        match(TokenType.LBRACE);
        changeID();
        while(nextLA(0) == TokenType.COMMA) {
            match(TokenType.COMMA);
            changeID();
        }
        match(TokenType.RBRACE);
    }

    // 80. change-id ::= ID => ID
    private void changeID() {
        match(TokenType.ID);
        match(TokenType.EQ);
        match(TokenType.GT);
        match(TokenType.ID);
    }
}

package Parser;

import Lexer.*;
import Token.*;
import java.util.ArrayList;

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
        lookaheads[lookPos].toString();
        lookPos = (lookPos + 1) % k;
    }

    private void match(TokenType expectedTok) {
        if(nextLA(expectedTok)) consume();
        else throw new IllegalArgumentException("\nWarning! Expected " + expectedTok + ", but got " + currentLA(0).getTokenType());
    }

    // currentLA : Returns the current token we are checking
    private Token currentLA(int nextPos) { return lookaheads[(lookPos+nextPos)%k]; }

    // nextLA : Returns the next LA at the specified index
    private boolean nextLA(TokenType expectedTok) { return currentLA(0).getTokenType() == expectedTok; }

    private boolean nextLA(TokenType expectedTok, int nextPos) { return currentLA(nextPos).getTokenType() == expectedTok; }

    private boolean anyLA(ArrayList<TokenType> tokens, int nextPos) {
        for(TokenType expectedTok : tokens) {
            if(nextLA(expectedTok,nextPos)) return true;
        }
        return false;
    }

    private boolean anyLA(ArrayList<TokenType> tokens) {
        for(TokenType expectedTok : tokens) {
            if(nextLA(expectedTok)) return true;
        }
        return false;
    }

    private boolean isEnumFIRST() {
        return isScalarTypeFIRST() ||
               nextLA(TokenType.ID) ||
               nextLA(TokenType.LIST) ||
               nextLA(TokenType.TUPLE);
    }

    private boolean isScalarTypeFIRST() {
        return nextLA(TokenType.STRING) ||
                nextLA(TokenType.REAL) ||
                nextLA(TokenType.BOOL) ||
                nextLA(TokenType.INT) ||
                nextLA(TokenType.CHAR);
    }

    private boolean isDataDeclFIRST() {
        return nextLA(TokenType.PROPERTY) ||
                nextLA(TokenType.PROTECTED) ||
                nextLA(TokenType.PUBLIC);
    }

    private boolean isStatFIRST() {
        return isConstant() ||
               isScalarTypeFIRST() ||
               nextLA(TokenType.LBRACK) ||
               nextLA(TokenType.NEW) ||
               nextLA(TokenType.ID) ||
               nextLA(TokenType.LPAREN) ||
               nextLA(TokenType.NOT) ||
               nextLA(TokenType.TILDE) ||
               nextLA(TokenType.LBRACK) ||
               nextLA(TokenType.RETURN) ||
               nextLA(TokenType.SET) ||
               nextLA(TokenType.IF) ||
               nextLA(TokenType.WHILE) ||
               nextLA(TokenType.FOR) ||
               nextLA(TokenType.DO) ||
               nextLA(TokenType.CHOICE) ||
               nextLA(TokenType.CIN) ||
               nextLA(TokenType.COUT);
    }

    private boolean isPrimExprFIRST() {
        return isConstant() ||
               nextLA(TokenType.ARRAY) ||
               nextLA(TokenType.LIST) ||
               nextLA(TokenType.TUPLE) ||
               nextLA(TokenType.LBRACK) ||
               nextLA(TokenType.LPAREN) ||
               nextLA(TokenType.ID) ||
               nextLA(TokenType.SLICE) ||
               nextLA(TokenType.LENGTH) ||
               nextLA(TokenType.CAST);
    }

    private boolean isAfterPrimExprFIRST() {
        return nextLA(TokenType.LBRACK) ||
                nextLA(TokenType.LPAREN) ||
                nextLA(TokenType.ELVIS) ||
                nextLA(TokenType.PERIOD);
    }

    private boolean isAfterPowerFIRST() {
        return nextLA(TokenType.MULT) ||
                nextLA(TokenType.DIV) ||
                nextLA(TokenType.MOD);
    }

    private boolean isAfterAddFIRST() {
        return nextLA(TokenType.LT) ||
               nextLA(TokenType.GT) ||
               nextLA(TokenType.LTEQ) ||
               nextLA(TokenType.GTEQ);
    }

    private boolean isConstant() {
        return nextLA(TokenType.STR_LIT) ||
                nextLA(TokenType.TEXT_LIT) ||
                nextLA(TokenType.REAL_LIT) ||
                nextLA(TokenType.BOOL_LIT) ||
                nextLA(TokenType.INT_LIT) ||
                nextLA(TokenType.CHAR_LIT);
    }

    /*
    ------------------------------------------------------------
                          COMPILATION UNIT
    ------------------------------------------------------------
    */

    // 1. compilation ::= file_merge* enum_type* global_variable* class_type* function* main
    public void compilation() {
        while(nextLA(TokenType.INCLUDE) || nextLA(TokenType.EXCLUDE))
            fileMerge();

        while(nextLA(TokenType.DEF) && nextLA(TokenType.ID,1))
            enumType();

        while(nextLA(TokenType.DEF) && (nextLA(TokenType.CONST, 1) || nextLA(TokenType.GLOBAL, 1)))
            globalVariable();

        while(nextLA(TokenType.ABSTR) || nextLA(TokenType.FINAL) || nextLA(TokenType.CLASS))
            classType();

        while((nextLA(TokenType.DEF)) && !nextLA(TokenType.MAIN,1))
                function();

        mainFunc();

        if(!nextLA(TokenType.EOF))
            System.out.println("EOF Not Reached.");
    }


    /*
    ------------------------------------------------------------
                          COMPILER DIRECTIVES
    ------------------------------------------------------------
    */


    // 2. file_merge ::= '#include' filename choice? rename?
    //                 | '#exclude' choice
    private void fileMerge() {
        if(nextLA(TokenType.INCLUDE)) {
            match(TokenType.INCLUDE);
            fileName();
            if(nextLA(TokenType.ONLY)) choice();
            if(nextLA(TokenType.RENAME)) rename();
        }
        else {
            match(TokenType.EXCLUDE);
            choice();
        }
    }


    // 3. filename ::= STRING_LITERAL
    private void fileName() { match(TokenType.STR_LIT); }


    // 4. choice ::= 'only' '{' global_id ( ',' global_id )* '}'
    //             | 'except' '{' global_id ( ',' global_id )* '}'
    private void choice() {
        if(nextLA(TokenType.ONLY)) {
            match(TokenType.ONLY);
            match(TokenType.LBRACE);
            globalID();
            while(nextLA(TokenType.COMMA)) {
                match(TokenType.COMMA);
                globalID();
            }
            match(TokenType.RBRACE);
        }
        else {
            match(TokenType.EXCEPT);
            match(TokenType.LBRACE);
            globalID();
            while(nextLA(TokenType.COMMA)) {
                match(TokenType.COMMA);
                globalID();
            }
            match(TokenType.RBRACE);
        }
    }


    // 5. global_id ::= ID
    private void globalID() {
        match(TokenType.ID);
    }


    // 6. rename ::= 'rename' '{' change_id ( ',' change_id )* '}'
    private void rename() {
        match(TokenType.RENAME);
        match(TokenType.LBRACE);
        changeID();
        while(nextLA(TokenType.COMMA)) {
            match(TokenType.COMMA);
            changeID();
        }
        match(TokenType.RBRACE);
    }


    // 7. change-id ::= ID => ID
    private void changeID() {
        match(TokenType.ID);
        match(TokenType.ARROW);
        match(TokenType.ID);
    }


    /*
    ------------------------------------------------------------
                          ENUM DECLARATION
    ------------------------------------------------------------
    */


    // 8. enum_type ::= 'def' ID type? 'type' '=' '{' enum_field ( ',' , enum_field)* '}'
    private void enumType() {
        match(TokenType.DEF);
        match(TokenType.ID);

        if(isEnumFIRST()) type();

        match(TokenType.TYPE);
        match(TokenType.EQ);
        match(TokenType.LBRACE);

        enumField();

        while(nextLA(TokenType.COMMA)) {
            match(TokenType.COMMA);
            enumField();
        }
        match(TokenType.RBRACE);
    }


    // 9. enum_field ::= 'ID' ( '=' constant )?
    private void  enumField() {
        match(TokenType.ID);
        if(nextLA(TokenType.EQ)) {
            match(TokenType.EQ);
            constant();
        }
    }


    /*
    ------------------------------------------------------------
              GLOBAL VARIABLES AND VARIABLE DECLARATIONS
    ------------------------------------------------------------
    */


    // 10. global_variable ::= 'def' ( 'const' | 'global' ) variable_decl
    private void globalVariable() {
        match(TokenType.DEF);
        if(nextLA(TokenType.CONST)) match(TokenType.CONST);
        else match(TokenType.GLOBAL);
        variableDecl();
    }


    // 11. variable_decl ::= variable_decl_list
    private void variableDecl() { variableDeclList(); }


    // 12. variable_decl_list ::= variable_decl_init ( ',' variable_decl_init )* type
    private void variableDeclList() {
        variableDeclInit();
        while(nextLA(TokenType.COMMA)) {
            match(TokenType.COMMA);
            variableDeclInit();
        }
        type();
    }


    // 13. variable_decl_init ::= ID ( '=' ( expression | 'uninit' ) )?
    private void variableDeclInit() {
        match(TokenType.ID);
        if(nextLA(TokenType.EQ)) {
            match(TokenType.EQ);
            if(nextLA(TokenType.UNINIT)) match(TokenType.UNINIT);
            else expression();
        }
    }


    /*
    ------------------------------------------------------------
                                TYPES
    ------------------------------------------------------------
    */


    // 14. type ::= scalar_type
    //            | class_name
    //            | 'List' '[' type ']'
    //            | 'Tuple' '<' type ( ',' type )* '>'
    private void type() {
        if(nextLA(TokenType.ID))
            className();
        else if(nextLA(TokenType.LIST)) {
            match(TokenType.LIST);
            match(TokenType.LBRACK);
            type();
            match(TokenType.RBRACK);
        }
        else if(nextLA(TokenType.TUPLE)) {
            match(TokenType.TUPLE);
            match(TokenType.LT);
            type();
            while(nextLA(TokenType.COMMA)) {
                match(TokenType.COMMA);
                type();
            }
            match(TokenType.GT);
        }
        else scalarType();
    }


    // 15. scalar_type ::= discrete_type
    //                   | 'String' ( '[' INT_LITERAL ']' )*
    //                   | 'Real' ( ':' INT_LITERAL )? ( '[' INT_LITERAL ']' )*
    private void scalarType() {
        if(nextLA(TokenType.STRING)) {
            match(TokenType.STRING);
            while(nextLA(TokenType.LBRACK)) {
                match(TokenType.LBRACK);
                match(TokenType.INT_LIT);
                match(TokenType.RBRACK);
            }
        }
        else if(nextLA(TokenType.REAL)) {
            match(TokenType.REAL);
            if(nextLA(TokenType.COLON)) {
                match(TokenType.COLON);
                match(TokenType.INT_LIT);
            }
            while(nextLA(TokenType.LBRACK)) {
                match(TokenType.LBRACK);
                match(TokenType.INT_LIT);
                match(TokenType.RBRACK);
            }
        }
        else discreteType();
    }


    // 16. discrete_type ::= 'Bool' ( '[' INT_LITERAL ']' )*
    //                     | 'Int' ( ':' INT_LITERAL '..' INT_LITERAL )? ( '[' INT_LITERAL ']' )*
    //                     | 'Char' ( ':' CHAR_LITERAL '..' CHAR_LITERAL )? ( '[' INT_LITERAL ']' )*
    private void discreteType() {
        if(nextLA(TokenType.BOOL)) {
            match(TokenType.BOOL);
            while(nextLA(TokenType.LBRACK)) {
                match(TokenType.LBRACK);
                match(TokenType.INT_LIT);
                match(TokenType.RBRACK);
            }
        }
        else if(nextLA(TokenType.INT)) {
            match(TokenType.INT);
            if(nextLA(TokenType.COLON)) {
                match(TokenType.COLON);
                match(TokenType.INT_LIT);
                match(TokenType.INC);
                match(TokenType.INT_LIT);
            }
            while(nextLA(TokenType.LBRACK)) {
                match(TokenType.LBRACK);
                match(TokenType.INT_LIT);
                match(TokenType.RBRACK);
            }
        }
        else if(nextLA(TokenType.CHAR)) {
            match(TokenType.CHAR);
            if(nextLA(TokenType.COLON)) {
                match(TokenType.COLON);
                match(TokenType.CHAR_LIT);
                match(TokenType.INC);
                match(TokenType.CHAR_LIT);
            }
            while(nextLA(TokenType.LBRACK)) {
                match(TokenType.LBRACK);
                match(TokenType.INT_LIT);
                match(TokenType.RBRACK);
            }
        }
        else throw new IllegalStateException("Error! Invalid type entered!");
    }


    // 17. class_name ::= ID ( '<' type ( ',' type )* '>' )?
    private void className() {
        match(TokenType.ID);
        if(nextLA(TokenType.LT)) {
            match(TokenType.LT);
            type();
            while(nextLA(TokenType.COMMA)) {
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


    // 18. class_type ::= ( 'abstr' | 'final' )? 'class' ID type_params? super_class? class_body
    private void classType() {
        if(nextLA(TokenType.ABSTR)) match(TokenType.ABSTR);
        else if(nextLA(TokenType.FINAL)) match(TokenType.FINAL);

        match(TokenType.CLASS);
        match(TokenType.ID);

        if(nextLA(TokenType.LT)) typeParams();
        if(nextLA(TokenType.INHERITS)) superClass();

        classBody();
    }


    // 19. type_params ::= '<' type ( ',' type )* '>'
    private void typeParams() {
        match(TokenType.LT);
        type();
        while(nextLA(TokenType.COMMA)) {
            match(TokenType.COMMA);
            type();
        }
        match(TokenType.GT);
    }


    // 20. super_class ::= 'inherits' ID type_params?
    private void superClass() {
        match(TokenType.INHERITS);
        match(TokenType.ID);
        if(nextLA(TokenType.LT)) typeParams();
    }


    // 21. class_body ::= '{' data_decl* method_decl* '}'
    private void classBody() {
        match(TokenType.LBRACE);

        while(isDataDeclFIRST() && nextLA(TokenType.ID,1))
            dataDecl();
        while(nextLA(TokenType.PROTECTED) || nextLA(TokenType.PUBLIC))
            methodDecl();

        match(TokenType.RBRACE);
    }



    /*
    ------------------------------------------------------------
                          FIELD DECLARATION
    ------------------------------------------------------------
    */


    // 22. data_decl ::= ( 'property' | 'protected' | 'public' ) variable_decl
    private void dataDecl() {
        if(nextLA(TokenType.PROPERTY)) match(TokenType.PROPERTY);
        else if(nextLA(TokenType.PROTECTED)) match(TokenType.PROTECTED);
        else match(TokenType.PUBLIC);

        variableDecl();
    }


    /*
    ------------------------------------------------------------
                          METHOD DECLARATION
    ------------------------------------------------------------
    */


    // 23. method_decl ::= method_class | operator_class
    private void methodDecl() {
        if(nextLA(TokenType.OPERATOR,1) || nextLA(TokenType.OPERATOR,2))
            operatorClass();
        else methodClass();
    }


    // 24. method_class ::= method_modifier attribute* 'override'? 'method' method_header '=>' return_type block_statement
    private void methodClass() {
        methodModifier();

        while(nextLA(TokenType.FINAL) || nextLA(TokenType.PURE) || nextLA(TokenType.RECURS))
            attribute();

        if(nextLA(TokenType.OVERRIDE)) match(TokenType.OVERRIDE);

        match(TokenType.METHOD);
        methodHeader();
        match(TokenType.ARROW);
        returnType();
        blockStatement();
    }


    // 25. method_modifier : 'protected' | 'public' ;
    private void methodModifier() {
        if(nextLA(TokenType.PROTECTED)) match(TokenType.PROTECTED);
        else if(nextLA(TokenType.PUBLIC)) match(TokenType.PUBLIC);
        else throw new IllegalStateException("Error! Invalid method modifier.");
    }


    // 26. attribute ::= 'final' | 'pure' | 'recurs'
    private void attribute() {
        if(nextLA(TokenType.FINAL)) match(TokenType.FINAL);
        else if(nextLA(TokenType.PURE)) match(TokenType.PURE);
        else match(TokenType.RECURS);
    }


    // 27. method-header ::= ID '(' formal-params? ')'
    private void methodHeader() {
        match(TokenType.ID);
        match(TokenType.LPAREN);
        if(nextLA(TokenType.IN) || nextLA(TokenType.OUT) || nextLA(TokenType.INOUT) || nextLA(TokenType.REF))
                formalParams();
        match(TokenType.RPAREN);
    }


    // 28. formal_params : param_modifier Name type ( ',' param_modifier Name type)*
    private void formalParams() {
        paramModifier();
        match(TokenType.ID);
        type();

        while(nextLA(TokenType.COMMA)) {
            match(TokenType.COMMA);
            paramModifier();
            match(TokenType.ID);
            type();
        }
    }


    // 29. param_modifier : 'in' | 'out' | 'inout' | 'ref'
    private void paramModifier() {
        if(nextLA(TokenType.IN)) match(TokenType.IN);
        else if(nextLA(TokenType.OUT)) match(TokenType.OUT);
        else if(nextLA(TokenType.INOUT)) match(TokenType.INOUT);
        else if(nextLA(TokenType.IN) && nextLA(TokenType.OUT,1)) {
            match(TokenType.IN);
            match(TokenType.OUT);
        }
        else if(nextLA(TokenType.REF)) match(TokenType.REF);
        else throw new IllegalStateException("Error! Invalid parameter type was given.");
    }


    // 30. return-type ::= Void | type
    private void returnType() {
        if(nextLA(TokenType.VOID))
            match(TokenType.VOID);
        else type();
    }


    // 31. operator_class : operator_modifier 'final'? 'operator' operator_header '=>' return_type block_statement
    private void operatorClass() {
        methodModifier();

        if(nextLA(TokenType.FINAL)) match(TokenType.FINAL);

        match(TokenType.OPERATOR);
        operatorHeader();
        match(TokenType.ARROW);
        returnType();
        blockStatement();
    }


    // 32. operator_header ::= operator_symbol '(' formal-params? ')'
    private void operatorHeader() {
        operatorSymbol();

        match(TokenType.LPAREN);
        if(nextLA(TokenType.IN) || nextLA(TokenType.OUT) || nextLA(TokenType.INOUT) || nextLA(TokenType.REF))
            formalParams();
        match(TokenType.RPAREN);
    }


    // 33. operator_symbol ::= binary_operator | unary_operator
    private void operatorSymbol() {
        if(nextLA(TokenType.TILDE) || nextLA(TokenType.NOT))
            unaryOperator();
        else binaryOperator();
    }


    // 34. binary_operator ::= <= | < | > | >= | == | <> | <=> | <: | :> | + | - | * | / | % | **
    private void binaryOperator() {
        if(nextLA(TokenType.LTEQ)) match(TokenType.LTEQ);
        else if(nextLA(TokenType.LT)) match(TokenType.LT);
        else if(nextLA(TokenType.GT)) match(TokenType.GT);
        else if(nextLA(TokenType.GTEQ)) match(TokenType.GTEQ);
        else if(nextLA(TokenType.EQEQ)) match(TokenType.EQEQ);
        else if(nextLA(TokenType.LTGT)) match(TokenType.LTGT);
        else if(nextLA(TokenType.UFO)) match(TokenType.UFO);
        else if(nextLA(TokenType.MIN)) match(TokenType.MIN);
        else if(nextLA(TokenType.MAX)) match(TokenType.MAX);
        else if(nextLA(TokenType.PLUS)) match(TokenType.PLUS);
        else if(nextLA(TokenType.MINUS)) match(TokenType.MINUS);
        else if(nextLA(TokenType.MULT)) match(TokenType.MULT);
        else if(nextLA(TokenType.DIV)) match(TokenType.DIV);
        else if(nextLA(TokenType.MOD)) match(TokenType.MOD);
        else if(nextLA(TokenType.EXP)) match(TokenType.EXP);
        else throw new IllegalStateException("Error! Invalid Binary Operator");
    }


    // 35. unary-operator ::= ~ | not
    private void unaryOperator() {
        if(nextLA(TokenType.TILDE)) match(TokenType.TILDE);
        else match(TokenType.NOT);
    }


    /*
    ------------------------------------------------------------
                        FUNCTION DECLARATION
    ------------------------------------------------------------
    */


    // 36. function ::= 'def' ( 'pure' | 'recurs' )? function_header '=>' return_type block_statement
    private void function() {
        match(TokenType.DEF);

        if(nextLA(TokenType.PURE)) match(TokenType.PURE);
        else if(nextLA(TokenType.RECURS)) match(TokenType.RECURS);

        functionHeader();
        match(TokenType.ARROW);
        returnType();
        blockStatement();
    }


    // 37. function_header ::= ID function_type_params? '(' formal_params? ')'
    private void functionHeader() {
        match(TokenType.ID);

        if(nextLA(TokenType.LT)) functionTypeParams();

        match(TokenType.LPAREN);
        if(nextLA(TokenType.IN) || nextLA(TokenType.OUT) || nextLA(TokenType.INOUT) || nextLA(TokenType.REF))
            formalParams();
        match(TokenType.RPAREN);
    }


    // 38. function_type_params ::= '<' typeifier ( ',' typeifier )* '>'
    private void functionTypeParams() {
        match(TokenType.LT);
        typeifier();

        while(nextLA(TokenType.COMMA)) {
            match(TokenType.COMMA);
            typeifier();
        }
        match(TokenType.GT);
    }


    // 39. typeifier ::= ( 'discr' | 'scalar' | 'class' )? ID
    private void typeifier() {
        if(nextLA(TokenType.DISCR)) match(TokenType.DISCR);
        else if(nextLA(TokenType.SCALAR)) match(TokenType.SCALAR);
        else if(nextLA(TokenType.CLASS)) match(TokenType.CLASS);
        else throw new IllegalStateException("Error! Invalid function typeifer!");

        match(TokenType.ID);
    }


    /*
    ------------------------------------------------------------
                          MAIN FUNCTION
    ------------------------------------------------------------
    */


    // 40. main ::= 'def' 'main' args? '=>' return_type block_statement
    private void mainFunc() {
        match(TokenType.DEF);
        match(TokenType.MAIN);
        if(nextLA(TokenType.LPAREN)) args();
        match(TokenType.ARROW);
        returnType();
        blockStatement();
    }


    // 41. args ::= '(' formal_params? ')'
    private void args() {
        match(TokenType.LPAREN);
        if(nextLA(TokenType.IN) || nextLA(TokenType.OUT) || nextLA(TokenType.INOUT) || nextLA(TokenType.REF))
            formalParams();
        match(TokenType.RPAREN);
    }


    // 42. block-statement ::= '{' declaration* statement* '}'
    private void blockStatement() {
        match(TokenType.LBRACE);
        while(nextLA(TokenType.DEF)) declaration();

        while(isStatFIRST()) statement();

        match(TokenType.RBRACE);
    }


    // 43. declaration ::= 'def' 'local'? variable_decl
    private void declaration() {
        match(TokenType.DEF);
        if(nextLA(TokenType.LOCAL)) match(TokenType.LOCAL);
        variableDecl();
    }


    /*
    ------------------------------------------------------------
                             STATEMENTS
    ------------------------------------------------------------
    */

    // TODO: FOR STATEMENT
    // 44. statement ::=    'stop'
    //    | return_statement
    //    | assignment_statement
    //    | block_statement
    //    | if_statement
    //    | while_statement
    //    | do_while_statement
    //    | choice_statement
    //    | input_statement
    //    | output_statement
    private void statement() {
        if(nextLA(TokenType.STOP)) match(TokenType.STOP);
        else if(nextLA(TokenType.RETURN)) returnStatement();
        else if(nextLA(TokenType.LBRACE)) blockStatement();
        else if(nextLA(TokenType.IF)) ifStatement();
        else if(nextLA(TokenType.WHILE)) whileStatement();
        else if(nextLA(TokenType.DO)) doWhileStatement();
        else if(nextLA(TokenType.FOR)) forStatement();
        else if(nextLA(TokenType.CHOICE)) choiceStatement();
        else if(nextLA(TokenType.CIN)) inputStatement();
        else if(nextLA(TokenType.COUT)) outputStatement();
        else assignmentStatement();
    }


    // 45. return_statement ::= expression?
    private void returnStatement() {
        match(TokenType.RETURN);
        if(isPrimExprFIRST()) expression();
    }


    // 46. assignment_statement ::= 'set' expression assignment_operator expression
                            //    |  logical_or_expression
    private void assignmentStatement() {
        if(nextLA(TokenType.SET)) {
            match(TokenType.SET);
            expression();
            assignmentOperator();
            expression();
        }
        else logicalOrExpression();
    }


    //TODO: Add new operators
    // 47. assignment_operator ::= '=' | '+=' | '-=' | '*=' | '/=' | '%=' | '**='
    private void assignmentOperator() {
        if(nextLA(TokenType.EQ)) match(TokenType.EQ);
        else throw new IllegalStateException("Error! Invalid assignment operator.");
    }


    // 48. if_statement ::= if expression block_statement ( elseif_statement )* ( 'else' block_statement)?
    private void ifStatement() {
        match(TokenType.IF);
        expression();
        blockStatement();

        while(nextLA(TokenType.ELSE) && nextLA(TokenType.IF))
            elseIfStatement();

        if(nextLA(TokenType.ELSE)) {
            match(TokenType.ELSE);
            blockStatement();
        }
    }


    // 49. elseif_statement ::= 'else' 'if' expression block_statement
    private void elseIfStatement() {
        match(TokenType.ELSE);
        match(TokenType.IF);
        expression();
        blockStatement();
    }

    // 50. while_statement ::= 'while' expression ( 'next' expression )? block_statement
    private void whileStatement() {
        match(TokenType.WHILE);
        expression();
        if(nextLA(TokenType.NEXT)) {
            match(TokenType.NEXT);
            expression();
        }
        blockStatement();
    }


    // 51. do_while_statement ::= 'do' block_statement ( 'next' expression )? 'while' expression
    private void doWhileStatement() {
        match(TokenType.DO);
        blockStatement();
        if(nextLA(TokenType.NEXT)) {
            match(TokenType.NEXT);
            expression();
        }
        match(TokenType.WHILE);
        blockStatement();
    }

    //TODO: ADD FOR STATEMENT
    // 52. for_statement ::= 'for' expression ( 'next' expression )? do expression block_statement
    private void forStatement() {
        match(TokenType.FOR);
        expression();

        if(nextLA(TokenType.NEXT)) {
            match(TokenType.NEXT);
            expression();
        }
        match(TokenType.DO);
        expression();
        blockStatement();
    }

    // 52. choice_statement ::= 'choice' expression '{' case_statement* 'other' block_statement '}'
    private void choiceStatement() {
        match(TokenType.CHOICE);
        expression();
        match(TokenType.LBRACE);
        while(nextLA(TokenType.ON)) caseStatement();
        match(TokenType.OTHER);
        blockStatement();
        match(TokenType.RBRACE);
    }


    // 53. case_statement ::= 'on' label block_statement
    private void caseStatement() {
        match(TokenType.ON);
        label();
        blockStatement();
    }


    // 51. label ::= constant ('..' constant)?
    private void label() {
        constant();
        if(nextLA(TokenType.INC)) {
            match(TokenType.INC);
            constant();
        }
    }


    // 55. input_statement ::= 'cin' ( '>>' expression )+
    private void inputStatement() {
        match(TokenType.CIN);
        if(nextLA(TokenType.SRIGHT)) {
            while(nextLA(TokenType.SRIGHT)) {
                match(TokenType.SRIGHT);
                expression();
            }
        }
        else throw new IllegalStateException("Error! Invalid cin statement");
    }


    // 56. output_statement ::= 'cout' ( '<<' expression )+
    private void outputStatement() {
        match(TokenType.COUT);
        if(nextLA(TokenType.SLEFT)) {
            while(nextLA(TokenType.SLEFT)) {
                match(TokenType.SLEFT);
                expression();
            }
        }
        else throw new IllegalStateException("Error! Invalid cout statement");
    }


    /*
    ------------------------------------------------------------
                            EXPRESSIONS
    ------------------------------------------------------------
    */


    // 56. primary_expression ::= ID | constant | '(' expression ')'
    private void primaryExpression() {
        if(nextLA(TokenType.LPAREN)) {
            match(TokenType.LPAREN);
            expression();
            match(TokenType.RPAREN);
        }
        else if(nextLA(TokenType.ID)) match(TokenType.ID);
        else constant();
    }


    // 57. postfix_expression ::= primary_expression ( '[' expression ']' | '(' arguments ')' | ( '.' | '?.' ) expression)*
    private void postfixExpression() {
        primaryExpression();

        while(isAfterPrimExprFIRST()) {
            if(nextLA(TokenType.LBRACK)) {
                match(TokenType.LBRACK);
                expression();
                match(TokenType.RBRACK);
            }
            else if(nextLA(TokenType.LPAREN)) {
                match(TokenType.LPAREN);
                arguments();
                match(TokenType.RPAREN);
            }
            else {
                if(nextLA(TokenType.ELVIS)) match(TokenType.ELVIS);
                else match(TokenType.PERIOD);
                expression();
            }
        }
    }


    // 58. arguments ::= expression ( ',' expression )*
    private void arguments() {
        expression();
        while(nextLA(TokenType.COMMA)) {
            match(TokenType.COMMA);
            expression();
        }
    }

    // 59. unary_expression ::= unary_operator cast_expression | postfix_expression
    private void unaryExpression() {
        if(nextLA(TokenType.TILDE) || nextLA(TokenType.NOT)) {
            unaryOperator();
            castExpression();
        }
        else postfixExpression();
    }


    // 60. cast_expression ::= scalar_type '(' cast_expression ')' | unary_expression
    private void castExpression() {
        if(isScalarTypeFIRST()) {
            scalarType();
            match(TokenType.LPAREN);
            castExpression();
            match(TokenType.RPAREN);
        }
        else unaryExpression();
    }


    // 61. power_expression ::= cast_expression ( '**' cast_expression )*
    private void powerExpression() {
        castExpression();
        while(nextLA(TokenType.EXP)) {
            match(TokenType.EXP);
            castExpression();
        }
    }


    // 62. multiplication_expression ::= power_expression ( ( '*' | '/' | '%' ) power_expression )*
    private void multiplicationExpression() {
        powerExpression();

        while(isAfterPowerFIRST()) {
            if(nextLA(TokenType.MULT)) match(TokenType.MULT);
            else if(nextLA(TokenType.DIV)) match(TokenType.DIV);
            else match(TokenType.MOD);

            powerExpression();
        }
    }


    // 63. additive_expression ::= multiplication_expression ( ( '+' | '-' ) multiplication_expression )*
    private void additiveExpression() {
        multiplicationExpression();

        while(nextLA(TokenType.PLUS) || nextLA(TokenType.MINUS)) {
            if(nextLA(TokenType.PLUS)) match(TokenType.PLUS);
            else match(TokenType.MINUS);

            multiplicationExpression();
        }
    }


    // 64. shift_expression ::= additive_expression ( ( '<<' | '>>' ) additive_expression )*
//    private void shiftExpression() {
//        additiveExpression();
//        ArrayList<TokenType> SLEFT_SRIGHT = expectedShiftToks();
//
//        while(anyLA(SLEFT_SRIGHT)) {
//            if(nextLA(TokenType.SLEFT)) match(TokenType.SLEFT);
//            else match(TokenType.SRIGHT);
//            additiveExpression();
//        }
//    }


    // 65. relational_expression ::= shift_expression ( ( '<' | '>' | '<=' | '>=' ) shift_expression )*
    private void relationalExpression() {
        additiveExpression();

        while(isAfterAddFIRST()) {
            if(nextLA(TokenType.LT)) match(TokenType.LT);
            else if(nextLA(TokenType.GT)) match(TokenType.GT);
            else if(nextLA(TokenType.LTEQ)) match(TokenType.LTEQ);
            else match(TokenType.GTEQ);

            additiveExpression();
        }
    }

    // TODO: ADD new keywords
    // 66. instanceof_expression ::= relational_expression ( ( 'instanceof' | '!instanceof' | 'as?' ) relational_expression )*
    private void instanceOfExpression() {
        relationalExpression();
        while(nextLA(TokenType.INSTANCEOF)) {
            match(TokenType.INSTANCEOF);
            relationalExpression();
        }
    }


    // 67. equality_expression ::= instanceof_expression ( ( '==' | '<>' ) instanceof_expression )*
    private void equalityExpression() {
        instanceOfExpression();
        while(nextLA(TokenType.EQEQ) || nextLA(TokenType.NEQ)) {
            if(nextLA(TokenType.EQEQ)) match(TokenType.EQEQ);
            else match(TokenType.NEQ);
            instanceOfExpression();
        }
    }


    // 68. and_expression ::= equality_expression ( '&' equality_expression )*
    private void andExpression() {
        equalityExpression();
        while(nextLA(TokenType.BAND)) {
            match(TokenType.BAND);
            equalityExpression();
        }
    }


    // 69. exclusive_or_expression ::= and_expression ( '^' and_expression )*
    private void exclusiveOrExpression() {
        andExpression();
        while(nextLA(TokenType.XOR)) {
            match(TokenType.XOR);
            andExpression();
        }
    }


    // 70. inclusive_or_expression ::= exclusive_or_expression ( '|' exclusive_or_expression )*
    private void inclusiveOrExpression() {
        exclusiveOrExpression();
        while(nextLA(TokenType.BOR)) {
            match(TokenType.BOR);
            exclusiveOrExpression();
        }
    }


    // 71. logical_and_expression ::= inclusive_or_expression ( 'and' inclusive_or_expression )*
    private void logicalAndExpression() {
        inclusiveOrExpression();
        while(nextLA(TokenType.AND)) {
            match(TokenType.AND);
            inclusiveOrExpression();
        }
    }


    // 72. logical_or_expression ::= logical_and_expression ( 'or' logical_and_expression )*
    private void logicalOrExpression() {
        logicalAndExpression();
        while(nextLA(TokenType.OR)) {
            match(TokenType.OR);
            logicalAndExpression();
        }
    }


    // 73. expression ::= logical_or_expression
    private void expression() { logicalOrExpression(); }


    /*
    ------------------------------------------------------------
                                LITERALS
    ------------------------------------------------------------
    */


    // 74. constant ::= object_constant | array_constant | list_constant | scalar_constant
    private void constant() {
        if(nextLA(TokenType.NEW)) objectConstant();
        else if(nextLA(TokenType.ARRAY) || nextLA(TokenType.LBRACK))
            arrayConstant();
        else if(nextLA(TokenType.LIST) || nextLA(TokenType.LBRACE))
            listConstant();
        else scalarConstant();
    }


    // 75. object_constant ::= 'new' ID '(' (object_field ( ',' object_field )* ')'
    private void objectConstant() {
        match(TokenType.NEW);
        match(TokenType.ID);
        match(TokenType.LPAREN);

        objectField();
        while(nextLA(TokenType.COMMA)) {
            match(TokenType.COMMA);
            objectField();
        }
        match(TokenType.RPAREN);
    }


    // 76. object_field ::= ID ':' expression
    private void objectField() {
        match(TokenType.ID);
        match(TokenType.COLON);
        expression();
    }


    // 77. array_constant ::= 'Array' ( '[' expression ']' )* '(' arguments ')'
    private void arrayConstant() {
        match(TokenType.ARRAY);
        while(nextLA(TokenType.LBRACK)) {
            match(TokenType.LBRACK);
            expression();
            match(TokenType.RBRACK);
        }
        match(TokenType.LPAREN);
        arguments();
        match(TokenType.RPAREN);
    }


    // 78. list_constant ::= 'List' '(' expression (',' expression)* ')'
    private void listConstant() {
        match(TokenType.LIST);
        match(TokenType.LPAREN);
        expression();
        while(nextLA(TokenType.COMMA)) {
            match(TokenType.COMMA);
            expression();
        }
        match(TokenType.RPAREN);
    }



    // 79. scalar_constant ::= discrete_constant | STRING_LITERAL | TEXT_LITERAL | REAL_LITERAL
    private void scalarConstant() {
        if(nextLA(TokenType.STR_LIT)) match(TokenType.STR_LIT);
        else if(nextLA(TokenType.TEXT_LIT)) match(TokenType.TEXT_LIT);
        else if(nextLA(TokenType.REAL_LIT)) match(TokenType.REAL_LIT);
        else discreteConstant();
    }


    // 89. discrete_constant ::= INT_LITERAL | CHAR_LITERAL | BOOL_LITERAL
    private void discreteConstant() {
        if(nextLA(TokenType.INT_LIT)) match(TokenType.INT_LIT);
        else if(nextLA(TokenType.CHAR_LIT)) match(TokenType.CHAR_LIT);
        else if(nextLA(TokenType.BOOL_LIT)) match(TokenType.BOOL_LIT);
    }
}

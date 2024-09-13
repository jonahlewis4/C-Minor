package Parser;

import java.util.ArrayList;
import Lexer.Lexer;
import Lexer.Token;

public class Parser {
    private Lexer input;
    private Token currToken;
    private int inputPos;

    public Parser(Lexer input) {
        this.input = input;
        this.inputPos = 0;
    }

    private void consume() {
        this.inputPos += 1;
    }

    private boolean match(Lexer.TokenType expectedTok) {
        if(currToken.getTokenType() != expectedTok)
            return false;
        return true;
    }


    /*
    ------------------------------------------------------------
                          COMPILATION UNIT
    ------------------------------------------------------------
    */

    // 1. compilation ::= file-merge* enum-type* global-variable* class-type* function* main
    public void compilation() {

    }

    /*
    ------------------------------------------------------------
                          ENUM DECLARATION
    ------------------------------------------------------------
    */

    // 2. enum-type ::= Enum ID { ID (, ID)* }
    private void enumType() {

    }

    /*
    ------------------------------------------------------------
              GLOBAL VARIABLES AND VARIABLE DECLARATIONS
    ------------------------------------------------------------
    */

    // 3. global-variable ::= (const | global) variable-decl
    private void globalVariable() {

    }

    // 4. variable-decl ::= def variable-decl-list
    private void variableDecl() {

    }

    // 5. variable-decl-list ::= variable-decl-init (, variable-decl-init)*
    private void variableDeclList() {

    }

    // 6. variable-decl-init ::= ID:type = (expression | uninit)
    private void variableDeclInit() {

    }

    /*
    ------------------------------------------------------------
                                TYPES
    ------------------------------------------------------------
    */

    // 7. type ::= scalar-type | class-name | List[type] | Tuple<type (, type)*>
    private void type() {

    }

    // 8. scalar-type ::= discrete-type
    //                  | String([INT-LITERAL])*
    //                  | Real (:INT-LITERAL)? ([INT-LITERAL])*
    private void scalarType() {

    }

    // 9. discrete-type ::= Bool([INT-LITERAL])*
    //                     | Int (: INT-LITERAL .. INT-LITERAL)? ([INT-LITERAL])*
    //                     | Char(: CHAR-LITERAL .. CHAR-LITERAL)? ([INT-LITERAL])?
    //                     | ID (: ID .. ID)? ([INT-LITERAL])*
    private void discreteType() {

    }

    // 10. class-name ::= ID (<type (, type)*>)>
    private void className() {

    }

    /*
    ------------------------------------------------------------
                          CLASS DECLARATION
    ------------------------------------------------------------
    */

    // 11. class-type ::= (abstr | final) class ID type-params? super-class? body-decl
    private void classType() {

    }

    // 12. type-params ::= <type (, type)*>
    private void typeParams() {

    }

    // 13. super-class ::= inherits ID type-params?
    private void superClass() {

    }

    // 14. body-decl ::= protected {data-field* method*} public {data-field* method*}
    private void bodyDecl() {

    }

    /*
    ------------------------------------------------------------
                          FIELD DECLARATION
    ------------------------------------------------------------
    */

    // 15. data-field ::= ref? variable-decl
    private void dataField() {

    }

    // 16. method ::= method-class | operator-class
    private void method() {

    }

    /*
    ------------------------------------------------------------
                          METHOD DECLARATION
    ------------------------------------------------------------
    */

    // 17. method-class ::= modifier* override? method method-header => return-type block-statement
    private void methodClass() {

    }

    // 18. method-header ::= ID (formal-params?)
    private void methodHeader() {

    }

    // 19. modifier ::= final | pure | recurs
    private void modifier() {

    }

    // 20. formal-params ::= (in | out | inout | in out | ref) type ID
    private void formalParams() {

    }

    // 21. return-type ::= Void | type
    private void returnType() {

    }

    // 22. operator-class ::= final? operator operator-header => return-type block-statement
    private void operatorClass() {

    }

    // 23. operator-header ::= operator-declaration (formal-params?)
    private void operatorHeader() {

    }

    // 24. operator-declaration ::= binary-operator | unary-operator
    private void operatorDeclaration() {

    }

    // 25. binary-operator ::= <= | < | > | >= | == | <> | <=> | <: | :> | + | - | * | / | % | **
    private void binaryOperator() {

    }

    // 26. unary-operator ::= - | not
    private void unaryOperator() {

    }


    /*
    ------------------------------------------------------------
                        FUNCTION DECLARATION
    ------------------------------------------------------------
    */

    // 27. function ::= (pure | recurs)? def function-header => return-type block-statement
    private void function() {

    }

    // 28. function-header ::= ID function-type-params? (formal-params?)
    private void functionHeader() {

    }

    // 29. function-type-params ::= <typeifier, (, typeifier)*)>
    private void functionTypeParams() {

    }

    // 30. typeifier ::= (discr | scalar | class)? ID
    private void typeifier() {

    }

    /*
    ------------------------------------------------------------
                          MAIN FUNCTION
    ------------------------------------------------------------
    */

    // 31. main ::= def main block-statement
    private void mainFunc() {

    }

    // 32. block-statement ::= {declaration* statement*}
    private void blockStatement() {

    }

    // 33. declaration ::= local? variable-decl | ref variable-decl
    private void declaration() {

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

    }

    // 36. assignment-statement ::= reassign ID = argument-value
    private void assignmentStatement() {

    }

    // 37. argument-value ::= ref expression | expression
    private void argumentValue() {

    }

    // 38. if-statement ::= if expression block-statement (elseif-statement)* (else block-statement)?
    private void ifStatement() {

    }

    // 39. elseif-statement ::= else if expression block-statement
    private void elseIfStatement() {

    }

    // 40. iterator-statement ::= for (range-iterator | array-iterator) block-statement
    private void iteratorStatement() {

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

    }

    // 49. choice-statement ::= choice expression { case-statement* other block-statement }
    private void choiceStatement() {

    }

    // 50. case-statement ::= on label block-statement
    private void caseStatement() {

    }

    // 51. label ::= constant (.. constant)?
    private void label() {

    }

    // 52. list-command-statement ::= append (argument-list) | remove (argument-list) | insert (argument-list)
    private void listCommandStatement() {

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

    }

    // 55. and-expression ::= equal-expression ( and equal-expression)*
    private void andExpression() {

    }

    // 56. equal-expression ::= relational-expression ((== | <>) relational-expression)*
    private void equalExpression() {

    }

    // 57. relational-expression ::= minmax-expression (relational-operator minmax-expression)*
    private void relationalExpression() {

    }

    // 58. relational-operator ::= <= | < | > | >= | <=> | instanceof
    private void relationalOperator() {

    }

    // 59. minmax-expression ::= additive-expression ( (<: | :>) additive-expression)*
    private void minMaxExpression() {

    }

    // 60. additive-expression ::= multiplicative-expression ( (+ | -) multiplicative-expression)*
    private void additiveExpression() {

    }

    // 61. multiplicative-expression ::= power-expression ( (*|/|%) power-expression)*
    private void multiplicativeExpression() {

    }

    // 62. power-expression ::= unary-expression (** unary-expression)*
    private void powerExpression() {

    }

    // 63. unary-expression ::= (- | not) unary-expression | factor-expression
    private void unaryExpression() {

    }

    // 64. factor-expression ::= call-expression | uninit? (expression) | length (expression)
    //                         | slice (expression, expression range-operator expression) | cast (type, expression)
    private void factorExpression() {

    }

    // 65. call-expression ::= primary ((argument-list?) | . ID | [expression])*
    private void callExpression() {

    }

    // 66. primary ::= ID | constant | (expression)
    private void primary() {

    }

    /*
    ------------------------------------------------------------
                                LITERALS
    ------------------------------------------------------------
    */

    // 67. constant ::= object-constant | array-constant | list-constant | tuple-constant | scalar-constant
    private void constant() {

    }

    // 68. object-constant ::= ID (object-field (, object-field)*)
    private void objectConstant() {

    }

    // 69. object-field ::= ID = expression
    private void objectField() {

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

    }

    // 74. discrete-constant ::= INT-LITERAL | CHAR-LITERAL | BOOL-LITERAL
    private void discreteConstant() {

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

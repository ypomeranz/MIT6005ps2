package calculator;

/*
 * TODO define your symbols and groups from problem 1 here
 list of symbols
 numbers 0-9
 +,-,*,/,in,pt,.,(,),
 */

/**
 * Token type.
 numbers: (all combinations of 0-9, with 0-1 decmial pt - i.e. all positive real numbers)
 open paren: (
 close paren: )
 units: in pt
 pt sign: .
 operators: + - * /
 
 */
enum Type {
	NUMBER, OPENPAREN, CLOSEPAREN, UNIT, OPERATOR, EOF
}
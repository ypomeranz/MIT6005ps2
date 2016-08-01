package calculator;

import calculator.Lexer;
import calculator.Lexer.Token;
import java.util.ArrayList;

/*
Expression ::= (Value Operator Value) | Value
Value ::= (Number Unit?) | (Openparen Expression Closeparen Unit?)
Openparen ::= (
Number ::= Digit+ (. Digit+)?
Digit :: = 0|1|2|3|4|5|6|7|8|9
Unit ::= in | pt
Operator ::= + | - | * | /
Closeparen ::= )
				
 
 
 */

/**
 * Calculator parser. All values are measured in pt.
 */
class Parser {
	
	@SuppressWarnings("serial")
	static class ParserException extends RuntimeException {
		public ParserException(){
			super();
		}
		public ParserException(String s){
			super(s);
		}
		
	}

	/**
	 * Type of values.
	 */
	private enum ValueType {
		POINTS, INCHES, SCALAR
	};

	/**
	 * Internal value is always in points.
	 */
	public class Value {
		final double value;
		final ValueType type;

		Value(double value, ValueType type) {
			this.value = value;
			this.type = type;
		}

		@Override
		public String toString() {
			switch (type) {
			case INCHES:
				return value / PT_PER_IN + " in";
			case POINTS:
				return value + " pt";
			default:
				return "" + value;
			}
		}
	}

	private static final double PT_PER_IN = 72;
	private final Lexer lexer;
	public ArrayList<Token> alltokens;


	/* 
	Constructor for Parser object
	@param lexer - lexer to prepare for parsing 
	mutates lexer so that it reaches EOF
	*/ 
	Parser(Lexer lexer) {
		this.lexer = lexer;
		ArrayList<Token> fulltokenlist = new ArrayList<Token>();
		Token x = lexer.next();
			while (x.type != Type.EOF){
				fulltokenlist.add(x);
				x = lexer.next();
			}
		alltokens=fulltokenlist;
	}

	/*
	Evaluates Parser.alltokens
	@param tokens ArrayList of tokens to parse
	@returns answer - Value containing the value of the expression represented by tokens and the final units
	mutates tokens - results in an empty ArrayList
	*/
	public Value evaluate(ArrayList<Token> tokens) {
		int stage = 0;
		String operator = new String();
		Value firstvalue=null;
		Value secondvalue=null;
		Value answer = null;
		boolean nooperator = false;
		while (tokens.size()>0 && stage <= 3){
			stage++;
			
			if (stage == 1){
				//evaluate the first part of the expression
				
				if (tokens.get(0).type == Type.NUMBER){
					firstvalue = evalNumber(tokens);
					
				} else if (tokens.get(0).type == Type.OPENPAREN){
					firstvalue = evalExpression(tokens);
					//expression may be followed by a unit type to convert to
					if (tokens.size()>0){
						if (tokens.get(0).type==Type.UNIT && firstvalue.type==ValueType.SCALAR){
							firstvalue = convertScalar(firstvalue, tokens.get(0).text);
							tokens.remove(0);
						} else if (tokens.get(0).type==Type.UNIT && firstvalue.type!=ValueType.SCALAR){
							firstvalue = convertNonScalar(firstvalue, tokens.get(0).text);
							tokens.remove(0);
						}
					}
				} else {
					//illegal expression - raise error - until I implement exceptions, working with a print statement
					throw new ParserException("Error! First part of expression invalid!");
				}
				nooperator = true;
			} else if (stage == 2){
				nooperator = false;
				if (tokens.get(0).type==Type.OPERATOR){
					//save the operator
					operator = tokens.get(0).text;
					tokens.remove(0);
				} else {
					//invalid expression
					throw new ParserException("Problem with the Syntax! Expected operator!");
				}
			} else if (stage == 3){
				//evaluate the second part of the expression
				if (tokens.get(0).type == Type.NUMBER){
					secondvalue = evalNumber(tokens);
					
				} else if (tokens.get(0).type == Type.OPENPAREN){
					secondvalue = evalExpression(tokens);
					//expression may be followed by a unit type to convert to
					if (tokens.size()>0){
						if (tokens.get(0).type==Type.UNIT && secondvalue.type==ValueType.SCALAR){
							secondvalue = convertScalar(secondvalue, tokens.get(0).text);
							tokens.remove(0);
						} else if (tokens.get(0).type==Type.UNIT && secondvalue.type != ValueType.SCALAR){
							secondvalue = convertNonScalar(secondvalue, tokens.get(0).text);
							tokens.remove(0);
						}
					}
				} else {
					//illegal expression - raise error - until I implement exceptions, working with a print statement
					throw new ParserException("Syntax error in second part of an expression!");
				}
			} else if (stage == 4){
				throw new ParserException("Tokens left at the end of expression");
			}

			

		
		}
		//evaluate the expression and return a Value
		//four cases - one for each operation
		if (stage == 1){
			return firstvalue;
		} else if (stage == 2){
			throw new ParserException("No value after operand");
		}
		if (operator.charAt(0) == '+'){
			double answervalue = firstvalue.value + secondvalue.value;
			if (firstvalue.type ==  ValueType.SCALAR && secondvalue.type == ValueType.SCALAR){
				answer = new Value(answervalue, ValueType.SCALAR);
			} else if ((firstvalue.type == ValueType.INCHES | secondvalue.type == ValueType.INCHES) 
							&& (firstvalue.type!=ValueType.POINTS && secondvalue.type!=ValueType.POINTS)){
				answer = new Value(answervalue, ValueType.INCHES);	
			} else if ((firstvalue.type==ValueType.POINTS | secondvalue.type == ValueType.POINTS) 
							&& firstvalue.type != ValueType.INCHES && secondvalue.type != ValueType.INCHES){
				answer = new Value(answervalue, ValueType.POINTS);
			} else if ((firstvalue.type==ValueType.POINTS && secondvalue.type==ValueType.INCHES) 
							| (firstvalue.type==ValueType.INCHES && secondvalue.type==ValueType.POINTS)){
				answer = new Value(answervalue, firstvalue.type);	
			}
			
		} else if (operator.charAt(0) == '-'){
			double answervalue = firstvalue.value - secondvalue.value;
			if (firstvalue.type ==  ValueType.SCALAR && secondvalue.type == ValueType.SCALAR){
				answer = new Value(answervalue, ValueType.SCALAR);
			} else if ((firstvalue.type == ValueType.INCHES | secondvalue.type == ValueType.INCHES) 
							&& firstvalue.type!=ValueType.POINTS && secondvalue.type!=ValueType.POINTS){
				answer = new Value(answervalue, ValueType.INCHES);	
			} else if ((firstvalue.type==ValueType.POINTS | secondvalue.type == ValueType.POINTS) 
							&& firstvalue.type != ValueType.INCHES && secondvalue.type != ValueType.INCHES){
				answer = new Value(answervalue, ValueType.POINTS);
			} else if ((firstvalue.type==ValueType.POINTS && secondvalue.type==ValueType.INCHES) 
							| (firstvalue.type==ValueType.INCHES && secondvalue.type==ValueType.POINTS)){
				answer = new Value(answervalue, firstvalue.type);	
			}
			
			
		} else if (operator.charAt(0) == '*'){
			double answervalue = firstvalue.value * secondvalue.value;
			//if one of the values is a scalar, return in the format of the other (even if scalar)
			//otherwise return in terms of the first value
			if (firstvalue.type==ValueType.SCALAR){
				answer = new Value(answervalue, secondvalue.type);
			} else if (secondvalue.type == ValueType.SCALAR){
				answer = new Value(answervalue, firstvalue.type);
			} else{
				answer = new Value(answervalue, firstvalue.type);
			}
			
		} else if (operator.charAt(0) == '/'){
			if (secondvalue.value == (double)0.0){
				//raise a divide by zero error
				//System.out.println("Divide by zero error!");
				throw new ParserException("Divide by zero error");
			}
			
			double answervalue = firstvalue.value / secondvalue.value;
			
			
			if ((firstvalue.type == secondvalue.type) | (firstvalue.type!=ValueType.SCALAR 
							&& secondvalue.type != ValueType.SCALAR)){
				answer = new Value(answervalue, ValueType.SCALAR);
			} else if (firstvalue.type != ValueType.SCALAR){
				answer = new Value(answervalue, firstvalue.type);
			} else if (secondvalue.type != ValueType.SCALAR){
				answer = new Value(answervalue, secondvalue.type);
			}
			
		}
		if (nooperator == true){
			answer = firstvalue;
		}
		return answer;
		
	}
	
	
	public Value evalNumber(ArrayList<Token> tokens){
		double num = Double.parseDouble(tokens.get(0).text);
		tokens.remove(0);
		Value numvalue = new Value(num, ValueType.SCALAR);
		if (tokens.size() > 0){
			//check to see if there are units
			if (tokens.get(0).type == Type.UNIT){
				//check to see if the units are in or pt and save first value
				if (tokens.get(0).text == "in"){
					tokens.remove(0);
					numvalue = new Value(PT_PER_IN * num, ValueType.INCHES);
				} else if (tokens.get(0).text=="pt"){
					tokens.remove(0);
					numvalue = new Value(num, ValueType.POINTS);
				}
			} else {
				numvalue = new Value(num, ValueType.SCALAR);
			}
			return numvalue;
		} else {
			return new Value(num, ValueType.SCALAR);
		} 
						
		
	}
	
	/*spec for evalExpression
	mutates tokens
	i haven't yet adjusted for the possibility that you never get the close paren - take that into account
	*/
	public Value evalExpression(ArrayList<Token> tokens){
		//figure out how much of the array is this expression, and transfer to a new array
		int parencount = 1;
		ArrayList<Token> sublist = new ArrayList<Token>();
		tokens.remove(0);
		boolean closedparen = false;
		while (tokens.size()>0){
			Token x = tokens.get(0);
			
			//adjust our two values for checking when we are done finding the subexpression
			//first adjust parencount
			if (x.type==Type.OPENPAREN){
				parencount++;
			} else if (x.type==Type.CLOSEPAREN){
				parencount--;
			}
			//now adjust our boolean to say if we are currently on a closed parenthesis
			if (x.type==Type.CLOSEPAREN){
				closedparen = true;
			} else {
				closedparen = false;
			}
			//break if we've finished the expression and remove the current token(closeparen)
			if (parencount==0 && closedparen == true){
				tokens.remove(0);
				break;
			}
			//do what the while loop actually does - it transfers from tokens to sublist
			sublist.add(x);
			tokens.remove(0);
		}
		
		//the while loop is completed 
		//we now have separated the subexpression from our main tokens array
		//so recurse on the subarray
		return evaluate(sublist);
	}
	
	
	public Value convertScalar(Value value, String unit){
		if (unit=="in"){
			return new Value(value.value*PT_PER_IN, ValueType.INCHES);
		} else if (unit=="pt"){
			return new Value(value.value, ValueType.POINTS);
		}
		return new Value(value.value, ValueType.POINTS);	
	}
	
	public Value convertNonScalar(Value value, String unit){
		if (unit == "in"){
			return new Value(value.value, ValueType.INCHES);
		} else if (unit=="pt"){
			return new Value(value.value, ValueType.POINTS);
		}
		return new Value(value.value, ValueType.POINTS);
	}
}

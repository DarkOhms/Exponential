/*
 * Evaluator class
 *
 * Luke Martin
 * w0742587
 * Dixon CISP 430 - W 6:30
 * Spring 2016
 * Assignment 3
 *
 * Class Associations
 *
 * Evaluator // performs evaluation of a string input into a numerical answer of the highest common precision
 * Parser // takes an infix expression and makes a postfix expression for evaluation
 * Tokenizer //makes tokens from the input string
 * HashTable  // stores and retrieves tokens for processing by the evaluator
 * Stack // used by Parser and Evaluator
 * Queue // also used by Parser and Evaluator
 *
 * Evaluator ---- 1:1 uses ----- Parser
 * Evaluator ---- 1:1 uses ----- Stack<String>
 * Evaluator ---- 1:m uses ----- SymbolNotFound
 * Evaluator ---- 1:m uses ----- SyntaxError
 * Parser ------- 1:1 uses ----- Stack<String>
 * Parser ------- 1:1 uses ----- Queue<String>
 * Parser ------- 1:1 uses ----- StringTokenizer
 * Parser ------- 1:m uses ----- SyntaxError
 *
 * operator tokens::= {+,-,/,*,Sin,Sqr,Abs}
 * operandTokens::={alpha,beta,charlie,delta}
 * ConstantTokens::={"1..9", "whole.decimal"}
 *
 * Class Evaluator
 *
 * Members
 * (~)HashTable symbols
 * Methods
 * (~)String unaryEval(String, String)  //takes an operand and an operator for evaluation, returns a string for the stack
 * (~)String unaryEval(String, String, String)  //takes two operands and an operator for evaluation, returns a string for the stack
 * (+)double evaluate(String) //takes a user input string and evaluates it to a double
 * (+)void display() //displays the contents of alpha beta charlie delta and X from the symbol table
 *
 * Class Parser
 *
 * Members
 * (-)String toParse
 * (-)String inputSymbol
 * (-)List<String> tokens
 * (-)Stack<String> s2
 * (#)Queue<String> s1
 *
 * Methods
 * (~)void parseTable(String)  //takes an input symbol for evaluation
 * (+)void parse()  //performs parse operation on toParse
 * (-)void nested_switch1()  //called by the parse table
 * (-)void nested_switch2()  //called by the parse table
 * (-)void nested_switch3()  //called by the parse table
 * (-)void nested_switch4()  //called by the parse table
 * (-)void nested_switch5()  //called by the parse table
 * (-)void s1()  //called by the parse table and various nested_switches
 * (-)void s2()  //called by the parse table and various nested_switches
 * (-)void u1()  //called by the parse table and various nested_switches
 * (-)void uc()  //called by the parse table and various nested_switches
 * (-)void unaryCheck()  //checks to see if a token is a unary operator
 * (-)void u2()  // unstacks s2 on to the queue
 * (-)void u1()  //called to throw a SyntaxError
 *
 * Class SyntaxError
 * 
 * Methods
 * (~)void SyntaxError
 * 
 * Class SymbolNotFound
 * 
 * Methods
 * (~)void SymbolNotFound
 *
 */

import listadt.Stack;
import errors.SyntaxError;
import errors.SymbolNotFound;
import HashTable.*;
import java.util.*;

public class Evaluator {

	HashTable symbols = new HashTable();
	
	boolean hasOperator = false; //checks to make sure that expression is an operation and not an assignment

	{
		symbols.insert("alpha", 25);
		symbols.insert("beta", 10);
		symbols.insert("charlie", 6.0);
		symbols.insert("delta", 11);
	}



	String unaryEval(String data, String operator) throws SymbolNotFound{
		double operand = 0.0;
		String result = "";

		if(data.matches("[a-zA-Z]+")){

			try{
				operand = symbols.getData(data);
			}catch(SymbolNotFound e){
				throw e;
			}

		}else{
			operand = Double.parseDouble(data);
		}

		switch(operator){
			case "sin":  result = Double.toString(Math.sin(operand));
				break;
			case "sqr":  result = Double.toString(Math.sqrt(operand));
				break;
			case "abs":  result = Double.toString(Math.abs(operand));
				break;
		}

		return result;
	}

	String binaryEval(String data1, String data2, String operator) throws SymbolNotFound{

		double left = 0.0;
		double right = 0.0;

		String result = "";

		if(data1.matches("[a-zA-Z]+")){
			try{
				right = symbols.getData(data1);
			}catch(SymbolNotFound e){
				throw e;
			}
		}else{
			right = Double.parseDouble(data1);
		}

		if(data2.matches("[a-zA-Z]+")){
			try{
				left = symbols.getData(data2);
			}catch(SymbolNotFound e){
				throw e;
			}
		}else{
			left = Double.parseDouble(data2);
		}


		switch(operator){
			case "+":  result = Double.toString(left+right);
				break;
			case "-":  result = Double.toString(left-right);
				break;
			case "*":  result = Double.toString(left*right);
				break;
			case "/":  result = Double.toString(left/right);
				break;
		}
		return result;
	}

	public double evaluate(String input) throws SyntaxError, SymbolNotFound{

		//parse the infix
		Stack<String> eval = new Stack<>();
		Parser parser = new Parser(input);
		boolean hasOperator = false; //checks to make sure that expression is an operation and not an assignment
		
		parser.parse();

		//prime the eval stack with the symbol for assignment
		//check for operators and put everything else on eval stack
		//loop through parser.s1 and evaluate

		eval.push(parser.s1.dequeue());
		double data = 0.0;

		if(parser.s1.getCount() < 1){
			throw new SyntaxError("SyntaxError");
		}
		while(!parser.s1.isEmpty()){

			if(parser.s1.firstInLine().matches("=")){

				if(hasOperator){
					data = Double.parseDouble(eval.pop());
					String key = eval.pop();
					symbols.insert(key, data);
					return data;
				}else{
					if(eval.showTop().matches("[a-zA-Z]+")){

						try{
							data = symbols.getData(eval.pop());
							String key = eval.pop();
							symbols.insert(key, data);
							return data;
						}catch(SymbolNotFound e){
							throw e;
						}
					}
				}
			}
			
			if(parser.s1.firstInLine().matches("\\+|\\*|/|sin|sqr|abs|-")){
				//use the operator to evaluate the eval stack
				hasOperator = true;
				
				switch(parser.s1.firstInLine()){

					case "+":
					case "-":
					case "/":
					case "*":
						if(eval.getCount() > 1){
						  eval.push(binaryEval(eval.pop(), eval.pop(), parser.s1.dequeue()));
						  break;
						}else{
							throw new SyntaxError("SyntaxError");
						}
					case "sin":
					case "sqr":
					case "abs":
						if(eval.getCount() > 0){
						  eval.push(unaryEval(eval.pop(), parser.s1.dequeue()));
						  break;
						}else{
							throw new SyntaxError("SyntaxError");
						}
				}

			}else{
				eval.push(parser.s1.dequeue());
			}
		}
		return data;
	}



	public static void main(String[] args){

		String userInput = new String("X=12*(alpha+3)");
		Evaluator eze = new Evaluator();

		System.out.println("symbols.insert(\"alpha\", 25)");
		System.out.println("symbols.insert(\"beta\", 10)");
		System.out.println("symbols.insert(\"charlie\", 6)");
		System.out.println("symbols.insert(\"delta\", 11)");

		System.out.println("userInput: X=12*(alpha+3) ");

		System.out.println("alpha   EXPECTED: 25");
		System.out.println("beta    EXPECTED: 10");
		System.out.println("charlie EXPECTED: 6");
		System.out.println("delta   EXPECTED: 11");
		System.out.println("X       EXPECTED: 336 ");

		try{
			System.out.println("X is:" + eze.evaluate(userInput));
		}catch(SyntaxError | SymbolNotFound e){
			System.out.println(e.getMessage());
		}

		System.out.println("-------------------Actual------------------");
		display(eze);

		System.out.println("userInput: alpha = alpha + beta / charlie * delta");

		userInput = "alpha = alpha + beta / charlie * delta";

		System.out.println("alpha   EXPECTED: 43.33333333333");
		System.out.println("beta    EXPECTED: 10");
		System.out.println("charlie EXPECTED: 6");
		System.out.println("delta   EXPECTED: 11");
		System.out.println("X       EXPECTED: 336 ");

		try{
			System.out.println("alpha is:" + eze.evaluate(userInput));
		}catch(SyntaxError | SymbolNotFound e){
			System.out.println(e.getMessage());
		}

		System.out.println("-------------------Actual------------------");
		display(eze);

		System.out.println("userInput: beta = 5/2.0 + alpha");

		userInput = "beta = 5/2.0 + alpha";

		System.out.println("alpha   EXPECTED: 43.33333333333");
		System.out.println("beta    EXPECTED: 45.83333333333");
		System.out.println("charlie EXPECTED: 6");
		System.out.println("delta   EXPECTED: 11");
		System.out.println("X       EXPECTED: 336 ");

		try{
			System.out.println("beta is:" + eze.evaluate(userInput));
		}catch(SyntaxError | SymbolNotFound e){
			System.out.println(e.getMessage());
		}

		System.out.println("-------------------Actual------------------");
		display(eze);

		System.out.println("userInput: charlie = sin(alpha) + (charlie-delta)");

		userInput = "charlie = sin(alpha) + (charlie-delta)";

		System.out.println("alpha   EXPECTED: 43.33333333333");
		System.out.println("beta    EXPECTED: 45.83333333333");
		System.out.println("charlie EXPECTED: -5.60436119243");
		System.out.println("delta   EXPECTED: 11");
		System.out.println("X       EXPECTED: 336 ");

		try{
			System.out.println("charlie is:" + eze.evaluate(userInput));
		}catch(SyntaxError | SymbolNotFound e){
			System.out.println(e.getMessage());
		}

		System.out.println("-------------------Actual------------------");
		display(eze);

		System.out.println("userInput: delta = alpha - beta * charlie/delta");

		userInput = "delta = alpha - beta * charlie/delta";

		System.out.println("alpha   EXPECTED: 43.33333333333");
		System.out.println("beta    EXPECTED: 45.83333333333");
		System.out.println("charlie EXPECTED: -5.60436119243");
		System.out.println("delta   EXPECTED: 66.68483830182");
		System.out.println("X       EXPECTED: 336 ");

		try{
			System.out.println("delta is:" + eze.evaluate(userInput));
		}catch(SyntaxError | SymbolNotFound e){
			System.out.println(e.getMessage());
		}

		System.out.println("-------------------Actual------------------");
		display(eze);

		System.out.println("userInput: = delta alpha - beta * charlie/delta");

		userInput = "= delta alpha - beta * charlie/delta";

		System.out.println("EXPECTED: SyntaxError");
		System.out.println("-------------------Actual------------------");

		try{
			System.out.println("? is:" + eze.evaluate(userInput));
		}catch(SyntaxError | SymbolNotFound e){
			System.out.println(e.getMessage());
		}
		
		System.out.println("userInput: -  - + *");

		userInput = "- - + *";

		System.out.println("EXPECTED: SyntaxError");
		System.out.println("-------------------Actual------------------");

		try{
			System.out.println("? is:" + eze.evaluate(userInput));
		}catch(SyntaxError | SymbolNotFound e){
			System.out.println(e.getMessage());
		}
		
		System.out.println("userInput: beta = alpha + zeta");

		userInput = "beta = alpha + zeta";

		System.out.println("EXPECTED: SymbolNotFound");
		System.out.println("-------------------Actual------------------");

		try{
			System.out.println("beta is:" + eze.evaluate(userInput));
		}catch(SyntaxError | SymbolNotFound e){
			System.out.println(e.getMessage());
		}
		
		System.out.println("userInput = \"alpha beta delta\"");

		userInput = "alpha beta delta";

		System.out.println("EXPECTED: SyntaxError");
		System.out.println("-------------------Actual------------------");

		try{
			System.out.println("? is:" + eze.evaluate(userInput));
		}catch(SyntaxError | SymbolNotFound e){
			System.out.println(e.getMessage());
		}
		System.out.println("userInput: alpha = beta");

		userInput = "alpha = beta";

		System.out.println("alpha   EXPECTED: 43.33333333333");
		System.out.println("beta    EXPECTED: 45.83333333333");
		System.out.println("charlie EXPECTED: -5.60436119243");
		System.out.println("delta   EXPECTED: 66.68483830182");
		System.out.println("X       EXPECTED: 336 ");
		System.out.println("-------------------Actual------------------");

		try{
			System.out.println("alpha is:" + eze.evaluate(userInput));
		}catch(SyntaxError | SymbolNotFound e){
			System.out.println(e.getMessage());
		}
		display(eze);
		
		Scanner scan = new Scanner(System.in);
		
		do{
			System.out.println("------------------------------------------------------");
			System.out.println("Enter an expression for evaluation (\"exit to exit\"):");
			userInput = scan.nextLine();
			if (userInput.equalsIgnoreCase("exit"))
                break;
			try{
				eze.evaluate(userInput);
			}catch(SyntaxError | SymbolNotFound e){
				System.out.println(e.getMessage());
			}
			display(eze);
			
		}while(!userInput.equals("exit"));
		scan.close();


	}//end main

	public static void display(Evaluator eval){
		try{
			System.out.println("alpha contains   :" + eval.symbols.getData("alpha"));
			System.out.println("beta contains    :" + eval.symbols.getData("beta"));
			System.out.println("charlie contains :" + eval.symbols.getData("charlie"));
			System.out.println("delta contains   :" + eval.symbols.getData("delta"));
			System.out.println("X contains       :" + eval.symbols.getData("X"));
		}catch(SymbolNotFound e){
			System.out.println(e.getMessage());
		}
	}
}

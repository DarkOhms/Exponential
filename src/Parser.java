import java.util.StringTokenizer;
import errors.SyntaxError;
import listadt.*;

public class Parser{

	private String toParse = "";
	private String inputSymbol = "";
	private List<String> tokens = new List<>();
	private Stack<String> s2 = new Stack<>();
	protected Queue<String> s1 = new Queue<>();

	public Parser(String input){
		toParse = input;
	}

	void parseTable(String inputSymbol) throws SyntaxError{

		switch(inputSymbol){

			case "=":
				nested_switch1();
				break;
			case "+":
			case "-":
				nested_switch2();
				break;
			case "*":
			case "/":
				nested_switch3();
				break;
			case "sin":
			case "abs":
			case "sqr":
			case "(":
				nested_switch4();
				break;
			case ")":
				nested_switch5();
				break;
			default:
				s1();
				break;
		}
	}

	public void parse() throws SyntaxError{

		toParse = toParse.replaceAll("\\s+","");
		StringTokenizer st = new StringTokenizer(toParse,"=-+*/)(", true);
		while (st.hasMoreTokens()) {
			tokens.insert(st.nextToken());

		}

		Iterator<String> ob1 = tokens.iterator();
		while(ob1.hasNext()){

			inputSymbol = ob1.nextData();
			parseTable(inputSymbol);

		}
		//end of input
		u2();
	}
	private void nested_switch1() throws SyntaxError{

		if(s2.showTop() == null && s1.getCount() == 1){
			s2();
		}else{
			throw new SyntaxError("SyntaxError ");
		}


	}

	private void nested_switch2() throws SyntaxError{

		if(s2.showTop() == null){
			throw new SyntaxError("SyntaxError ");
		}

		switch(s2.showTop()){

			case "=":
			case "(":
				s2();
				break;
			case "+":
			case "-":
			case "*":
			case "/":
				u1();
				break;


		}
	}

	private void nested_switch3() throws SyntaxError{

		if(s2.showTop() == null){
			uError();
		}else{

			switch(s2.showTop()){

				case "=":
				case "-":
				case "+":
				case "(":
					s2();
					break;
				case "*":
				case "/":
					u1();
					break;

			}
		}
	}

	private void nested_switch4() throws SyntaxError{

		if(s2.showTop() == null){
			uError();
		}else{
			s2();
		}
	}

	private void nested_switch5() throws SyntaxError{

		if(s2.showTop() == null||s2.showTop().equals("=")){
			uError();
		}else{
			uc();
		}
	}

	private void s1(){
		s1.enqueue(inputSymbol);
	}

	private void s2(){
		s2.push(inputSymbol);
	}

	private void u1() throws SyntaxError{
		s1.enqueue(s2.pop());
		parseTable(inputSymbol);
	}

	private void uc(){

		while(!s2.isEmpty()){

			if(s2.showTop().equals("(")){
				s2.pop();
				unaryCheck();
				break;
			}

			s1.enqueue(s2.pop());
		}
	}

	private void unaryCheck(){

		if(s2.showTop().contains("abs") || s2.showTop().contains("sin") || s2.showTop().contains("sqr")){
			s1.enqueue(s2.pop());
		}
	}

	private void u2(){
		while(!s2.isEmpty()){
			s1.enqueue(s2.pop());
		}
	}

	private void uError() throws SyntaxError{
		throw new SyntaxError("SyntaxError ");
	}

}

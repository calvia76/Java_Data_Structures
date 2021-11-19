package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {
	
	
	//CREATE EVALUATION TO PERFORM ARRAY MATHEMATICS
	
	
	//public static String delims = "*/+-()[]";
	public static String variables = "[a-zA-Z]";
	public static String numbers = "[0-9]";
	public static String operators = "[*/-+]";
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/
 
    	expr = expr.replaceAll("\\s+", ""); //removes all spaces and tabs from the expression
    	int i = 0;
    	String temp = "";
    	String checker = "";
    	
    	while(true) {
    		if(i==expr.length()) { //end of expression
    			if(temp.equals("")) {
    				break;
    				
    			}else {
    				vars.add(new Variable(temp));
    				break;
   				}
   		}
    		
    		checker = Character.toString(expr.charAt(i));
    		
    		if(checker.matches(variables)) { //if x is a letter(variable) add it to a temporary string placeholder
    			temp += checker;
    			i++; continue;
    		
    		}else {
    			if(checker.equals(")") || checker.equals("]") || checker.equals("(") ) { //|| checker.equals("(")
    				i++; continue;
    			}
    			
    			else if(checker.equals("[")){ //determines if temp is an array variable
    				arrays.add(new Array(temp));
    				temp = ""; i++; continue;
    			
    			}else if(checker.matches(numbers)){ //if checker pointer is a number
    				i++; continue;
    				
    			}else if(checker.equals("+") || checker.equals("*") || checker.equals("-") || checker.equals("/")) {
    				if(temp.equals("")) {
    					i++; continue;
    				}else {
    				vars.add(new Variable(temp));
    				temp="";
    				i++; continue;
    				}
    				
    			}else {
    				vars.add(new Variable(temp));
    				temp = ""; i++; continue;
    			}
    		}
    	}		
    }	
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }

    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	// following line just a placeholder for compilation
    	
    	Stack<String> operator = new Stack<String>(); 
    	Stack<Float> answer = new Stack<Float>(); 
    	Stack<Float> operand = new Stack<Float>();
    	String checker = ""; String temp = ""; 
    	int i = 0; int v = 0;
    	float ans = 0;
    	
    	//building the stacks for evaluator
    	while(true) {
    		if(i==expr.length()) {
    			if(arrays.isEmpty() && vars.isEmpty()) {
    				if(!operator.isEmpty()) { 
    					operand.push(Float.parseFloat(temp));
    					return evaluator(operator,operand);
    				}
    				//return Float.parseFloat(temp);
    			}else if(arrays.isEmpty() && !vars.isEmpty()) { //for expressions only containing variables
    				if(operator.isEmpty()) {
    					return ((float)vars.get(0).value); //for single variable inputs
    				}else {
    					operand.push((float)(vars.get(v).value));
    					return evaluator(operator,operand);
    				}
    			}else{
    				return ans;
    			}
    		}
    		
    		checker = Character.toString(expr.charAt(i));
		
			if(checker.matches(numbers)) {
				temp += checker;
				i++; continue;
		
			}else if(checker.matches(variables)) {
				temp +=checker;
				i++; continue;
				
			}else if(checker.equals("+") || checker.equals("*") || checker.equals("-") || checker.equals("/") || checker.equals("(") || checker.equals(")")){
					if(checker.equals(")")) { //when end of a subexpression is reached, solve for the sub expression
						ans += evaluator(operator,operand);
						operator.pop();
						i++;continue;

					}else if(checker.equals("(")){
						operator.push(checker);
						i++; continue;
					
					}else if(checker.equals("+") || checker.equals("*") || checker.equals("-") || checker.equals("/")){
						operator.push(checker);
						
						if(temp.length()==1) { //if temp is a single digit or a single character variable
							if(temp.matches(variables)) {
								operand.push((float)vars.get(v).value);
								i++; v++; temp=""; continue;
								
							}else if(temp.matches(numbers)) {
								operand.push(Float.parseFloat(temp));
								i++; temp = ""; continue;
							}
							
						}else if(temp.length()>1){
							String t = Character.toString(temp.charAt(0));
							if(t.matches(numbers)) {
								operand.push(Float.parseFloat(temp));														
								i++; temp=""; continue;
								
							}else if(t.matches(variables)) { //if temp is only contain string values(variables)
								operand.push((float)(vars.get(v).value));
								i++; v++; temp=""; continue;
							}
						}
					}
				}
		}
    }
    
	private static float evaluator(Stack<String> operator,Stack<Float> operand) {
		
		String op = "";
		float a = 0; float b = 0; float ans = 0;
		Stack<Float> answer = new Stack<Float>();
		int i = 0;
		
		
		while(true) {
				
			if(operator.isEmpty() && operand.isEmpty()) {
				return ans;
			}else if(operator.peek().equals("(")) {
				return ans;
			}else { 
				a = operand.pop();
				op = operator.pop();
					if(operand.isEmpty()) { //for an uneven evaluation ex: (2*(7+4))
						b = ans;
						ans = operation(op,a,b);
						continue;
					}else {
						b = operand.pop();
						ans += (operation(op,a,b));
						continue;
					}
				}
			}
		}		
	
	
	private static float operation(String op, float a, float b) {
	    	
			switch(op) {
			case "+":
				return b + a;
			case "*":
				return b * a;
			case "/":
				return b/a;
			case "-":
				return b-a;
			}
			return 0;
	    }
	 
	private static boolean PEMDAS(String op1, String op2) {
		if(op2.equals("(") || op2.equals(")"))
			return false;
		if((op1.equals("*") || op1.equals("/") && (op2.equals("+") || op2.equals("-"))))
			return false;
		else
			return true;
}
}


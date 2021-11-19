package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	
	
	public static Node add(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
			float coeff=0;
			
			Node ans = new Node(0,0,null);
			Node front = ans;
			
			boolean flag = true;
			
			
		while(flag ==true) {
			coeff = 0; //resets the placeholder after each loop
			
			if(poly1==null && poly2 == null) {
				front = front.next;
				return front;
			}
			if(poly1==null || poly2==null) {
				if(poly1==null && poly2!=null) {
					coeff = poly2.term.coeff;
					ans.next = new Node(coeff,poly2.term.degree,null);
					ans = ans.next;

					poly2=poly2.next;
					continue;
				}else if(poly1!=null && poly2==null) {
					coeff = poly1.term.coeff;
					ans.next = new Node(coeff,poly1.term.degree,null);
					ans = ans.next;
					
					poly1=poly1.next;
					continue;
				}
			}
			if(poly1.term.degree == poly2.term.degree) {
				coeff = (poly1.term.coeff + poly2.term.coeff);
				if(coeff == 0) {
					poly1 = poly1.next; poly2 = poly2.next;
					continue;
				}else if(coeff>0 || coeff <0) {
						ans.next=new Node(coeff,poly1.term.degree,null);
						ans = ans.next;
						
						poly1 = poly1.next; poly2=poly2.next;
						continue;
				}
				
			}else if(poly1.term.degree > poly2.term.degree) {
				ans.next = new Node(poly2.term.coeff,poly2.term.degree,null);
				ans = ans.next;

				poly2 = poly2.next;
				continue;
				
			}else if(poly1.term.degree < poly2.term.degree) {
				ans.next = new Node(poly1.term.coeff,poly1.term.degree,null);
				ans = ans.next;

				poly1 = poly1.next;
				continue;
				}
				
			}
		return null;
	}
	
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		
		Node p1 = new Node(0,0,null); Node p1front = p1;
		Node p2 = new Node(0,0,null); Node p2front = p2;
		Node answer = null;
		Node temp = null; Node reset = poly2;
		
		
		float coeff = 0;
		int degree = 0; int counter = 1; int tracker = 0;
		
		boolean flag = true;
		
		
		while(flag == true) {
			coeff = 0; degree = 0;
			
			//final
			if(poly1==null) {
				if(tracker==1) { //for 1x1 polynomial multiplication ex: 2x * 2x
					p1front = p1front.next;
					return p1front;
				}else if(tracker%2!=0) { //for odd looped multiplication
					p1front = p1front.next;
					return (answer=add(answer,p1front));
				}else {
					return answer;
				}
				
			}
			
			if(poly2!=null) { //while poly2 isn't null, traverse the LL and multiply, adding the product of coeff & sum of degree to a new LL
				if(counter==1) {
					coeff=(poly1.term.coeff * poly2.term.coeff);
					degree=(poly1.term.degree + poly2.term.degree);
					p1.next = new Node(coeff,degree,null);
					p1=p1.next;
					poly2 = poly2.next;
					
					if(poly2==null) {
						poly2=reset;
						poly1 = poly1.next;
						counter=2; tracker++;
						
					}else if(poly2!=null) {
						continue;
					}
					
				}else if(counter==2) {
					coeff = (poly1.term.coeff * poly2.term.coeff);
					degree = (poly1.term.degree + poly2.term.degree);
					p2.next = new Node(coeff,degree,null);
					p2 = p2.next;
					poly2 = poly2.next;
					
					if(poly2==null) {
						p1front = p1front.next; p2front = p2front.next;
						temp = add(p1front,p2front); answer = add(temp,answer);
						poly2=reset; p1front = p1; p2front = p2;
						poly1 = poly1.next;
						counter=1; tracker++;
						
					}else if(poly2!=null) {
						continue;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		
		double total = 0;
		float ans = 0;
		
		while(poly != null) {
			
			ans += (float)total;
			
			if(poly.term.degree == 0) {
				total = poly.term.coeff;
				poly=poly.next;
				continue;
				
			}else if(poly.term.degree > 0){
				total = (poly.term.coeff)*Math.pow(x,poly.term.degree);
				poly=poly.next;
				
			}if(poly==null) {
				ans += (float)total;
				return ans;
			}
		}
		return 0;
	}
	

	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}

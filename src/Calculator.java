/*
 * this class has 2 public static methods
 * 
 * 1.Calculator.solve(String exp)
 * -------------------------------
 * this is for solving infix(normal) expression passed in exp parameter and get the result.
 * 
 * 2.Calculator.getPostfixExpression(String exp)
 * -------------------------------
 * this is for converting infix(normal) expression passed in exp into postfix expression.
 * 
 */



 import java.util.Stack;
 import java.util.StringTokenizer;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;
 
 public class Calculator {
 
     //here XXX_PRECEDENCE variables holds the level of precedence of operators;
 
     private static final String FIRST_PRECIDENCE = "^";
     private static final String SECOND_PRECIDENCE = "*/";
     private static final String THIRD_PRECIDENCE = "+-";
 
     private static final String OPERATORS = "+-*/^*()";
 
     // here variable infixExpression is for hold the infix notation
     // here variable postfix is for hold the postfix notation 
     // here variable nextElement is for denote the number or operator when it is taken out 
     // from the infix expression
 
     private static Calculator calculator;
     private static PostfixSolver postfixSolver;
 
     private String infixExpression;
     private String postfixExpression;
     private String nextElement;
     private Stack<String> stack;;
 
     private Calculator()
     {
         this.infixExpression = "";
         this.postfixExpression = "";
         this.nextElement = null;
         this.stack = new Stack<String>();
     }
 
     public static void main(String[] args) {
 
             String exp = "2.1^(3.2)";
             System.out.println("Expression : " + exp);
             System.out.println("Answer : " + Calculator.solveExpression(exp));
 
     }
 
     public static String solveExpression(String exp)
     {
         calculator = new Calculator();
         return calculator.solve(exp);
     }
 
     public String solve(String exp)
     {
         try{
             this.infixExpression = Calculator.formatExpression(exp);
             this.getPostfixExpression();
             
             postfixSolver = new PostfixSolver();
             return postfixSolver.solvePostfix(postfixExpression);
         }
         catch(Exception e){
             return "Invalid input";
         }
     }
 
 
     // this method is for getting only postfix expression rather than getting answer.
     
     public static String getPostfixExpression(String exp)
     {
 
         calculator = new Calculator();
         calculator.infixExpression = exp;
         calculator.getPostfixExpression();
         
         return calculator.postfixExpression;
     }
  
 
     // this method is called only when solve method is called.
     // for getPostfixExpression() and solvePostfixExpression() it will not be called.
 
     private static String formatExpression(String exp)
     {
         if(exp.startsWith("-"))
         {
             exp = "0" + exp;
         }
         exp = exp.replaceAll("\\)\\(", "\\)\\*\\(");
        
         Pattern pattern = Pattern.compile("\\)\\d");
         Matcher matcher = pattern.matcher(exp);
 
         StringBuffer str = new StringBuffer(exp);
         int i = 1;
 
         while(matcher.find())
         {
             str.insert(matcher.start() + i, "*");
             i++;
         }
         exp = str.toString();
 
         pattern = Pattern.compile("\\d\\(");
         matcher = pattern.matcher(exp);
 
         str = new StringBuffer(exp);
         i = 1;
 
         while(matcher.find())
         {
             str.insert(matcher.start() + i, "*");
             i++;
         }
         exp = str.toString();
 
         exp = exp.replaceAll("\\(\\-", "\\(0\\-");
 
         //System.out.println("fromatted exp : "+exp);
         return exp;
     }
 
 
     private void getPostfixExpression() {
          
         StringTokenizer tokenizer = new StringTokenizer(infixExpression, OPERATORS, true);
         while(tokenizer.hasMoreTokens()){
             
             this.nextElement = tokenizer.nextToken();
             this.addNextElement();
 
         }
     
         this.popAllOperators();
     }
 
 
     private void addNextElement() {
         if(!this.nextElement.matches("[\\+\\-\\*\\/\\(\\)\\^]"))
         {
             // if the nextElement is value
             this.postfixExpression += this.nextElement + " ";       
         }
         else{
             // if the nextElement is operator
             if(this.stack.empty() || this.nextElement.equals("("))
             {
                 this.stack.push(this.nextElement);
             }
             else if(this.nextElement.equals(")"))
             {
                 while(!this.stack.peek().equals("("))
                 {
                     this.postfixExpression += this.stack.pop() + " ";
                 }
                 this.stack.pop();
             }
             else
             {
                 this.addElementByCheckingPriority();
             }
                 
         }
     }
 
     private void addElementByCheckingPriority() {
 
         String topElement = this.stack.peek();
         if(topElement.equals(this.nextElement))
         {
             this.postfixExpression += this.nextElement + " ";
         }
         else if((FIRST_PRECIDENCE.contains(topElement) && SECOND_PRECIDENCE.contains(this.nextElement)) || (SECOND_PRECIDENCE.contains(topElement) && THIRD_PRECIDENCE.contains(this.nextElement)) || ((FIRST_PRECIDENCE.contains(topElement) && FIRST_PRECIDENCE.contains(this.nextElement)) || (SECOND_PRECIDENCE.contains(topElement) && SECOND_PRECIDENCE.contains(this.nextElement)) || (THIRD_PRECIDENCE.contains(topElement) && THIRD_PRECIDENCE.contains(this.nextElement))))
         {
             this.postfixExpression += this.stack.pop() + " ";
             this.addNextElement();
         }
         else{
             this.stack.push(this.nextElement);
         }
     }
 
     private void popAllOperators() {
         while(!this.stack.empty())
         {
             this.postfixExpression += this.stack.pop() + " ";
         }
         this.postfixExpression = this.postfixExpression.trim();
     }
 
     /**
      * InnerCalculator for solve the postfix expression
      */
     private class PostfixSolver {
 
         private String pfPostfixExpression;
         private String pfNextElement;
         private Stack<String> pfStack;
         private String result;
 
         public PostfixSolver()
         {
             this.pfStack = new Stack<String>();
         }
         
         public String solvePostfix(String exp)
         {
             this.pfPostfixExpression = exp; 
             this.result = exp;
             
             StringTokenizer tokenizer = new StringTokenizer(pfPostfixExpression, " ");
             while(tokenizer.hasMoreTokens())
             {
                 this.pfNextElement = tokenizer.nextToken();
                 if(!this.pfNextElement.matches("[\\+\\-\\*\\^\\/]"))
                 {
                     // if the next element is not operator then push it into stack.
 
                     this.pfStack.push(this.pfNextElement);
                 }
                 else{
                     this.solve();
                 }
             }
             String temp = "";
 
             if((temp = this.result).endsWith(".0"))
             {
                 this.result = temp.replace(".0", "");
             }
 
             return this.result;
         }
 
         private void solve() {
             String value2 = this.pfStack.pop();
             String value1 = this.pfStack.pop();
 
             double a = Double.parseDouble(value1);
             double b = Double.parseDouble(value2);
             double answer = 0.0d;
 
             switch (this.pfNextElement) {
                 case "+":
                     answer = a+b;
                     break;
                 case "-":
                     answer = a-b;
                     break;
                 case "*":
                     answer = a*b;
                     break;
                 case "/":
                     answer = a/b;
                     break;
                 case "^":
                     answer = Math.pow(a, b);
                     break;
                 default:
                 System.out.println("unknown opertor");
                     break;
             }
             this.result = Double.toString(answer);
             
             this.pfStack.push(this.result);
         }
         
     }
 
 }
 
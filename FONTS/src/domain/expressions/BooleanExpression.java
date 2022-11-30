package src.domain.expressions;
//import java.io.*;
//import java.util.*;

public class BooleanExpression{
    //Estructures de la classe
    private String expression;
    ExpressionTreeNode root;


    //funcions publiques

    /**
     * @cost 0(1) 
     * @param //the string of the expression
     */
    private BooleanExpression(String expr,ExpressionTreeNode rot){
        expression=expr.trim();
        root=rot;
    }

    static public BooleanExpression createBooleanExpression(String expr) throws Exception{
        ExpressionTreeNode rot=new ExpressionTreeNode();
        buildTree(rot,expr);
        return new BooleanExpression(expr,rot);
    }

    /**
     * @cost 0(1) 
     * @return the string of the expression 
     */
    public String getExpression(){
        return expression;
    }

    public ExpressionTreeNode getRoot(){
        return root;
    }
    /**
     * @cost 
     * @return true if the expression is valid, else false
     */
    /*private boolean checkExpression(){
        root=new ExpressionTreeNode();
        return buildTree(root,expression);
    }*/
 
    public String printTree(){//nomes per veure si va be, eliminar desprs
        return print(root);
    }

    //funcions privades
    /**
     * @cost 
     * @param  //the root of the tree and the expression to be converted to a tree
     * @return true if the expression is correct, else false
     */
    static private void buildTree(ExpressionTreeNode rot,String s)throws Exception{
        while(parentesis(s)){
            s=s.substring(1,s.length()-1);
        }
        if(s.length()==0) throw new Exception("Incorrect expression: missing operand");
        int index =findLastOperation(s);//també fa control d'errors
        if(index==-1) throw new Exception("Algo passa");//no haura de saltar mai aquesta
        if(index==-2){
            
            if(missingOperator(s)) throw new Exception("Incorrect expression: missing operator");
            rot.setValue(s);
            return;
        }
        else{

            String val=String.valueOf(s.charAt(index));
            rot.setValue(val);
            String s1= s.substring(0,index).trim();
            String s2= s.substring(index+1,s.length()).trim();
            if(!val.equals("!")){
                 ExpressionTreeNode l = new ExpressionTreeNode();
                buildTree(l, s1);
                 rot.setLeft(l);
                 ExpressionTreeNode r = new ExpressionTreeNode();
                buildTree(r, s2);
                 rot.setRight(r);
                 //Exception exc = new Exception("aaasaaa"); 

                //return build(s.l).map(l -> build(r).map(r -> new Node(rot, l, r) per tornar excepcions
                return;
            }
            else{
                 ExpressionTreeNode r = new ExpressionTreeNode();
                 buildTree(r, s2);
                 rot.setRight(r);
                 if(s1.length()>0) throw new Exception("Incorrect expression: missing operator");

                return;
            }
            
            
            
        }
    }

    static private boolean missingOperator(String s){
        char[] x = s.trim().toCharArray();
        if(x[0]=='{'){
            int count=0;
            boolean estaBuit=true;
            for(char c: x){
                if (count>=2) return true;
                if(c=='}' |c == '{' ) {
                    count++;
                }
                else if( c!=' '){
                    estaBuit=false;
                }
                
            }
            if(estaBuit) return true;
        }
        else if(x[0]=='"'){
            int count = 0;
            boolean estaBuit=true;
            for(char c: x){
                if(count>=2) return true;
                if(c=='"') count++;
                else if( c!=' '){
                    estaBuit=false;
                }
            }
            if(estaBuit) return true;
        }
        else{
            for(char c: x){
                if(c==' ') return true;
            }
        }
        return false;
    }

    /**
     * @cost 
     * @param //a expression
     * @return the index of the last operation that should be computed, -1 if its not correct, -2 if there arent any operations
     */
    static private int findLastOperation(String s) throws Exception{
        char[] x = s.toCharArray();
        int[] ranking = new int[s.length()];
        int rank = 0;
        int index = 0;
        boolean cntrCorxetes=false;
        boolean cntrCometes=false;
        int ultimParentesi=0;
        for(char c : x)
        { // Increment through every character in your string.
            if(!cntrCorxetes && !cntrCometes && (c == '&' || c == '|' || c == '!')) ranking[index] = rank;
            else if(!cntrCorxetes && !cntrCometes && c =='('){
                    ranking[index] = -1;
                    rank++;
            }
            else if(!cntrCorxetes && !cntrCometes && c==')'){
                ranking[index] = -1;
                rank--;
            }
            else if(!cntrCorxetes && !cntrCometes && c=='{'){
                ranking[index] = -1;
                cntrCorxetes=true;
            }
            else if(c=='}' && !cntrCometes){
                ranking[index] = -1;
                if(cntrCorxetes) cntrCorxetes=false;
                else throw new Exception("Incorrect expression: there is a problem with brakets");//la expressio no es correcte
            }
            else if(c=='"' && !cntrCorxetes){
                ranking[index] = -1;
                cntrCometes=!cntrCometes;
            }
            else ranking[index] = -1;
            
            if(ultimParentesi==index-1 && (c==')'))throw new Exception("Incorrect expression: there is a problem with parentheses"); //la expressio es incorrecte
            if(ultimParentesi==index-1 && (c==' ')) ultimParentesi++;
            
            if(rank<0) throw new Exception("Incorrect expression: there is a problem with parentheses");//la expressió no és correcte
            index++;
        }
        if(rank!=0) throw new Exception("Incorrect expression: there is a problem with parentheses");//la expressió no és correcte
        if(cntrCometes) throw new Exception("Incorrect expression: there is a problem with quotation marks");
        if(cntrCorxetes)throw new Exception("Incorrect expression: there is a problem with brakets"); //la expressio no es correcte
        int min = s.length() + 1;
        int indexMin = -2;
        for (int i = 0; i < ranking.length; ++i) {
            if (ranking[i] != -1)
                if (ranking[i] < min) {
                    min = ranking[i];
                    indexMin = i;
                }
                else if(ranking[i] == min && hasLowerPreference(x[i], x[indexMin])) {//quina es la preferencia?
                    min = ranking[i];
                    indexMin = i;
                }
            }
        return indexMin;//si torna -2 vol dir que no queden operacions 
    }

    /**
     * @cost 0(1)
     * @param //two operators (|,&,!)
     * @return true if iact has lower preference, else return false
     */
    static private Boolean hasLowerPreference(char iact, char imin) {
        if (imin == '|') return false;
        if (iact == '|') return true;
        if (imin == '&') return false;
        if (iact == '&') return true;
        else return false;
    }

    private String print(ExpressionTreeNode r){
        if(r.getRight()!=null & r.getLeft()!=null){
            return "["+ print(r.getLeft())+"]"+r.getValue()+"["+print(r.getRight())+"]";
        }
        else if(r.getValue().equals("!")){
            return r.getValue()+"["+print(r.getRight())+"]";
        }
        else return r.getValue();
    }

    /**
     * @cost 0(n) ,n=mida s
     * @param //a string s
     * @return returns true if it has a parentesis that contains everything else
     */
    static private boolean parentesis(String s){//comprova si hi ha un parentesis que conté la expressió sencera
        int count=0;
        if(s.length()>1 && s.charAt(0)=='(' && s.charAt(s.length()-1)==')'){
            for(int i=0;i<s.length();i++){
                if(s.charAt(i)=='(') count++;
                else if(s.charAt(i)==')') count--;

                if(count==0 && i!=s.length()-1) return false;

            }
            return true;
        }
        else return false;
    }


    
    
}

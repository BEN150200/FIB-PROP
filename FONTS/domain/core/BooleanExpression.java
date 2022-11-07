package domain.core;
import java.io.*;
import java.util.*;

public class BooleanExpression{
    //Estructures de la classe
    private String expression;
    ExpressionTreeNode root;


    //funcions publiques

    public BooleanExpression(String expr){
        expression=expr;
    }

    public String getExpression(){
        return expression;
    }

    public boolean checkExpression(){
        root=new ExpressionTreeNode(expression);
        return buildTree(root,expression);
    }

    public HashSet<Long> solveExpression(){
        return root.solveExpression();
    }


    public void printTree(){//nomes per veure si va be, eliminar desprs
        print(root);
    }

    //funcions privades

    private boolean buildTree(ExpressionTreeNode rot,String s){
        
        //if(s.charAt(0)=='|' || s.charAt(0)=='&' || s.charAt(s.length()-1)=='|' || s.charAt(s.length()-1)=='&') return false;
        while(parentesis(s)){
            s=s.substring(1,s.length()-1);
        }
        if(s.length()==0) return false;
        int index =findLastOperation(s);//també fa control d'errors
        if(index==-1) return false;//vol dir que hi ha error en la expresio
        if(index==-2){
            rot.setValue(s);
            return true;
        }
        else{

            String val=String.valueOf(s.charAt(index));
            rot.setValue(val);
            System.out.println(index);
            //System.out.println("Pas 6");
            String s1= s.substring(0,index);
            String s2= s.substring(index+1);
            if(!val.equals("!")){
                 ExpressionTreeNode l = new ExpressionTreeNode(s1);
                 rot.setLeft(l);
                 ExpressionTreeNode r = new ExpressionTreeNode(s2);
                 rot.setRight(r);
                 //Exception exc = new Exception("aaasaaa"); 
                 return buildTree(l, s1) & buildTree(r, s2);//return build(s.l).map(l -> build(r).map(r -> new Node(rot, l, r) per tornar excepcions
            }
            else{
                 System.out.println("Pas 8");
                 ExpressionTreeNode r = new ExpressionTreeNode(s2);
                 rot.setRight(r);
                 System.out.println("Pas 9");
                 if(s1.length()>0) return false;
                 return buildTree(r, s2);
            }
            
            
            
        }
    }

    private int findLastOperation(String s){
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
                else return -1;//la expressio no es correcte
            }
            else if(c=='"' && !cntrCorxetes){
                ranking[index] = -1;
                cntrCometes=!cntrCometes;
            }
            else ranking[index] = -1;
            
            if(ultimParentesi==index-1 && (c==')'))return -1; //la expressio es incorrecte
            if(ultimParentesi==index-1 && (c==' ')) ultimParentesi++;
            
            if(rank<0) return -1;//la expressió no és correcte
            index++;
        }
        if(rank!=0) return -1;//la expressió no és correcte
        if(cntrCometes || cntrCorxetes) return -1;//la expressio no es correcte
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
        return indexMin;//si torna -1 és expressió incorrecta, si torna -2 vol dir que no queden operacions 
    }

    private Boolean hasLowerPreference(char iact, char imin) {
        if (imin == '|') return false;
        if (iact == '|') return true;
        if (imin == '&') return false;
        if (iact == '&') return true;
        else return false;
    }

    
    private void print(ExpressionTreeNode r){
        if(r.right!=null & r.left!=null){
            System.out.print('[');
            print(r.left);
            System.out.print(']'+ r.value + '[');
            print(r.right);
            System.out.print(']');
        }
        else if(r.value.equals("!")){
            System.out.print(r.value + '[');
            print(r.right);
            System.out.print(']');
        }
        else System.out.print(r.value);

    }

    private boolean parentesis(String s){//comprova si hi ha un parentesis que conté la expressió sencera
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

package domain.core;

import java.io.*;
import java.util.*;
import domain.indexing.BooleanModel;

public class ExpressionTreeNode{
    ExpressionTreeNode right, left;
    String value;

    /**
     * @cost 0(1)
     * @param the value of the node
     */
    public ExpressionTreeNode(String s) {
        left = null;
        right = null;
        value = s;
    }

    /**
     * @cost 0(1)
     * @param the value and son of the node
     */
    public ExpressionTreeNode(String s, ExpressionTreeNode i, ExpressionTreeNode d) {
        left = i;
        right = d;
        value = s;
    }

    /**
     * @cost 0(1)
     * @param the right son of the node
     */
    public void setRight(ExpressionTreeNode d)
    {
        right = d;
    }

    /**
     * @cost 0(1)
     * @return returns the right son
     */
    public ExpressionTreeNode getRight()
    {
        return right;
    }

    /**
     * @cost 0(1)
     * @param the left son of the node
     */
    public void setLeft(ExpressionTreeNode i)
    {
        left = i;
    }

    /**
     * @cost 0(1)
     * @return returns the left son
     */
    public ExpressionTreeNode getLeft()
    {
        return left;
    }

    /**
     * @cost 0(1)
     * @return returns the value of the node
     */
    public String getValue()
    {
        return value;
    }

    /**
     * @cost 0(1)
     * @param the value of the node
     */
    public void setValue(String s)
    {
        value = s;
    }

    private HashSet<Long> intersection(HashSet<Long> x, HashSet<Long> y){
        HashSet<Long> z=new HashSet<Long>();
        if(x.size()<y.size()){
            for(Long i :x){
                if(y.contains(i)) z.add(i); 
            }
            return z;
        }
        else{
            for(Long i :y){
                if(x.contains(i)) z.add(i);
            }
            return z;
        }
    }

    private HashSet<Long> union(HashSet<Long> x, HashSet<Long> y){
        if(x.size()<y.size()){
            for(Long i :x){
                //if(!y.contains(i)) y.add(i); 
                y.add(i);
            }
            return y;
        }
        else{
            for(Long i :y){
                //if(!x.contains(i)) x.add(i); 
                x.add(i);
            }
            return x;
        }
    }
    private HashSet<Long> negate(HashSet<Long> x){
        HashSet<Long> tot=new HashSet<Long>();//tot = all()//..............................
        for(Long i:x){
            tot.remove(i);
        }
        return tot;
    }

    

    

    public HashSet<Long> solveExpression(){
        if(value.charAt(0)=='&') return intersection(left.solveExpression(),right.solveExpression());
        else if(value.charAt(0)=='|') return union(left.solveExpression(),right.solveExpression());
        else if(value.charAt(0)=='!') return negate(right.solveExpression());
        //sino vol dir que és node final
        String aux = eraseSpacesValue();
        if(aux.charAt(0)=='{'){//llista de paraules
            aux=aux.substring(1,aux.length()-1);//treiem els {}
            String[] list=aux.split(" ");
            //return querySet(list);//...............................
        }
        else if(aux.charAt(0)=='"'){//cadena de paraules
            aux=aux.substring(1,aux.length()-1);//treiem els ""
            String[] list=aux.split(" ");

            //return querySequence(list) //ha de tornar totes les frases que tenen la frase.................................

        }
        else{
            //return queryTermVavr(aux);//.........................................................
        }
    }

    private char firstUsefulChar(){
        char[] x = value.toCharArray();
        for(char i:x){
            if(i!=' ') return i;
        }
        return ' ';
    }

    private String eraseSpacesValue(){
        int first=-1;
        int last=value.length();
        char[] x = value.toCharArray();
        int pos=0;
        for(char i:x){
            if(i!=' ' & first==-1) first=pos;
            if(i!=' ')last=pos;


            pos++;
        }
        return value.substring(first,last+1);
    }
}
package DomainLayer.Classes;

import java.io.*;
import java.util.*;

public class ExpressionTreeNode{
    ExpressionTreeNode right, left;
    String value;

    public ExpressionTreeNode(String s) {
        left = null;
        right = null;
        value = s;
    }

    public ExpressionTreeNode(String s, ExpressionTreeNode i, ExpressionTreeNode d) {
        left = i;
        right = d;
        value = s;
    }


    public void setRight(ExpressionTreeNode d)
    {
        right = d;
    }

    public ExpressionTreeNode getRight()
    {
        return right;
    }

    public void setLeft(ExpressionTreeNode i)
    {
        left = i;
    }

    public ExpressionTreeNode getLeft()
    {
        return left;
    }

    public String getValue()
    {
        return value;
    }

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
        HashSet<Long> tot=new HashSet<Long>();//tot = agafa tots els documents crid a una altre classe......................
        for(Long i:x){
            tot.remove(i);
        }
        return tot;
    }

    private char firstUsefulChar(){
        char[] x = value.toCharArray();
        for(char i:x){
            if(i!=' ') return i;
        }
        return ' ';
    }

    private String sequence(){
        int first=-1;
        int last=value.length();
        char[] x = value.toCharArray();
        int pos=0;
        for(char i:x){
            if(i!=' ' & first==-1) first=pos;
            if(i!=' ')last=pos;


            pos++;
        }
        return value.substring(first,last);
    }

    public HashSet<Long> solveExpression(){
        if(value.charAt(0)=='&') return intersection(left.solveExpression(),right.solveExpression());
        else if(value.charAt(0)=='|') return union(left.solveExpression(),right.solveExpression());
        else if(value.charAt(0)=='!') return negate(right.solveExpression());
        //sino vol dir que és node final
        char op= firstUsefulChar();
        if(op=='{'){//llista de paraules
            
        }
        else if(op=='"'){//cadena de paraules
            String aux = sequence();
            //return buscafrases(aux) //ha de tornar totes les frases que tenen la frase
        }
        else{

        }
    }
}
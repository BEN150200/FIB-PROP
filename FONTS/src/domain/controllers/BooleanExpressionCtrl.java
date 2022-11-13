package src.domain.controllers;

import src.domain.core.BooleanExpression;
import src.domain.core.ExpressionTreeNode;

//import java.io.*;
import java.util.*;

public class BooleanExpressionCtrl {
    private Map<String,BooleanExpression> savedExpressions= new HashMap<String,BooleanExpression>();//no val la pena ordenarho no?, potser valdria la pena un map
    private ArrayList<BooleanExpression> historial=new ArrayList<BooleanExpression>();//aqui si que un vector aixi queden ordenats per entrada, aixo o una queue i quan poso 1 trec el primer si la cua te mida 10 (o la del historial)
    private int midaHistorial=10;

    private static BooleanExpressionCtrl instance = null;

    public static BooleanExpressionCtrl getInstance() {
        if (instance == null) {
            instance = new BooleanExpressionCtrl();
        }
        return instance;
    }

    public BooleanExpressionCtrl(){}
    /**
     * @cost 0(n) n=number of saved expressions
     * @return Names and expression of all the saved expressions 
     */
    public HashMap<String,String> getSavedExpressionsNames(){//Torna els noms de les expressions guardades
        HashMap<String,String> names= new HashMap<String,String>();
        for(Map.Entry<String, BooleanExpression> i : savedExpressions.entrySet() ){
            
            names.put(i.getKey(),i.getValue().getExpression());
            
        }
        return names;
    }

    /**
     * @cost 0(1)
     * @param The name of the desired expression
     * @return If it exists returns the expression, else exception
     */
    public String getExpression(String name){//retorna una string buida si no existeix
        BooleanExpression aux=savedExpressions.get(name);
        if(aux==null) return null;
        return aux.getExpression(); 
    }

    /**
     * @cost 0(1)
     * @return The most recent expressions
     */
    public ArrayList<String> getHistorialExpressions(){//Torna les expressions recents
        ArrayList<String> exp= new ArrayList<String>(0);
        for(int i=0; i<historial.size();i++){
            BooleanExpression s=historial.get(i);
            exp.add(s.getExpression());
            //System.out.println(s.getExpression());
        }
        return exp;
    }

    /**
     * @cost 
     * @param The expression and its name 
     * @return Return true if the expression is valid, false if invalid
     */
    public boolean saveExpression(String exp, String name){
        BooleanExpression newexp=new BooleanExpression(exp);
        if(newexp.checkExpression()==false) return false;
        savedExpressions.put(name,newexp);
        return true;
    }

    /**
     * @cost 
     * @param Name of the expression that you want the tree of
     * @return Return null if the expression do not exist, else returns the root of the expressions tree
     */
    public ExpressionTreeNode getSavedExpressionTree(String name){
        BooleanExpression aux=savedExpressions.get(name);
        if(aux==null) return null; //si no existeix return null (canviare a excepcio)
        //addHistorial(aux);
        return aux.getRoot();
    }

    /**
     * @cost 
     * @param The expression to be solved 
     * @return returns null if the expression is not correct, else returns the root of the expressions tree
     */
    public ExpressionTreeNode createExpressionTree(String exp){

        BooleanExpression newexp=new BooleanExpression(exp);
        if(newexp.checkExpression()==false) return null;//haura de retornar excepcio si no es correcte
        //addHistorial(newexp);
        return newexp.getRoot();
    }
    
    /**
     * @cost 0(1)
     * @param The name of the expression to be deleted
     * @return false if the expression did not exist, else true
     */
    public boolean deleteExpression (String name){
        BooleanExpression aux=savedExpressions.get(name);
        if(aux==null)  return false;//si no existeix return excepcio
        else savedExpressions.remove(name);
        return true;
    }

    public boolean existsBooleanExpression(String boolExpName){
        return (savedExpressions.get(boolExpName)!=null);
    }

    /**
     * @cost 0(1)
     * @param a boolean expression
     */
    private void addHistorial(BooleanExpression expr){
        historial.add(expr);
        if(historial.size()>midaHistorial) historial.remove(0);
    }
}
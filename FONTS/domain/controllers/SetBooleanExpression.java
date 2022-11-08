package domain.controllers;

import domain.core.BooleanExpression;

import java.io.*;
import java.util.*;

public class SetBooleanExpression {
    private Map<String,BooleanExpression> savedExpressions= new TreeMap<String,BooleanExpression>();//no val la pena ordenarho no?, potser valdria la pena un map
    private ArrayList<BooleanExpression> historial=new ArrayList<BooleanExpression>();//aqui si que un vector aixi queden ordenats per entrada, aixo o una queue i quan poso 1 trec el primer si la cua te mida 10 (o la del historial)
    private int midaHistorial=10;

    private static SetBooleanExpression instance = null;

    public static SetBooleanExpression getInstance() {
        if (instance == null) {
            instance = new SetBooleanExpression();
        }
        return instance;
    }

    public SetBooleanExpression(){}
    /**
     * @cost 0(n) n=number of saved expressions
     * @return Names of all the saved expressions 
     */
    public ArrayList<String> getSavedExpressionsNames(){//Torna els noms de les expressions guardades
        ArrayList<String> names= new ArrayList<String>(0);
        Iterator<String> it = savedExpressions.keySet().iterator();
        while(it.hasNext()){
            String clave = it.next();
            names.add(clave);
            System.out.println(clave);
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
     * @param Name of the expression you want to solve 
     * @return Return null if the expression do not exist, else returns a HashSet with all the Sentences id that obey the expression
     */
    public HashSet<Long> solveSavedExpression(String name){
        BooleanExpression aux=savedExpressions.get(name);
        if(aux==null) return null; //si no existeix return null (canviare a excepcio)
        addHistorial(aux);
        return aux.solveExpression();
    }

    /**
     * @cost 
     * @param The expression to be solved 
     * @return returns null if the expression is not correct, else returns a HashSet with all the Sentences id that obey the expression
     */
    public HashSet<Long> solveExpression(String exp){

        BooleanExpression newexp=new BooleanExpression(exp);
        if(newexp.checkExpression()==false) return null;//haura de retornar excepcio si no es correcte
        addHistorial(newexp);
        return newexp.solveExpression();
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

    /**
     * @cost 0(1)
     * @param a boolean expression
     */
    private void addHistorial(BooleanExpression expr){
        historial.add(expr);
        if(historial.size()>midaHistorial) historial.remove(0);
    }
}

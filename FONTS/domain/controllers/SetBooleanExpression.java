package domain.controllers;

import domain.core.BooleanExpression;

import java.io.*;
import java.util.*;

public class SetBooleanExpression {
    private Map<String,BooleanExpression> savedExpressions= new TreeMap<String,BooleanExpression>();//no val la pena ordenarho no?, potser valdria la pena un map
    private ArrayList<BooleanExpression> historial=new ArrayList<BooleanExpression>();//aqui si que un vector aixi queden ordenats per entrada, aixo o una queue i quan poso 1 trec el primer si la cua te mida 10 (o la del historial)
    private int midaHistorial=10;

    public SetBooleanExpression(){}

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
    public String getExpression(String name){//retorna una string buida si no existeix
        BooleanExpression aux=savedExpressions.get(name);
        if(aux==null) return null;
        return aux.getExpression(); 
    }

    public ArrayList<String> getHistorialExpressions(){//Torna les expressions recents
        ArrayList<String> exp= new ArrayList<String>(0);
        for(int i=0; i<historial.size();i++){
            BooleanExpression s=historial.get(i);
            exp.add(s.getExpression());
            //System.out.println(s.getExpression());
        }
        return exp;
    }

    public boolean saveExpression(String exp, String name){
        BooleanExpression newexp=new BooleanExpression(exp);
        if(newexp.checkExpression()==false) return false;
        savedExpressions.put(name,newexp);
        return true;
    }

    public HashSet<Long> solveSavedExpression(String name){
        BooleanExpression aux=savedExpressions.get(name);
        if(aux==null) return null; //si no existeix return null (canviare a excepcio)
        addHistorial(aux);
        return aux.solveExpression();
    }

    public HashSet<Long> solveExpression(String exp){

        BooleanExpression newexp=new BooleanExpression(exp);
        if(newexp.checkExpression()==false) return null;//haura de retornar excepcio si no es correcte
        addHistorial(newexp);
        return newexp.solveExpression();
    }
    
    public void deleteExpression (String name){
        BooleanExpression aux=savedExpressions.get(name);
        if(aux==null)  ;//si no existeix return excepcio
        else savedExpressions.remove(name);
    }

    private void addHistorial(BooleanExpression expr){
        historial.add(expr);
        if(historial.size()>midaHistorial) historial.remove(0);
    }
}

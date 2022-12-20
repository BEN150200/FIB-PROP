package src.domain.controllers;

import src.domain.expressions.BooleanExpression;
import src.domain.expressions.ExpressionTreeNode;

//import java.io.*;
import java.util.*;

public class BooleanExpressionCtrl {
    private Map<String,BooleanExpression> savedExpressions= new HashMap<String,BooleanExpression>();//no val la pena ordenarho no?, potser valdria la pena un map

    private static BooleanExpressionCtrl instance = null;

    public static BooleanExpressionCtrl getInstance() {
        if (instance == null) {
            instance = new BooleanExpressionCtrl();
        }
        return instance;
    }

    public BooleanExpressionCtrl(){}

    public HashMap<String,String> getSavedExpressionsNames(){//Torna els noms i expressio de les expressions guardades
        HashMap<String,String> names= new HashMap<String,String>();
        for(Map.Entry<String, BooleanExpression> i : savedExpressions.entrySet() ){
            
            names.put(i.getKey(),i.getValue().getExpression());
            
        }
        return names;
    }

    public String getExpression(String name){//retorna la expressio associada al nom o una string buida si no existeix
        BooleanExpression aux=savedExpressions.get(name);
        if(aux==null) return null;
        return aux.getExpression(); 
    }

    public void saveExpression(String exp, String name) throws Exception {//guarda una nova expressio si es correcte
        BooleanExpression newexp = BooleanExpression.createBooleanExpression(exp);
        savedExpressions.put(name,newexp);
    }

    public ExpressionTreeNode getSavedExpressionTree(String name){ //retorna la arrel del arbre associat a la expressió identificada per name
        BooleanExpression aux=savedExpressions.get(name);
        if(aux==null) return null;
        return aux.getRoot();
    }

    public ExpressionTreeNode createExpressionTree(String exp) throws Exception{ //crea l'arbre de la expressió i torna la seva arrel si aquest és correcte
        BooleanExpression newexp = BooleanExpression.createBooleanExpression(exp);
        return newexp.getRoot();
    }

    public boolean deleteExpression (String name){
        BooleanExpression aux=savedExpressions.get(name);
        if(aux==null)  return false;
        else savedExpressions.remove(name);
        return true;
    }

    public boolean existsBooleanExpression(String boolExpName){
        return (savedExpressions.get(boolExpName)!=null);
    }


    public void clear(){ //buida la informació guardada
        savedExpressions.clear();
    }
}

package DomainLayer.Classes;

import java.io.*;
import java.util.*;
public class SetBooleanExpression {
    private Map<String,BooleanExpression> savedExpressions= new TreeMap<String,BooleanExpression>();//no val la pena ordenarho no?, potser valdria la pena un map
    private Stack<BooleanExpression> historial=new Stack<BooleanExpression>();//aqui si que un vector aixi queden ordenats per entrada, aixo o una queue i quan poso 1 trec el primer si la cua te mida 10 (o la del historial)


    public SetBooleanExpression(){}

    public ArrayList<String> getSavedExpressionsNames(){//Torna els noms de les expressions guardades
        ArrayList<String> names= new ArrayList<String>(0);//ara mateix nomes va amb mida 2
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
            BooleanExpression s=historial.pop();
            exp.add(s.getExpression());
            System.out.println(s.getExpression());
            historial.push(s);
        }
        return exp;
    }

    public boolean saveExpression(String exp, String name){
        BooleanExpression newexp=new BooleanExpression(exp);
        if(newexp.checkExpression()==false)return false;
        savedExpressions.put(name,newexp);
        return true;
    }
    
}

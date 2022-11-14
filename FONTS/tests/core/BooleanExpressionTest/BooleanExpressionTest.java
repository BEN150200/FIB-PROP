package tests.core.BooleanExpressionTest;


import io.vavr.Tuple2;
import src.domain.expressions.BooleanExpression;

import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class BooleanExpressionTest {
    HashMap<String,Boolean> list = new HashMap<>();
    @Before
    public void setUp() throws Exception {


        list.put("hola|adeu",true);//casos basics
        list.put("hola&adeu",true);
        list.put("!hola",true);
        list.put("{hola adeu}",true);
        list.put("\"hola adeu!\"",true);
        list.put("hola",true);

        list.put("{hola adeu} | !(dia) & !\"com estas\"",true);//casos mes raros
        list.put("(hola|!(adeu & estas)) & {bon dia}",true);
        list.put("   adeu&hola|        ({que passa   }      & !      dia )",true);

        list.put("(hola",false);//parentització incorrecte
        list.put("((hola|adeu)& !(dia)",false);
        list.put("hola ()|adeu",false); //si posem "hola()" sense espais donaria correcte pq interpreta que vols buscar la paraula amb parentesis inclosos
        list.put("adeu| (hola&!(dia)))",false);

        list.put("{adeu hola& !dia",false);//corxetes mal tancats
        list.put("bon dia} &!hola",false);
        list.put("{hola adeu} | {bon dia",false);

        list.put("\" hola bon dia",false);//cometes mal tancades
        list.put("\"bon dia | hola &!adeu",false);
        list.put("bon &dia | hola bones\"",false);
        list.put("\"bon dia\" & \"bona tarda",false);

        list.put("|hola & dia",false);//falta un operand
        list.put("hola| &{bon dia}",false);
        list.put("adeu & !",false);
        list.put("bon & dia &",false);
        list.put("hola | | !adeu",false);

        list.put("hola adeu",false);//falta un operador
        list.put("hola& adeu | {bon dia}\"bona tarda\"",false);
        list.put("adeu | hola \"que tal\"",false);
        list.put("adeu |!(bon dia)",false);
        list.put("\"bon dia\" adeu|hola&bones",false);

        list.put("",false);//el cas buit és incorrecte

        list.put("hola | {     }",false);//corxetes o cometes buides
        list.put("hola | \"  \"",false);

        list.put("hola|(adeu&!(bon & dia | \"que tal\") | !!!!!{hola } & !3) | !nit", true);//casos extrems
        list.put("a & b & c & d & e & f & g | !( h| i| j |k |l) & \" m n o\"" ,true);



    }

    @Test
    public void getExpression() {
        BooleanExpression test = BooleanExpression.createBooleanExpression("hola|adeu&!\"bon dia\"");
        assertEquals("hola|adeu&!\"bon dia\"", test.getExpression());
    }

    @Test
    public void getRoot() {
        BooleanExpression test = BooleanExpression.createBooleanExpression("hola|adeu&!\"bon dia\"");
        assertEquals("|", test.getRoot().getValue());
    }

    @Test
    public void checkExpression() { //la funcio comprova si la expressio és correcte i crea l'arbre corresponent
        //primer comprovem que comprova be les expressions
        for(Map.Entry<String, Boolean> i : list.entrySet() ){
            BooleanExpression test = BooleanExpression.createBooleanExpression(i.getKey());
            boolean correcte=true;
            if(test==null) correcte=false;
            assertEquals(i.getValue(),correcte);
        }

        //comprovem que els arbres creats son els esperats
        String expr = "{hola adeu} | !(dia) & !\"com estas\"";
        BooleanExpression test1 = BooleanExpression.createBooleanExpression(expr);
        assertEquals("[{hola adeu}]|[[![dia]]&[![\"com estas\"]]]",test1.printTree());

        expr = "(hola|!(adeu & estas)) & {bon dia}";
        BooleanExpression test2 = BooleanExpression.createBooleanExpression(expr);
        assertEquals("[[hola]|[![[adeu]&[estas]]]]&[{bon dia}]",test2.printTree());

        expr = "   adeu&hola|        ({que passa   }      & !      dia )";
        BooleanExpression test3 = BooleanExpression.createBooleanExpression(expr);
        assertEquals("[[adeu]&[hola]]|[[{que passa   }]&[![dia]]]",test3.printTree());
    }
}
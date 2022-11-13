package tests.domain.core;

import org.junit.Before;
import org.junit.Test;
import src.domain.controllers.BooleanExpressionCtrl;
import src.domain.core.BooleanExpression;

import java.util.HashMap;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class BooleanExpressionCtrlTest {
    BooleanExpressionCtrl test;
    @Before
    public void setUp() throws Exception {
        test= new BooleanExpressionCtrl();
    }

    @Test
    public void getSavedExpressionsNames() {
        test.saveExpression("a|b","Expressio1");
        test.saveExpression("c&d","Expressio2");
        HashMap<String,String> result = new HashMap<>();
        result.put("Expressio1","a|b");
        result.put("Expressio2","c&d");
        assertEquals(result,test.getSavedExpressionsNames());

    }

    @Test
    public void getExpression() {
        test.saveExpression("a|b","Expressio1");
        assertEquals("a|b",test.getExpression("Expressio1"));

        test.saveExpression("a|","Expressio2");//com que la expressió és incorrecte no la guardarà i al buscarla retorna null
        assertEquals(null ,test.getExpression("Expressio2"));

    }

    @Test
    public void saveExpression() {
        assertEquals(true,test.saveExpression("a|b","Expression1"));
        assertEquals(false,test.saveExpression("a|","Expression2"));//si es incorrecte no la afageix
    }

    @Test
    public void getSavedExpressionTree() {
        test.saveExpression("a|b","Expression1");//sabem que l'arbre tindrà com a arrel "|" per com es comnstrueix
        assertEquals("|", test.getSavedExpressionTree("Expression1").getValue());
        assertEquals("a", test.getSavedExpressionTree("Expression1").getLeft().getValue());
        assertEquals("b", test.getSavedExpressionTree("Expression1").getRight().getValue());
    }

    @Test
    public void createExpressionTree() {
        assertEquals("&", test.createExpressionTree("c&d").getValue());//la arrel serà el &
        assertEquals("d", test.createExpressionTree("c&d").getRight().getValue());
        assertEquals("c", test.createExpressionTree("c&d").getLeft().getValue());
    }

    @Test
    public void deleteExpression() {
        test.saveExpression("a|b","Expression1");
        assertEquals(1,test.getSavedExpressionsNames().size());
        assertEquals(false,test.deleteExpression("Expression2"));//no hi ha cap expression2 aixi que torna false
        assertEquals(true,test.deleteExpression("Expression1"));
        assertEquals(0,test.getSavedExpressionsNames().size());
    }

    @Test
    public void existsBooleanExpression() {
        test.saveExpression("a|b","Expression1");
        assertEquals(true,test.existsBooleanExpression("Expression1"));
        assertEquals(false,test.existsBooleanExpression("Expression2"));
    }
}


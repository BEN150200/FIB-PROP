package tests.domain.core;

import src.domain.core.BooleanExpression;

public class BooleanExpressionStub extends BooleanExpression {

    public BooleanExpressionStub(String expr) {
        super(expr);
    }
    public String getExpression(String name){
        System.out.println("Stuuuuuuub");
        return "Stub";
    }
}

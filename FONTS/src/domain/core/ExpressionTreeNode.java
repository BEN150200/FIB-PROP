package src.domain.core;

import java.io.*;
import java.util.*;

import src.domain.indexing.booleanmodel.BooleanModel;

public class ExpressionTreeNode{
    private ExpressionTreeNode right, left;
    private String value;

    /**
     * @cost 0(1)
     * @param the value of the node
     */
    public ExpressionTreeNode() {
        left = null;
        right = null;
        value = null;
    }

    /**
     * @cost 0(1)
     * @param the value and son of the node
     */
    public ExpressionTreeNode(String s, ExpressionTreeNode i, ExpressionTreeNode d) {
        left = i;
        right = d;
        value = s.trim();
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
        value = s.trim();
    }

    
    
}

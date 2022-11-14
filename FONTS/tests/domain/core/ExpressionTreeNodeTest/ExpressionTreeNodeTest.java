package domain.core.ExpressionTreeNodeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import src.domain.expressions.ExpressionTreeNode;

import static org.junit.Assert.*;

public class ExpressionTreeNodeTest {
    ExpressionTreeNode root = new ExpressionTreeNode();

    @Before
    public void setUp() throws Exception {
        String s="root";
        root.setValue(s); //inserta el valor treient els espais a principi i fi
        ExpressionTreeNode right = new ExpressionTreeNode();
        String r="right";
        right.setValue(r); //inserta el valor treient els espais a principi i fi
        ExpressionTreeNode left = new ExpressionTreeNode();
        String l="left";
        left.setValue(l); //inserta el valor treient els espais a principi i fi
        root.setRight(right);
        root.setLeft(left);
        //hem creat un arbre de forma :
        //        root
        //       /    \
        //     left   right
    }

    @Test
    public void getRight() {
        assertEquals("right", root.getRight().getValue());
    }

    @Test
    public void getLeft() {
        assertEquals("left", root.getLeft().getValue());
    }

    @Test
    public void getValue() {
        assertEquals("root", root.getValue());
    }
}
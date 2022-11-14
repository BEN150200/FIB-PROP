package domain.indexing.booleanmodel.BooleanModel.Queries;

import static org.junit.Assert.assertEquals;

import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import org.junit.Test;

import src.domain.expressions.ExpressionTreeNode;
import src.domain.indexing.booleanmodel.BooleanModel;
import src.domain.indexing.core.Index;

public class TestBooleanQueries {
    /*private HashMap<String, Iterable<String>> corpus = Parsing.makeCollection(
        "d1", "and the earth was without form and void",
        "d2", "and darkness was upon the face of the deep",
        "d3", "and the spirit of god moved upon the face of the waters"
    );*/
    // should be equivalent to Index.of(corpus), but it's a stub!
    private BooleanModel<String> model = BooleanModel.of(
        new Index<String>(
            HashMap.of(
                "and", HashMap.of("d1", HashSet.of(0, 6), "d2", HashSet.of(0), "d3", HashSet.of(0)),
                "the", HashMap.of("d1", HashSet.of(1), "d2", HashSet.of(4, 7), "d3", HashSet.of(1, 7, 10)),
                "earth", HashMap.of("d1", HashSet.of(2)),
                "was", HashMap.of("d1", HashSet.of(3), "d2", HashSet.of(2)),
                "without", HashMap.of("d1", HashSet.of(4)),
                "form", HashMap.of("d1", HashSet.of(5)),
                "void", HashMap.of("d1", HashSet.of(7)),
                "darkness", HashMap.of("d2", HashSet.of(1)),
                "upon", HashMap.of("d2", HashSet.of(3), "d3", HashSet.of(6)),
                "face", HashMap.of("d2", HashSet.of(5), "d3", HashSet.of(8))
            ).merge(HashMap.of(
                "of", HashMap.of("d2", HashSet.of(6), "d3", HashSet.of(3, 9)),
                "deep", HashMap.of("d2", HashSet.of(8)),
                "spirit", HashMap.of("d3", HashSet.of(2)),
                "god", HashMap.of("d3", HashSet.of(4)),
                "moved", HashMap.of("d3", HashSet.of(5)),
                "waters", HashMap.of("d3", HashSet.of(11))
            )),
            HashMap.of(
                "d1", HashMap.of("form", HashSet.of(5), "without", HashSet.of(4), "was", HashSet.of(3), "earth", HashSet.of(2), "the", HashSet.of(1), "void", HashSet.of(7), "and", HashSet.of(0, 6)),
                "d2", HashMap.of("was", HashSet.of(2), "deep", HashSet.of(8), "darkness", HashSet.of(1), "the", HashSet.of(4, 7), "of", HashSet.of(6), "and", HashSet.of(0), "upon", HashSet.of(3), "face", HashSet.of(5)),
                "d3", HashMap.of("and", HashSet.of(0), "the", HashSet.of(1, 7, 10), "spirit", HashSet.of(2), "of", HashSet.of(3, 9), "god", HashSet.of(4), "moved", HashSet.of(5), "upon", HashSet.of(6), "face", HashSet.of(8), "waters", HashSet.of(11))
            )
        )
    );

    public void nullTree() {
        assertEquals(
            HashSet.empty(),
            model.query(new ExpressionTreeNode())
        );
    }

    public void emptyQuery() {
        assertEquals(
            HashSet.empty(),
            model.query(new ExpressionTreeNode("", null, null))
        );
    }

    @Test
    public void query(){
        {
            ExpressionTreeNode root1 = new ExpressionTreeNode(); //comprovem que les cometes van be
            root1.setValue("\"form and void \"");
            assertEquals(HashSet.of("d1"), model.query(root1));

            ExpressionTreeNode root2 = new ExpressionTreeNode();
            root2.setValue("form and not void");
            assertEquals(HashSet.empty(), model.query(root2));
        }

        {
            ExpressionTreeNode root3 = new ExpressionTreeNode(); //comprovem que els corxetes van be
            root3.setValue("{spirit waters}");
            assertEquals(HashSet.of("d3"), model.query(root3));

            ExpressionTreeNode root4 = new ExpressionTreeNode();
            root4.setValue("{spirit waters no}");
            assertEquals(HashSet.empty(), model.query(root4));
        }
        {
            ExpressionTreeNode root1 = new ExpressionTreeNode(); //comprovem que les paraules soles van be
            root1.setValue("face");
            assertEquals(HashSet.of("d2","d3"), model.query(root1));

            ExpressionTreeNode root2 = new ExpressionTreeNode();
            root2.setValue("noesta");
            assertEquals(HashSet.empty(), model.query(root2));
        }

        {
            ExpressionTreeNode root = new ExpressionTreeNode(); //ara expressions amb operadors
            ExpressionTreeNode right = new ExpressionTreeNode();
            ExpressionTreeNode left = new ExpressionTreeNode();
            ExpressionTreeNode leftRight = new ExpressionTreeNode();
            //    |
            //   / \
            //  !  {spirit of god}
            //   \
            //  darkness
            root.setValue("|");
            right.setValue("{spirit of god}");
            left.setValue("!");
            leftRight.setValue("darkness");

            left.setRight(leftRight);
            root.setRight(right);
            root.setLeft(left);

            assertEquals(HashSet.of("d1","d3"), model.query(root));
        }

        {
            ExpressionTreeNode root = new ExpressionTreeNode(); //ara expressions amb operadors
            ExpressionTreeNode right = new ExpressionTreeNode();
            ExpressionTreeNode left = new ExpressionTreeNode();
            ExpressionTreeNode leftRight = new ExpressionTreeNode();
            //    &
            //   / \
            //  !  {spirit of god}
            //   \
            //  darkness
            root.setValue("&");
            right.setValue("{spirit of god}");
            left.setValue("!");
            leftRight.setValue("darkness");

            left.setRight(leftRight);
            root.setRight(right);
            root.setLeft(left);

            assertEquals(HashSet.of("d3"), model.query(root));
        }

        {
            ExpressionTreeNode root = new ExpressionTreeNode(); //ara expressions amb operadors
            ExpressionTreeNode right = new ExpressionTreeNode();
            ExpressionTreeNode left = new ExpressionTreeNode();
            ExpressionTreeNode leftRight = new ExpressionTreeNode();
            //    &
            //   / \
            //  !  darkness
            //   \
            //  "spirit of god"
            root.setValue("&");
            right.setValue("darkness");
            left.setValue("!");
            leftRight.setValue("\"spirit of god\"");

            left.setRight(leftRight);
            root.setRight(right);
            root.setLeft(left);

            assertEquals(HashSet.of("d2"), model.query(root));
        }

        {
            ExpressionTreeNode root = new ExpressionTreeNode(); //ara expressions amb operadors
            ExpressionTreeNode right = new ExpressionTreeNode();
            ExpressionTreeNode left = new ExpressionTreeNode();
            ExpressionTreeNode leftRight = new ExpressionTreeNode();
            ExpressionTreeNode rightLeft = new ExpressionTreeNode();
            ExpressionTreeNode rightRight = new ExpressionTreeNode();

            //          &
            //    /              \
            //   !                   |
            //    \               /      \
            //"spirit of god"  darkness  earth
            root.setValue("&");
            right.setValue("|");
            left.setValue("!");
            leftRight.setValue("\"spirit of god\"");
            rightLeft.setValue("darkness");
            rightRight.setValue("earth");

            left.setRight(leftRight);
            right.setLeft(rightLeft);
            right.setRight(rightRight);
            root.setRight(right);
            root.setLeft(left);

            assertEquals(HashSet.of("d1","d2"), model.query(root));
        }


    }
}

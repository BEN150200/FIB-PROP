package tests.domain.indexing.booleanmodel.BooleanModel;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import domain.indexing.booleanmodel.BooleanModel;


public class TestBooleanModel {
    @Test
    public void testBoolean() {
        var model = BooleanModel.of(Map.of(
            1, Arrays.asList("hello how are you ma man".split(" ")),
            2, Arrays.asList("very well how are you man".split(" "))
        ));

        assertEquals(model.all(), Set.of(1, 2));

        assertEquals(model.querySequence(List.of("how", "are", "you")), Set.of(1, 2));
        assertEquals(model.querySequence(List.of("how", "are", "you", "ma")), Set.of(1));
    }
}

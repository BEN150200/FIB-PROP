package tests.domain.indexing.core.IndexingController;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

import helpers.Parsing;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;
import io.vavr.control.Try;
import src.domain.indexing.booleanmodel.BooleanModel;
import src.domain.indexing.core.IndexingController;
import src.domain.indexing.vectorial.VectorialModel;

public class TestIndexingController {
   /* @Test
    @SuppressWarnings("deprecation")
    public void testWeightedQuery() {

        var folderPath = "..\\pracs-caim\\s1\\data\\raw\\20_newsgroups";
        
        var files = Parsing.parseFolder(folderPath);

        Map<String, Iterable<String>> corpus = HashMap.ofEntries(files);

        var controller = IndexingController.of(
            VectorialModel.of(corpus.toJavaMap()),
            BooleanModel.<Long>empty()
        );

        controller.weightedQuery("god^2").map(result -> {
            var results = result.toStream()
                .sortBy(Tuple2::_2)
                .reverse()
                .take(5);

            results.forEach(entry ->
                System.out.println(
                    "Doc " + entry._1 + " (score=" + entry._2 + ")\n" +
                    "-----------------------------------\n" +
                    Try.of(() -> Files.readString(Paths.get(entry._1), Charset.forName("ISO-8859-1"))).get() +
                    "\n-----------------------------------\n"
                )
            );
            
            if(results.isEmpty())
                System.out.println("No results");

            return true;
        })
        .mapLeft(err -> {
            System.out.println(err);
            return false;
        });
    }*/
}

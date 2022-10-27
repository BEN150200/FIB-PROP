package DomainLayer.Interfaces;

import java.util.Set;
import DomainLayer.Classes.Word;

public interface WordController {
    public Boolean existsWord(String word);

    public Word getWord(String word);

    public void addWord(Word word);

    public void deleteWord(String word);

    public Set<Word> getAllWords();
}
package p2pbay.testing;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TitleGenerator {
    private String[] words =
            {"test",
            "danger",
            "ed",
            "ana",
            "sala",
            "ist",
            "lx",
            "se",
            "word",
            "caps",
            "pt",
            "lol",
            "casa",
            "title",
            "best",
            "blast"};

    private Set<String> titles;
    private Random wordChooser;
    private int size = 4;

    public TitleGenerator() {
        titles = new HashSet<>();
        wordChooser = new Random();
    }

    public String nextTitle() {
        int i = wordChooser.nextInt(words.length);
        StringBuilder sb = new StringBuilder();
        sb.append(words[i]);
        for (int j = 1; j < size; j++) {
            sb.append(" ");
            sb.append(words[wordChooser.nextInt(words.length)]);
        }
        if (titles.add(sb.toString()))
            return sb.toString();

        return nextTitle();
    }
}

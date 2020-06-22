package com.company;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.apache.log4j.BasicConfigurator;

import java.util.Properties;

public class Pipeline {

    private static Properties properties;
    // tokenize, ssplit, pos, lemma, ner
    private static String propertyNames = "tokenize,ssplit,pos,lemma, ner";
    private static StanfordCoreNLP stanfordCoreNLP;

    private Pipeline () {

    }

    static {
        properties = new Properties();
        properties.setProperty( "annotators", propertyNames);
        properties.setProperty("coref.algorithm", "neural");
    }
    public static StanfordCoreNLP getPipeline() {
        if ( stanfordCoreNLP == null) {
            BasicConfigurator.configure();
            stanfordCoreNLP = new StanfordCoreNLP( properties);
        }
        return stanfordCoreNLP;
    }
}

package com.company;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.List;

public class Stanford {
    private String PLURAL = "NNS";
    private String PAST = "VBD";
    private String PERSON = "PERSON";
    private String COUNTRY = "COUNTRY";
    private String CITY = "CITY";
    private String LOCATION = "LOCATION";
    private String answer;
    public boolean isPlural = false;
    public boolean isPast = false;
    public boolean isPerson = false;
    public boolean isPlace = false;
    public Stanford(String answer) {
        this.answer = answer;
        getPOS();
        getEntity();
    }

    public void getPOS () {
        StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();
        CoreDocument coreDocument = new CoreDocument( answer);

        stanfordCoreNLP.annotate( coreDocument);

        List<CoreLabel> coreLabelList = coreDocument.tokens();
        if ( coreLabelList.size() > 0) {
            String pos = coreLabelList.get(0).get(CoreAnnotations.PartOfSpeechAnnotation.class);
            if (pos.equals(PLURAL)) {
                isPlural = true;
            } else if (pos.equals(PAST)) {
                isPast = true;
            }
        }
    }
    public void getEntity() {
        StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();
        CoreDocument coreDocument = new CoreDocument( answer);

        stanfordCoreNLP.annotate( coreDocument);
        List<CoreLabel> coreLabelList = coreDocument.tokens();
        if ( coreLabelList.size() > 0) {
            String ner = coreLabelList.get(0).get(CoreAnnotations.NamedEntityTagAnnotation.class);
            if (ner.equals(PERSON))
                isPerson = true;
            if (ner.equals(COUNTRY) || ner.equals(CITY) || ner.equals(LOCATION))
                isPlace = true;
        }
    }
}

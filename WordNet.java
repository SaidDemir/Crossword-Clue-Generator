package com.company;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.*;
import net.sf.extjwnl.data.list.PointerTargetNode;
import net.sf.extjwnl.data.list.PointerTargetNodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class WordNet {

    private Analyzer analyzer;
    private String originalSolution;
    private Set <POS> available;
    private HashMap <String, ArrayList<String>> generatedSolutions;

    public WordNet(Set<POS> available, String originalSolution, Analyzer analyzer) throws JWNLException {
        StanfordCoreNLP coreNLP = Pipeline.getPipeline();
        CoreDocument document = new CoreDocument( originalSolution);
        coreNLP.annotate( document);
        List <CoreLabel> lemma = document.tokens();
        originalSolution = lemma.get(0).lemma();
        this.originalSolution = originalSolution;
        this.available = available;
        this.analyzer = analyzer;
        generatedSolutions = new HashMap<>();
        generateSolutions();
    }
    private void generateSolutions() throws JWNLException {

        generateParticiple();
        generateCause();
        generateEntailments();
//        generateDefinition();
        generateSubstanceHolonyms();
        generateSubstanceMeronyms();
        generatePartHolonyms();
        generatePartMeronyms();
        generateAttributes();
        generateSees();
        generateCoordinateTerm();
        generateAntonyms();
        generateSynonyms();
        generateParents();
        generateChildren();
        generateSenses();

        generateMemberHolonyms();
        generateMemberMeronyms();

    }


    /*
    participle
     */
    private void generateParticiple(){
        generatedSolutions.put("participle", new ArrayList<>());
        for ( POS pos : available) {
            IndexWord indexWord = analyzer.getIndexWord( pos);
            List <Synset> senses = indexWord.getSenses();
            for (Synset sense :senses) {
                PointerTargetNodeList participle = RelationshipExplorer.getParticiple( sense);
                for (PointerTargetNode node : participle) {
                    String[] words = StringAnalyzer.getWords(node.toString());
                    for (String word : words)
                        generatedSolutions.get("participle").add(word);
                }
            }
        }
    }

    private void generateDefinition(){
        generatedSolutions.put("meaning", new ArrayList<>());
        for ( POS pos : available) {
            IndexWord indexWord = analyzer.getIndexWord( pos);
            List <Synset> senses = indexWord.getSenses();
            generatedSolutions.get("meaning").add(senses.get(0).getGloss());
        }
    }


    // non causative counterpart of a verb; Example: “burn” is a cause
    //of “ignite”
    private void generateCause(){
        generatedSolutions.put("cause", new ArrayList<>());
        for ( POS pos : available) {
            IndexWord indexWord = analyzer.getIndexWord( pos);
            List <Synset> senses = indexWord.getSenses();
            for (Synset sense :senses) {
                PointerTargetNodeList cause = RelationshipExplorer.getCause( sense);
                for (PointerTargetNode node : cause) {
                    String[] words = StringAnalyzer.getWords(node.toString());
                    for (String word : words)
                        generatedSolutions.get("cause").add(word);
                }
            }
        }
    }


    //An entailment is an implication. For example, looking implies seeing. Buying implies choosing and paying.
    private void generateEntailments(){
        generatedSolutions.put("entailments", new ArrayList<>());
        for ( POS pos : available) {
            IndexWord indexWord = analyzer.getIndexWord( pos);
            List <Synset> senses = indexWord.getSenses();
            for (Synset sense :senses) {
                PointerTargetNodeList entailments = RelationshipExplorer.getEntailment( sense);
                for (PointerTargetNode node : entailments) {
                    String[] words = StringAnalyzer.getWords(node.toString());
                    for (String word : words)
                        generatedSolutions.get("entailments").add(word);
                }
            }
        }
    }


    /*
    Example: if (input -> copper), output -> [brass, bronze, bornite,  peacock ore, chalcocite,  copper glance, chalcopyrite,  copper pyrites, cuprite, malachite]
    Example: if (input -> hydrogen), output -> [water,  H2O]
    Example: if (input -> carbon), [coal, petroleum,  crude oil,  crude,  rock oil,  fossil oil,  oil, limestone]
    */
    private void generateSubstanceHolonyms() {
        generatedSolutions.put( "substance_holonyms", new ArrayList<>());

        for ( POS pos : available) {
            IndexWord indexWord = analyzer.getIndexWord( pos);
            List<Synset> senses = indexWord.getSenses();
            for ( Synset sense : senses) {
                PointerTargetNodeList substanceholonyms = RelationshipExplorer.getSubstanceHolonyms( sense);
                for ( PointerTargetNode node : substanceholonyms) {
                    String [] words = StringAnalyzer.getWords( node.toString());
                    for ( String word : words)
                        generatedSolutions.get( "substance_holonyms").add( word);
                }
            }
        }
    }


    private void generateSubstanceMeronyms () {
        generatedSolutions.put("substance_meronyms", new ArrayList<>());

        for ( POS pos : available) {
            IndexWord indexWord = analyzer.getIndexWord(pos);
            List<Synset> senses = indexWord.getSenses();
            if (senses.size() != 0) {
                PointerTargetNodeList substanceMeronyms = RelationshipExplorer.getSubstanceMeronyms(senses.get(0));
            for (PointerTargetNode node : substanceMeronyms) {
                String[] words = StringAnalyzer.getWords(node.toString());
                for (String word : words)
                    generatedSolutions.get("substance_meronyms").add(word);
            }
        }

        }
    }


    /*
    It just returns part relationship.
    Example: if (input -> arm), then the method returns body because arm is a part of human body.
    We can modify the solution as "It is a part of .....".
     */
    private void generatePartHolonyms()  {
        generatedSolutions.put( "part_holonyms", new ArrayList<>());

        for ( POS pos : available) {
            IndexWord indexWord = analyzer.getIndexWord( pos);
            List<Synset> senses = indexWord.getSenses();
            for ( Synset sense : senses) {
                PointerTargetNodeList partholonyms = RelationshipExplorer.getPartHolonyms( sense);
                for ( PointerTargetNode node : partholonyms) {
                    String [] words = StringAnalyzer.getWords( node.toString());
                    for ( String word : words)
                        generatedSolutions.get( "part_holonyms").add( word);
                }
            }
        }
    }


    private void generatePartMeronyms () {
        generatedSolutions.put("part_meronyms", new ArrayList<>());

        for ( POS pos : available) {
            IndexWord indexWord = analyzer.getIndexWord( pos);
            List<Synset> senses = indexWord.getSenses();
            if ( !senses.isEmpty()) {
                PointerTargetNodeList partMeronyms = RelationshipExplorer.getPartMeronyms(senses.get(0));
                for (PointerTargetNode node : partMeronyms) {
                    String[] words = StringAnalyzer.getWords(node.toString());
                    for (String word : words)
                        generatedSolutions.get("part_meronyms").add(word);
                }
            }

        }
    }


    // POS has to be noun, in order to find the words' attributes.
    private void generateAttributes() {
        generatedSolutions.put( "attributes", new ArrayList<>());

        if ( available.contains( POS.NOUN)) {
            IndexWord indexWord = analyzer.getIndexWord( POS.NOUN);
            List <Synset> senses = indexWord.getSenses();
            for ( Synset sense: senses) {
                PointerTargetNodeList attributes = RelationshipExplorer.getAttributes( sense);
                for ( PointerTargetNode node : attributes) {
                    String [] words = StringAnalyzer.getWords( node.toString());
                    for ( String word : words)
                        generatedSolutions.get( "attributes").add( word);
                }
            }
        }
    }


    //Find what words are related to synset
    private void generateSees(){
        generatedSolutions.put("sees", new ArrayList<>());
        for ( POS pos : available) {
            IndexWord indexWord = analyzer.getIndexWord( pos);
            List <Synset> senses = indexWord.getSenses();
            for (Synset sense :senses) {
                PointerTargetNodeList sees = RelationshipExplorer.getAlsoSee( sense);
                for (PointerTargetNode node : sees) {
                    String[] words = StringAnalyzer.getWords(node.toString());
                    for (String word : words) {
                        if ( !word.toLowerCase().contains( originalSolution.toLowerCase()))
                        generatedSolutions.get("sees").add(word);
                    }
                }
            }
        }
    }


    //Coordinate terms are nouns or verbs that have the same hypernym .
    private void generateCoordinateTerm(){
        generatedSolutions.put("coordinateTerm", new ArrayList<>());
        for ( POS pos : available) {
            IndexWord indexWord = analyzer.getIndexWord( pos);
            List <Synset> senses = indexWord.getSenses();
            for (Synset sense :senses) {
                PointerTargetNodeList coordinateTerm = RelationshipExplorer.getCoordinateTerm( sense);
                for (PointerTargetNode node : coordinateTerm) {
                    String[] words = StringAnalyzer.getWords(node.toString());
                    for (String word : words)
                        generatedSolutions.get("coordinateTerm").add(word);
                }
            }
        }
    }


    private void generateAntonyms() {
        generatedSolutions.put( "antonyms", new ArrayList<>());
        for ( POS pos : available) {
            IndexWord indexWord = analyzer.getIndexWord( pos);
            List <Synset> senses = indexWord.getSenses();
            for (Synset sense :senses) {
                PointerTargetNodeList antonyms = RelationshipExplorer.getAntonyms( sense);
                for (PointerTargetNode node : antonyms) {
                    String[] words = StringAnalyzer.getWords(node.toString());
                    //    System.out.println( node.getWord().toString());
                    for (String word : words) {
                        if ( !word.toLowerCase().contains( originalSolution.toLowerCase()))
                        generatedSolutions.get("antonyms").add(word);
                    }
                }


            }
        }
    }


    private void generateSynonyms() {
        generatedSolutions.put( "synonyms", new ArrayList<>());
        for ( POS pos : available) {
            IndexWord indexWord = analyzer.getIndexWord( pos);
            List <Synset> senses = indexWord.getSenses();
            for (Synset sense :senses) {
                PointerTargetNodeList synonyms = RelationshipExplorer.getSynonym( sense);
                for (PointerTargetNode node : synonyms) {
                    String[] words = StringAnalyzer.getWords(node.toString());
                    for (String word : words)
                        generatedSolutions.get("synonyms").add(word);
                }
            }
        }
    }


    private void generateParents () {
        generatedSolutions.put("parents", new ArrayList<>());

        for ( POS pos : available) {
            IndexWord indexWord = analyzer.getIndexWord( pos);
            List<Synset> senses = indexWord.getSenses();
            for ( Synset sense : senses) {
                PointerTargetNodeList parents = RelationshipExplorer.findParents( sense);
                for ( PointerTargetNode node : parents) {
                    String [] words = StringAnalyzer.getWords( node.toString());
                    for ( String word : words)
                        generatedSolutions.get( "parents").add( word + " " + pos.getLabel());
                }
            }
        }
    }


    private void generateChildren() {
        generatedSolutions.put( "children", new ArrayList<>());

        for ( POS pos : available) {
            IndexWord indexWord = analyzer.getIndexWord( pos);
            List<Synset> senses = indexWord.getSenses();
            for ( Synset sense : senses) {
                PointerTargetNodeList children = RelationshipExplorer.findChildren( sense);
                for (PointerTargetNode node : children) {
                    String[] words = StringAnalyzer.getWords(node.toString());
                    for (String word : words) {
                        if ( !word.toLowerCase().contains( originalSolution.toLowerCase()))
                        generatedSolutions.get("children").add(word);
                    }
                }


            }
        }
    }


    private void generateSenses () {
        generatedSolutions.put( "senses", new ArrayList<>());
        generatedSolutions.put( "description" , new ArrayList<>());
        for ( POS pos: available) {
            IndexWord indexWord = analyzer.getIndexWord( pos);
            List <Synset> senses = indexWord.getSenses();
            for ( Synset sense: senses)  {
                List <Word> words = sense.getWords();
                for ( Word word: words) {
                    generatedSolutions.get("senses").add( word.getLemma());
                    generatedSolutions.get( "description").add( StringAnalyzer.getDescription( word.toString()));
                }
            }
        }
    }


    /*
    It just returns member relationship not all part relationship.
    Example: if (input -> student), output -> [teacher-student relation]. Likewise, if (input -> teacher), output is the same.
    Example: if (input -> lawyer), output -> [lawyer-client relation,  attorney-client relation]
     */
    private void generateMemberHolonyms() {
        generatedSolutions.put( "memberholonyms", new ArrayList<>());

        for ( POS pos : available) {
            IndexWord indexWord = analyzer.getIndexWord( pos);
            List<Synset> senses = indexWord.getSenses();
            for ( Synset sense : senses) {
                PointerTargetNodeList memberholonyms = RelationshipExplorer.getMemberHolonyms( sense);
                for ( PointerTargetNode node : memberholonyms) {
                    String [] words = StringAnalyzer.getWords( node.toString());
                    for ( String word : words)
                        generatedSolutions.get( "memberholonyms").add( word);
                }
            }
        }
    }


    private void generateMemberMeronyms () {
        generatedSolutions.put("member meronyms", new ArrayList<>());

        for ( POS pos : available) {
            IndexWord indexWord = analyzer.getIndexWord( pos);
            List<Synset> senses = indexWord.getSenses();
            for ( Synset sense : senses) {
                PointerTargetNodeList memberMeronyms = RelationshipExplorer.getMemberMeronyms( sense);
                for ( PointerTargetNode node : memberMeronyms) {
                    String [] words = StringAnalyzer.getWords( node.toString());
                    for ( String word : words)
                        generatedSolutions.get( "member meronyms").add( word);
                }
            }
        }
    }


    public HashMap<String, ArrayList<String>> getGeneratedSolutions() {
        return generatedSolutions;
    }
}
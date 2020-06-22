package com.company;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.PointerUtils;
import net.sf.extjwnl.data.Synset;
import net.sf.extjwnl.data.list.PointerTargetNodeList;

/*
    Found relationships are :
    1- Antonyms
    2- Attributes
    3- Children
    4- Parents
    5- Synonyms
 */
public class RelationshipExplorer {
    private RelationshipExplorer() {}
    public static PointerTargetNodeList getAntonyms(Synset synset) {
        try {
            return PointerUtils.getAntonyms( synset);
        }catch (JWNLException e){
            return null;
        }
    }
    public static PointerTargetNodeList getAttributes (Synset synset) {
        try {
            return PointerUtils.getAttributes( synset);
        } catch (JWNLException e) {
            return null;
        }
    }
    public static PointerTargetNodeList findChildren(Synset synset) {
        try {
            return PointerUtils.getDirectHyponyms( synset);
        }catch (JWNLException e) {
            return  null;
        }

    }
    public static PointerTargetNodeList findParents(Synset synset) {
        try {
            return PointerUtils.getDirectHypernyms(synset);
        }
        catch ( JWNLException e) {
            return null;
        }
    }
    public static PointerTargetNodeList getSynonym(Synset synset) {
        try {
            return PointerUtils.getSynonyms(synset);
        } catch (JWNLException e) {
            return null;
        }

    }

    // I added:


    public static PointerTargetNodeList getMemberHolonyms(Synset synset) {
        try {
            return PointerUtils.getMemberHolonyms(synset);
        } catch (JWNLException e) {
            return null;
        }
    }

    public static PointerTargetNodeList getPartHolonyms(Synset synset) {
        try {
            return PointerUtils.getPartHolonyms(synset);
        } catch (JWNLException e) {
            return null;
        }
    }


    public static PointerTargetNodeList getSubstanceHolonyms(Synset synset) {
        try {
            return PointerUtils.getSubstanceHolonyms(synset);
        } catch (JWNLException e) {
            return null;
        }
    }

    public static PointerTargetNodeList getAlsoSee(Synset synset) {
        try {
            return PointerUtils.getAlsoSees(synset);
        } catch (JWNLException e) {
            return null;
        }

    }
    public static PointerTargetNodeList getCause(Synset synset) {
        try {
            return PointerUtils.getCauses(synset);
        } catch (JWNLException e) {
            return null;
        }

    }
    public static PointerTargetNodeList getCoordinateTerm(Synset synset) {
        try {
            return PointerUtils.getCoordinateTerms(synset);
        } catch (JWNLException e) {
            return null;
        }

    }
    public static PointerTargetNodeList getEntailment(Synset synset) {
        try {
            return PointerUtils.getEntailments(synset);
        } catch (JWNLException e) {
            return null;
        }

    }
    public static PointerTargetNodeList getParticiple(Synset synset) {
        try {
            return PointerUtils.getParticipleOf(synset);
        } catch (JWNLException e) {
            return null;
        }

    }

    /*
        A car is a member of a traffic jam.
        Car --> member meronym of traffic jam.
        Bir bütünün üyeleri
         */
    public static PointerTargetNodeList getMemberMeronyms (Synset synset) {
        try {
            return PointerUtils.getMemberMeronyms( synset);
        } catch (JWNLException e) {
            return null;
        }
    }
    /*
    A tire is a part of a car
    tire --> part meronym of car
    Bir bütünün parçaları
     */
    public static PointerTargetNodeList getPartMeronyms (Synset synset) {
        try {
            return PointerUtils.getPartMeronyms( synset);
        } catch (JWNLException e) {
            return null;
        }
    }
    /*
    A wheel is made from rubber
    rubber --> substance meronym of wheel
    Neyden yapıldığını döndürüyor
     */
    public static PointerTargetNodeList getSubstanceMeronyms (Synset synset) {
        try {
            return PointerUtils.getSubstanceMeronyms( synset);
        } catch (JWNLException e) {
            return null;
        }
    }

}

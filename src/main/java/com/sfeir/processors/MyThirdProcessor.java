package com.sfeir.processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.*;

/**
 * Created by bcornu on 2/2/17.
 */
public class MyThirdProcessor extends AbstractProcessor<CtIf> {

    @SuppressWarnings("unchecked")
    public void process(CtIf element) {
        if (element.getParent(CtIf.class) == null) {//pas dans un CtIf
            if (isStringEqualsCompare(element)){//condition de la forme String#equals
                if(hasSameElseChildren(element)){//idem pour le else
                    System.out.println("CANDIDAT !!!");
                }
            }
        }
        System.out.println("nope");
    }

    private boolean hasSameElseChildren(CtIf element) {
        CtBlock elseBlock = ((CtBlock)element.getElseStatement());
        if(elseBlock==null){//pas de else donc OK
            return true;
        }
        if(elseBlock.getStatements().size() == 1 && elseBlock.getLastStatement() instanceof CtIf){ // un seul element dans le else qui est un CtIf
            CtIf childrenIf = (CtIf)((CtBlock)element.getElseStatement()).getLastStatement();
            if (isStringEqualsCompare(childrenIf)){ // la condition du ctIf est un string#equals
                return hasSameElseChildren(childrenIf);//idem pour le else
            }
        }
        return false;
    }

    private boolean isStringEqualsCompare(CtIf element){
        if (element.getCondition() instanceof CtInvocation) {
            CtInvocation op = (CtInvocation) element.getCondition();
            CtExpression target = op.getTarget();
            if(target.getType().getActualClass().equals(String.class)){
                if (op.getExecutable().getSimpleName().equals("equals")) {
                    return true;
                }
            }
        }
        return false;
    }

}
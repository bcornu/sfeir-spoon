package com.sfeir.processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bcornu on 2/2/17.
 */
public class MyThirdProcessor extends AbstractProcessor<CtIf> {

    @SuppressWarnings("unchecked")
    public void process(CtIf element) {
        if (element.getParent(CtIf.class) == null) {//pas dans un CtIf
            if (isStringEqualsCompare(element)){//condition de la forme String#equals
                if(hasSameElseChildren(element)){//idem pour le else

                    Map<CtExpression, CtBlock> cases = getCases(element);

                    List<CtCase> ctCases = new ArrayList<>();
                    for (CtExpression expr : cases.keySet()) {
                        CtCase ctCase = getFactory().Core().createCase();
                        ctCase.setCaseExpression(expr);
                        ctCase.setStatements(cases.get(expr).getStatements());
                        ctCases.add(ctCase);
                    }

                    CtSwitch newSwitch = getFactory().Core().createSwitch();
                    newSwitch.setCases(ctCases);
                    newSwitch.setSelector(((CtInvocation)element.getCondition()).getTarget());

                    element.replace(newSwitch);
                }
            }
        }
    }

    private Map<CtExpression, CtBlock> getCases(CtIf element) {
        CtBlock elseBlock = element.getElseStatement();
        if(elseBlock==null){
            return new HashMap<>();
        }
        CtIf childrenIf = (CtIf)elseBlock.getLastStatement();
        Map<CtExpression, CtBlock> res = getCases(childrenIf);
        CtInvocation op = (CtInvocation) element.getCondition();
        CtExpression target = (CtExpression) op.getArguments().get(0);
        res.put(target, (CtBlock) element.getThenStatement());
        return res;
    }

    private boolean hasSameElseChildren(CtIf element) {
        CtBlock elseBlock = element.getElseStatement();
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
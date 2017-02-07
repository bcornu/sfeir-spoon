package com.sfeir.processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.*;
import spoon.reflect.reference.CtExecutableReference;

/**
 * Created by bcornu on 2/2/17.
 */
public class MySecondProcessor extends AbstractProcessor<CtIf> {

    public void process(CtIf element) {
        if (element.getCondition() instanceof CtBinaryOperator) {
            CtBinaryOperator op = (CtBinaryOperator) element.getCondition();
            if (op.getKind().equals(BinaryOperatorKind.EQ)) {
                CtExpression left = op.getLeftHandOperand();
                CtExpression right = op.getRightHandOperand();
                if (left.getType().getActualClass().equals(String.class)
                        && right.getType().getActualClass().equals(String.class)) {
                    //System.out.print("String compare with == @");
                    //System.out.println(element.getPosition());
                    //System.out.println(element.getCondition());
                    CtExecutableReference<Boolean> ctr = getFactory().Core().createExecutableReference();
                    ctr.setSimpleName("equals");

                    CtInvocation<Boolean> cond = getFactory().Core().createInvocation();
                    cond.setTarget(left);
                    cond.setExecutable(ctr);
                    cond.addArgument(right);
                    element.setCondition(cond);
                }
            }
        }
    }

}
package com.sfeir.processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.*;

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
                    System.out.print("String compare with == @");
                    System.out.println(element.getPosition());
                    System.out.println(element.getCondition());
                }
            }
        }
    }

}
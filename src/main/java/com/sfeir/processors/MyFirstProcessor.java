package com.sfeir.processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.*;

/**
 * Created by bcornu on 2/2/17.
 */
public class MyFirstProcessor extends AbstractProcessor<CtCatch> {

    public void process(CtCatch element) {
        if (element.getBody().getStatements().size() == 0) {
            System.out.print("empty catch: ");
            System.out.println(element.getPosition());
            System.out.println(element);
        }
    }

}
package org.blab.lambda.demo;

import org.blab.river.Event;

import java.util.List;
import java.util.Properties;

public class Minimizer implements Lambda<Event, List<Event>> {
    @Override
    public List<Event> apply(Event event) {
        return null;
    }

    @Override
    public void refresh(Configuration p) {

    }
}

package com.amairovi.goetz_concurrency_in_practice.chapter_10;

import com.amairovi.goetz_concurrency_in_practice.GuardedBy;

import java.util.HashSet;
import java.util.Set;

public class DeadlockBetweenCooperating {

    static class Point {}

    static class Taxi {

        @GuardedBy("this") private Point location, destination;
        private final Dispatcher dispatcher;

        public Taxi(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
        }

        public synchronized Point getLocation() {
            return location;
        }

        public synchronized void setLocation(Point location) {
            this.location = location;
            if (location.equals(destination)) {
                dispatcher.notifyAvailable(this);
            }
        }

    }

    static class Dispatcher {

        @GuardedBy("this") private final Set<Taxi> taxis;
        @GuardedBy("this") private final Set<Taxi> availableTaxis;

        Dispatcher() {
            taxis = new HashSet<>();
            availableTaxis = new HashSet<>();
        }

        public synchronized void notifyAvailable(Taxi taxi) {
            availableTaxis.add(taxi);
        }

        public synchronized Image getImage() {
            Image image = new Image();
            for (Taxi t : taxis) {
                image.drawMarker(t.getLocation());
            }
            return image;
        }

    }

    private static class Image {

        public void drawMarker(Point location) {

        }

    }

}

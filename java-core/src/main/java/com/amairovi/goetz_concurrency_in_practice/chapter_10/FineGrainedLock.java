package com.amairovi.goetz_concurrency_in_practice.chapter_10;

import com.amairovi.goetz_concurrency_in_practice.GuardedBy;

import java.util.HashSet;
import java.util.Set;

public class FineGrainedLock {

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

        public void setLocation(Point location) {
            boolean reachedDestination;
            synchronized (this) {
                this.location = location;
                reachedDestination = location.equals(destination);
            }
            if (reachedDestination) {
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

        public Image getImage() {
            Set<Taxi> copy;
            synchronized (this){
                copy = new HashSet<>(taxis);
            }

            Image image = new Image();
            for (Taxi t : copy) {
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

package model;

import org.w3c.dom.Node;

import javax.swing.text.Element;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class Train<T extends Wagon> implements Iterable<Wagon> {
    private Locomotive engine;
    private T firstWagon;
    private String destination;
    private String origin;
    private int numberOfWagons;


    public Train(Locomotive engine, String origin, String destination) {
        this.engine = engine;
        this.destination = destination;
        this.origin = origin;
    }

    public Wagon getFirstWagon() {
        return firstWagon;
    }

    public void setFirstWagon(T firstWagon) {
        this.firstWagon = firstWagon;
    }

    public void resetNumberOfWagons() {
       /*  when wagons are hooked to or detached from a train,
         the number of wagons of the train should be reset
         this method does the calculation */
        Wagon w;
        int counter = 1;
        if (firstWagon == null) {
            numberOfWagons = 0;
        } else {
            w = firstWagon;
            while (w.getNextWagon() != null) {
                w = w.getNextWagon();
                counter++;
            }
            numberOfWagons = counter;
        }
    }

    public int getNumberOfWagons() {
        if (firstWagon == null) {
            return 0;
        } else {
            return numberOfWagons = firstWagon.getNumberOfWagonsAttached() + 1;
        }
    }


    /* three helper methods that are usefull in other methods */

    public boolean hasNoWagons() {
        return (firstWagon == null);
    }

    public boolean isPassengerTrain() {
        return firstWagon instanceof PassengerWagon;
    }

    public boolean isFreightTrain() {
        return firstWagon instanceof FreightWagon;
    }

    public int getPositionOfWagon(int wagonId) {
        // find a wagon on a train by id, return the position (first wagon had position 1)
        // if not found, than return -1
        int position = 1;
        Wagon w = firstWagon;
        if (wagonId == w.getWagonId()) {
            return position;
        }
        while (w.getNextWagon() != null) {
            w = w.getNextWagon();
            position++;
            if (wagonId == w.getWagonId()) {
                return position;
            }

        }

        return -1;
    }


    public Wagon getWagonOnPosition(int position) throws IndexOutOfBoundsException {
        /* find the wagon on a given position on the train
         position of wagons start at 1 (firstWagon of train)
         use exceptions to handle a position that does not exist */


        int counter = 1;
        Wagon w = firstWagon;
        if (counter == position) {
            return w;
        }
        try {
            while (w.getNextWagon() != null) {
                counter++;
                w = w.getNextWagon();
                if (counter == position) {
                    return w;
                }
            }
        } catch (IndexOutOfBoundsException er) {
            if (counter != position) {
                System.out.println(er);
                throw new IndexOutOfBoundsException();
            }
        }
        return null;
    }

    public int getNumberOfSeats() {
         /* give the total number of seats on a passenger train
         for freight trains the result should be 0 */

        if (isPassengerTrain()) {
            int totalSeats = 0;
            for (Wagon w : this) {
                PassengerWagon passengerWagon = (PassengerWagon) w;
                totalSeats += passengerWagon.getNumberOfSeats();
            }
            return totalSeats;
        }
        return 0;
    }

    public int getTotalMaxWeight() {
        /* give the total maximum weight of a freight train
         for passenger trains the result should be 0 */


        if (isFreightTrain()) {
            int totalweight = 0;
            for (Wagon w : this) {
                FreightWagon freightWagon = (FreightWagon) w;
                totalweight += freightWagon.getMaxWeight();
            }
            return totalweight;
        }
        return 0;
    }

    public Locomotive getEngine() {
        return engine;


    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(engine.toString());
        Wagon next = this.getFirstWagon();
        while (next != null) {
            result.append(next.toString());
            next = next.getNextWagon();
        }
        result.append(String.format(" with %d wagons and %d seats from %s to %s", numberOfWagons, getNumberOfSeats(), origin, destination));
        return result.toString();
    }

    @Override
    public Iterator<Wagon> iterator() {
        return new TrainIterator();
    }


    class TrainIterator implements Iterator<Wagon> {
        T current = null;

        @Override
        public boolean hasNext() {
            if (current == null && firstWagon != null) {
                return true;
            } else if (current != null) {
                return current.getNextWagon() != null;
            }
            return false;
        }

        @Override
        public T next() {
            if (current == null && firstWagon != null) {
                current = firstWagon;
                return current;
            } else if (current != null) {
                current = (T) current.getNextWagon();
                return current;
            }
            return null;
        }
    }
}


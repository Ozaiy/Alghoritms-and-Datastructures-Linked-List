package model;

public class Shunter {


    /* four helper methods than are used in other methods in this class to do checks */
    private static boolean isSuitableWagon(Train train, Wagon wagon) {
        // trains can only exist of passenger wagons or of freight wagons
        return (train.getFirstWagon() instanceof PassengerWagon && wagon instanceof PassengerWagon) ||
                (train.getFirstWagon() instanceof FreightWagon && wagon instanceof FreightWagon);
    }

    private static boolean isSuitableWagon(Wagon one, Wagon two) {
        // passenger wagons can only be hooked onto passenger wagons
        return one instanceof PassengerWagon && two instanceof PassengerWagon;
    }

    private static boolean hasPlaceForWagons(Train train, Wagon wagon) {
        // the engine of a train has a maximum capacity, this method checks for a row of wagons
        return train.getEngine().getMaxWagons() > wagon.getNumberOfWagonsAttached() + train.getNumberOfWagons();
    }

    private static boolean hasPlaceForOneWagon(Train train, Wagon wagon) {
        // the engine of a train has a maximum capacity, this method checks for one wagon
        int sum = train.getNumberOfWagons() + 1 ;
        return train.getEngine().getMaxWagons() >= sum;
    }

    public static boolean hookWagonOnTrainRear(Train train, Wagon wagon) {
         /* check if Locomotive can pull new number of Wagons
         check if wagon is correct kind of wagon for train
         find the last wagon of the train
         hook the wagon on the last wagon (see Wagon class)
         adjust number of Wagons of Train */

        //check if a train doesnt have a first train
        if (train.getFirstWagon() == null) {
            train.setFirstWagon(wagon);
        } else {
            if (isSuitableWagon(train, wagon) && hasPlaceForWagons(train, wagon)) {
                // set the wagon to be the next wagon of the Last wagon on this train
                train.getFirstWagon().getLastWagonAttached().setNextWagon(wagon);
                //vise versa set the previous wagon of the inputted wagon to be the last getted wagon
                // the Last wagon gives back the inputted wagon so you have to get the previousWagon
                wagon.setPreviousWagon(train.getFirstWagon().getLastWagonAttached().getPreviousWagon());
                train.resetNumberOfWagons();
                return true;
            } else {
                System.out.println("trying to hookOn incompatible trains. Or have too many trains attached");
                return false;
            }
        }

        return true;

    }

    public static boolean hookWagonOnTrainFront(Train train, Wagon wagon) {
        /* check if Locomotive can pull new number of Wagons
         check if wagon is correct kind of wagon for train
         if Train has no wagons hookOn to Locomotive
         if Train has wagons hookOn to Locomotive and hook firstWagon of Train to lastWagon attached to the wagon
         adjust number of Wagons of Train */

        if (train.getFirstWagon() == null) {
            train.setFirstWagon(wagon);
            train.resetNumberOfWagons();
        } else if (train.getFirstWagon() != null) {
            if (isSuitableWagon(train, wagon) && hasPlaceForOneWagon(train, wagon)) {
                wagon.setNextWagon(train.getFirstWagon());
                train.setFirstWagon(wagon);
                train.resetNumberOfWagons();
                return true;
            } else {
                System.out.println("trying to hook on wrong type, Or max wagons reached");
                return false;
            }
        }
        return false;
    }

    public static boolean hookWagonOnWagon(Wagon first, Wagon second) {
        /* check if wagons are of the same kind (suitable)
         * if so make second wagon next wagon of first */
        if (isSuitableWagon(first, second)) {
            first.setNextWagon(second);
            second.setPreviousWagon(first);
        } else {
            System.out.println("trying to hook on wrong type of train!");
            return false;
        }

        return false;
    }


    public static boolean detachAllFromTrain(Train train, Wagon wagon) {
        /* check if wagon is on the train
         detach the wagon from its previousWagon with all its successor
         recalculate the number of wagons of the train */
        Wagon previous = wagon.getPreviousWagon();
        if (train.getPositionOfWagon(wagon.getWagonId()) == -1) {
            System.out.println("Wagon not found");
            return false;
        } else {
            if (train.getFirstWagon() == wagon) {
                train.setFirstWagon(null);
                train.resetNumberOfWagons();
            } else {
                previous.setNextWagon(null);
                train.resetNumberOfWagons();
            }
        }

        return false;

    }

    public static boolean detachOneWagon(Train train, Wagon wagon) {
        /* check if wagon is on the train
         detach the wagon from its previousWagon and hook the nextWagon to the previousWagon
         so, in fact remove the one wagon from the train
        */

        Wagon previous = wagon.getPreviousWagon();
        Wagon next = wagon.getNextWagon();
        if (train.getPositionOfWagon(wagon.getWagonId()) == -1) {
            System.out.println("Wagon not found");
            return false;
        } else {
            if (train.getFirstWagon() == wagon) {
                train.setFirstWagon(next);
                train.resetNumberOfWagons();
            } else {
                previous.setNextWagon(next);
                wagon.setNextWagon(null);
                wagon.setPreviousWagon(null);
                train.resetNumberOfWagons();
            }
            return true;
        }
    }

    public static boolean moveAllFromTrain(Train from, Train to, Wagon wagon) {
        /* check if wagon is on train from
         check if wagon is correct for train and if engine can handle new wagons
         detach Wagon and all successors from train from and hook at the rear of train to
         remember to adjust number of wagons of trains */

        Wagon previous = wagon.getPreviousWagon();
        Wagon next = wagon.getNextWagon();
        if (from.getPositionOfWagon(wagon.getWagonId()) == -1) {
            System.out.println("Wagon not found");
            return false;
        } else {
            if (isSuitableWagon(to, wagon) && hasPlaceForWagons(to, wagon)) {
                previous.setNextWagon(next);
                hookWagonOnTrainRear(to, wagon);
                previous.setNextWagon(null);

                to.resetNumberOfWagons();
                from.resetNumberOfWagons();
                return true;
            } else {
                System.out.println("trying to hook on wrong type, Or max wagons reached");
                return false;
            }
        }
    }

    public static boolean moveOneWagon(Train from, Train to, Wagon wagon) {
        // detach only one wagon from train from and hook on rear of train to
        // do necessary checks and adjustments to trains and wagon


        if (from.getPositionOfWagon(wagon.getWagonId()) == -1) {
            System.out.println("Wagon not found");
            return false;
        } else {
            detachOneWagon(from, wagon);
            if (to.getFirstWagon() == null) {
                to.setFirstWagon(wagon);
                to.resetNumberOfWagons();
            } else {
                if (isSuitableWagon(to, wagon) && hasPlaceForOneWagon(to, wagon)) {
                    detachOneWagon(from, wagon);
                    wagon.setNextWagon(null);
                    to.getFirstWagon().getLastWagonAttached().setNextWagon(wagon);
                    to.resetNumberOfWagons();
                    return true;
                } else {
                    System.out.println("trying to hook on wrong type, Or max wagons reached");
                    return false;
                }
            }
        }
        return false;
    }
}

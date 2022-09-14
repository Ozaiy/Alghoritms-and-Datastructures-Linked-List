package controller;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class TrainLauncher {



    public static void main(String[] args) {
        PassengerWagon passengerWagon = new PassengerWagon(2, 5);
        PassengerWagon passengerWagon1 = new PassengerWagon(4,6);
        PassengerWagon passengerWagon2 = new PassengerWagon(5,1);

        Locomotive sean = new Locomotive(5277, 8);
        Locomotive ai = new Locomotive(4124,4);
        Train secondTrain = new Train(sean, "Cape Town", "Joburg" );
        Train freighTrain = new Train(ai,"ads", "vige");

//TODO DONT TOUCH BELOW HERE

        List<Wagon> freighList = new ArrayList<>();
        freighList.add(new FreightWagon(3, 100));
        freighList.add(new FreightWagon( 12,300));
        freighList.add(new FreightWagon( 23,40));
        for (Wagon w : freighList){
            Shunter.hookWagonOnTrainRear(freighTrain,w);
        }

        List<Wagon> wagonList = new ArrayList<>();
        wagonList.add(new PassengerWagon(3, 100));
        wagonList.add(new PassengerWagon(24, 100));
        wagonList.add(new PassengerWagon(17, 140));
        wagonList.add(new PassengerWagon(32, 150));
        wagonList.add(new PassengerWagon(38, 140));
        Locomotive thomas = new Locomotive(2453, 7);
        Train firstTrain  = new Train(thomas, "Amsterdam", "Haarlem");
        for (Wagon w : wagonList) {
            Shunter.hookWagonOnTrainRear(firstTrain, w);
        }


        System.out.println("TESTING THE WAGON CLASS");
        System.out.println("----------------------");
        System.out.println("Testing the getNumberOf wagons");
        Shunter.hookWagonOnWagon(passengerWagon, passengerWagon1);
        Shunter.hookWagonOnWagon(passengerWagon1, passengerWagon2);
        System.out.println(passengerWagon.getNumberOfWagonsAttached() + " must equal 2");
        System.out.println();

        System.out.println("----------------------");
        System.out.println("Testing the getLastWagon Must be Wagon 5");
        System.out.println(passengerWagon.getLastWagonAttached());
        System.out.println();


        System.out.println("TESTING THE TRAIN CLASS");
        System.out.println("----------------------");
        System.out.println("The first train without Shunter class interference");
        System.out.println(firstTrain + "\n");

        System.out.println("Position of WagonID 21 is: " + firstTrain.getPositionOfWagon(24));
        System.out.println("Wagon on position 5 is: " + firstTrain.getWagonOnPosition(5));
        System.out.println();

        System.out.println("Freigh train getTotalMaxWeight");
        System.out.println(freighTrain.getTotalMaxWeight());
        System.out.println();

        System.out.println("TESTING THE SHUNTER CLASS");
        System.out.println("----------------------");
        System.out.println("Hooking PassengerWagon 21 to front");
        Shunter.hookWagonOnTrainFront(firstTrain, new PassengerWagon(21, 140));
        System.out.println(firstTrain + "\n");

        System.out.println("Moving one wagon to another train");
        Shunter.moveOneWagon(firstTrain, secondTrain, wagonList.get(3));
        System.out.println(firstTrain);
        System.out.println(secondTrain);

        System.out.println();
        System.out.println("----------------------");
        System.out.println("Moving an row of wagons to another train");
        Shunter.moveAllFromTrain(firstTrain, secondTrain, wagonList.get(2));
        System.out.println(firstTrain);
        System.out.println(secondTrain);

        System.out.println();
        System.out.println("----------------------");
        System.out.println("detaching one wagon from Loc 2453 Wagon 24");
        Shunter.detachOneWagon(firstTrain,wagonList.get(1));
        System.out.println(firstTrain);

        System.out.println();
        System.out.println("----------------------");
        System.out.println("detaching all wagons from Loc 2453");
        Shunter.detachAllFromTrain(firstTrain, firstTrain.getWagonOnPosition(1));
        System.out.println(firstTrain);

        System.out.println();
        System.out.println("----------------------");
        System.out.println("hooking wagon to wagon");
        Shunter.hookWagonOnWagon(wagonList.get(0),wagonList.get(1));
        System.out.println(wagonList.get(0) + " "  + wagonList.get(0).getNextWagon());
        System.out.println();

        System.out.println("TRYING TO BREAK SHUNTER CLASS");
        System.out.println("----------------------");
        System.out.println("Testing the Max wagons in this case you can't have more than 7");
        Wagon w1 = new PassengerWagon(11, 100);
        Shunter.hookWagonOnWagon(w1, new PassengerWagon(43, 140));
        Shunter.hookWagonOnTrainRear(firstTrain, w1);
        System.out.println(firstTrain);

        System.out.println();
        System.out.println("----------------------");
        System.out.println("Hooking wrong wagon on another wagon");
        FreightWagon freightWagon = new FreightWagon(12,200);
        Shunter.hookWagonOnWagon(wagonList.get(1), freightWagon);

        System.out.println();
        System.out.println("----------------------");
        System.out.println("Trying to hook on wrong type of wagon to trainRear");
        firstTrain.setFirstWagon(wagonList.get(0));
        Shunter.hookWagonOnTrainRear(firstTrain, freightWagon);

        System.out.println();
        System.out.println("----------------------");
        System.out.println("Trying to hook on wrong type of wagon to trainFront");
        Shunter.hookWagonOnTrainFront(firstTrain, freightWagon);


    }
}

import model.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;

public class TrainsTest {

    private ArrayList<PassengerWagon> pwList;
    private Train firstPassengerTrain;
    private Train secondPassengerTrain;
    private Train firstFreightTrain;
    private Train secondFreightTrain;

    @BeforeEach
    private void makeListOfPassengerWagons() {
        pwList = new ArrayList<>();
        pwList.add(new PassengerWagon(3, 100));
        pwList.add(new PassengerWagon(24, 100));
        pwList.add(new PassengerWagon(17, 140));
        pwList.add(new PassengerWagon(32, 150));
        pwList.add(new PassengerWagon(38, 140));
        pwList.add(new PassengerWagon(11, 100));
    }

    private void makeTrains() {
        makeListOfPassengerWagons();
        Locomotive thomas = new Locomotive(2453, 7);
        Locomotive gordon = new Locomotive(5277, 8);
        Locomotive emily = new Locomotive(4383, 11);
        Locomotive rebecca = new Locomotive(2275, 4);

        firstPassengerTrain = new Train(thomas, "Amsterdam", "Haarlem");
        for (Wagon w : pwList) {
            Shunter.hookWagonOnTrainRear(firstPassengerTrain, w);
        }
        secondPassengerTrain = new Train(gordon, "Joburg", "Cape Town");
    }

    @Test
    public void getNumberOfWagonsAttachedn() {
        makeTrains();
        Shunter.hookWagonOnWagon(pwList.get(0), pwList.get(1));
        Shunter.hookWagonOnWagon(pwList.get(1), pwList.get(2));
        Shunter.hookWagonOnWagon(pwList.get(2), pwList.get(3));
        System.out.println(pwList.get(0).getNumberOfWagonsAttached());
        assertEquals(5, pwList.get(0).getNumberOfWagonsAttached());
    }

    @Test
    public void checkgetLastWagonAttached() {
        Shunter.hookWagonOnWagon(pwList.get(0), pwList.get(1));
        Shunter.hookWagonOnWagon(pwList.get(1), pwList.get(2));
        assertEquals(2, pwList.get(0).getNumberOfWagonsAttached());
    }

    @Test
    public void detachOneWagon() {
        makeTrains();
        Shunter.detachOneWagon(firstPassengerTrain, pwList.get(0));
        assertEquals(5, firstPassengerTrain.getNumberOfWagons());
    }

    @Test
    public void detachAllwagons() {
        makeTrains();
        Shunter.detachAllFromTrain(firstPassengerTrain, pwList.get(0));
        assertEquals(0, firstPassengerTrain.getNumberOfWagons(), "should be 2");
    }

    @Test
    public void checkNumberOfWagonsOnTrain() {
        makeTrains();
        assertEquals(6, firstPassengerTrain.getNumberOfWagons(), "Train should have 6 wagons");
        System.out.println(firstPassengerTrain);
    }

    @Test
    public void checkNumberOfSeatsOnTrain() {
        makeTrains();
        assertEquals(730, firstPassengerTrain.getNumberOfSeats());
        System.out.println(firstPassengerTrain);
    }

    @Test
    public void checkPositionOfWagons() {
        makeTrains();
        int position = 1;
        for (PassengerWagon pw : pwList) {
            assertEquals(position, firstPassengerTrain.getPositionOfWagon(pw.getWagonId()));
            position++;
        }
    }

    @Test
    public void checkgetWagonOnPosition() {
        makeTrains();
        assertEquals(pwList.get(0), firstPassengerTrain.getWagonOnPosition(1));
    }

    @Test
    public void checkResetNumberOfWagons() {
        makeTrains();
        Shunter.detachOneWagon(firstPassengerTrain, pwList.get(1));
        assertEquals(5, firstPassengerTrain.getNumberOfWagons());
    }

    @Test
    public void checkHookWrongOneWagonTrainFront() {
        makeTrains();
        Shunter.hookWagonOnTrainFront(firstPassengerTrain, new FreightWagon(2, 4));
        System.out.println(firstPassengerTrain);
        assertEquals(6, firstPassengerTrain.getNumberOfWagons(), "Train should have 7 wagons");
        assertEquals(1, firstPassengerTrain.getPositionOfWagon(3));
    }

    @Test
    public void checkHookOneWagonTrainFrontExceedLimit(){
        makeTrains();
        Shunter.hookWagonOnTrainFront(firstPassengerTrain, new PassengerWagon(23,2));
        assertFalse(Shunter.hookWagonOnTrainFront(firstPassengerTrain, new PassengerWagon(21, 44)));
        System.out.println(firstPassengerTrain);
    }


    @Test
    public void checkHookOneWagonOnTrainFront() {
        makeTrains();
        Shunter.hookWagonOnTrainFront(firstPassengerTrain, new PassengerWagon(21, 140));
        System.out.println(firstPassengerTrain);
        assertEquals(7, firstPassengerTrain.getNumberOfWagons(), "Train should have 7 wagons");
        assertEquals(1, firstPassengerTrain.getPositionOfWagon(21));
    }

    @Test
    public void checkHookRowWagonsOnTrainRearFalse() {
        makeTrains();
        Wagon w1 = new PassengerWagon(11, 100);
        Shunter.hookWagonOnWagon(w1, new PassengerWagon(43, 140));
        Shunter.hookWagonOnTrainRear(firstPassengerTrain, w1);
        assertEquals(6, firstPassengerTrain.getNumberOfWagons(), "Train should have still have 6 wagons, capacity reached");
        assertEquals(-1, firstPassengerTrain.getPositionOfWagon(43));
    }

    @Test
    public void checkMoveOneWagon() {
        makeTrains();
        Shunter.moveOneWagon(firstPassengerTrain, secondPassengerTrain, pwList.get(3));
        assertEquals(5, firstPassengerTrain.getNumberOfWagons(), "Train should have 5 wagons");
        assertEquals(-1, firstPassengerTrain.getPositionOfWagon(32));
        assertEquals(1, secondPassengerTrain.getNumberOfWagons(), "Train should have 1 wagon");
        assertEquals(1, secondPassengerTrain.getPositionOfWagon(32));
    }

    @Test
    public void checkMoveRowOfWagons() {
        makeTrains();
        Wagon w1 = new PassengerWagon(11, 100);
        Shunter.hookWagonOnWagon(w1, new PassengerWagon(43, 140));
        Shunter.hookWagonOnTrainRear(secondPassengerTrain, w1);
        Shunter.moveAllFromTrain(firstPassengerTrain, secondPassengerTrain, pwList.get(2));
        assertEquals(2, firstPassengerTrain.getNumberOfWagons(), "Train should have 2 wagons");
        assertEquals(2, firstPassengerTrain.getPositionOfWagon(24));
        assertEquals(6, secondPassengerTrain.getNumberOfWagons(), "Train should have 6 wagons");
        assertEquals(4, secondPassengerTrain.getPositionOfWagon(32));
    }

    @Test
    public void checMoveToWrongTrain() {
        makeTrains();
        FreightWagon freightWagon = new FreightWagon(3, 4);
        Locomotive loco = new Locomotive(3, 5);
        firstFreightTrain = new Train(loco, "Ams", "Ds");
        firstFreightTrain.setFirstWagon(freightWagon);
        System.out.println(firstPassengerTrain);
        assertFalse(Shunter.moveOneWagon(firstPassengerTrain, firstFreightTrain, pwList.get(2)));
    }

    @Test
    public void checkMoveRowOfWagonsToWrongTrain() {
        makeTrains();
        FreightWagon freightWagon = new FreightWagon(3, 4);
        Locomotive loco = new Locomotive(3, 5);
        firstFreightTrain = new Train(loco, "Ams", "Ds");
        firstFreightTrain.setFirstWagon(freightWagon);
        System.out.println(firstPassengerTrain);
        assertFalse(Shunter.moveAllFromTrain(firstPassengerTrain, firstFreightTrain, pwList.get(2)));
    }

    @Test
    public void checkMoveRowOfWagonsExceedLimit() {
        makeTrains();
        Wagon w1 = new PassengerWagon(11, 100);
        Shunter.hookWagonOnWagon(w1, new PassengerWagon(43, 140));
        secondPassengerTrain.setFirstWagon(w1);
        assertEquals(false, Shunter.moveAllFromTrain(secondPassengerTrain, firstPassengerTrain, w1));
        System.out.println(firstPassengerTrain);

    }

    @Test
    public void checMoveOneWagonexceedLimit() {
        makeTrains();
        Wagon w1 = new PassengerWagon(234, 100);
        Shunter.hookWagonOnWagon(w1, new PassengerWagon(111, 140));
        secondPassengerTrain.setFirstWagon(w1);
        Shunter.hookWagonOnTrainRear(firstPassengerTrain, new PassengerWagon(32,4));
        assertEquals(false,Shunter.moveOneWagon(secondPassengerTrain, firstPassengerTrain, w1));
        System.out.println(secondPassengerTrain);
        System.out.println(firstPassengerTrain);
    }



}



package model;

public abstract class Wagon {
    private int wagonId;
    private Wagon previousWagon;
    private Wagon nextWagon;


    public Wagon(int wagonId) {
        this.wagonId = wagonId;
    }


    public Wagon getLastWagonAttached() {
        // find the last wagon of the row of wagons attached to this wagon
        // if no wagons are attached return this wagon
        return (nextWagon != null ? nextWagon.getLastWagonAttached() : this);
    }

    public void setNextWagon(Wagon nextWagon) {
        // when setting the next wagon, set this wagon to be previous wagon of next wagon
        if (nextWagon == null) {
            this.nextWagon = null;
        } else {
            this.nextWagon = nextWagon;
            nextWagon.setPreviousWagon(this);
        }
    }


    public Wagon getPreviousWagon() {
        return previousWagon;
    }

    public void setPreviousWagon(Wagon previousWagon) {
        this.previousWagon = previousWagon;
    }

    public Wagon getNextWagon() {
        return nextWagon;
    }

    public int getWagonId() {
        return wagonId;
    }

    public int getNumberOfWagonsAttached() {
        return (!hasNextWagon() ? 0 : nextWagon.getNumberOfWagonsAttached() + 1);
    }

    public boolean hasNextWagon() {
        return !(nextWagon == null);
    }

    @Override
    public String toString() {
        return String.format("[Wagon %d]", wagonId);
    }
}

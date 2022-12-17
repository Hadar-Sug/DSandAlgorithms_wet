

public class KeyVal implements Comparable<KeyVal>{
    protected int key;
    protected int val;

    public KeyVal(int key, int val) {
        this.key = key;
        this.val = val;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }
    //TODO: perhaps change the key comaprison order, make sure to update the rankings methods
    public int compareTo(KeyVal other) {
        if (this.key>other.key)
            return -1;
        else if (this.key<other.key)
            return 1;
        else if (this.val>other.val)
            return 1; //tsting change
        else if(this.val<other.val)
            return -1;
        else return 0;
    }

}

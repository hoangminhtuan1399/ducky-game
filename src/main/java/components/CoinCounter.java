package components;

public class CoinCounter {
    private static CoinCounter instance;
    private int coinCount;

    public CoinCounter() {
        coinCount = 0;
    }

    public static CoinCounter getInstance() {
        if (instance == null) {
            instance = new CoinCounter();
        }
        return instance;
    }

    public void incrementCoinCount() {
        coinCount++;
      

    }

    public int getCoinCount() {
        return coinCount;
    }
    public void setCoinCount(int count) {
        coinCount = count;
    }
}

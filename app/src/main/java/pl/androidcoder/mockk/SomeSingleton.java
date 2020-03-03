package pl.androidcoder.mockk;

public class SomeSingleton {

    private static SomeSingleton singleton;

    public static SomeSingleton getSingleton() {
        if (singleton == null)
            singleton = new SomeSingleton();
        return singleton;
    }

    private String data = null;

    private SomeSingleton() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data.trim().toLowerCase();
    }

}


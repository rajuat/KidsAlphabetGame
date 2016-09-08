package com.itservz.android.mayekid.masing;

/**
 * Created by raju.athokpam on 08-09-2016.
 */
public class MasingCard {

    private int pic;
    private int count;
    private String title;
    private int sound;

    public MasingCard(int count, int pic, int sound, String title) {
        this.count = count;
        this.pic = pic;
        this.sound = sound;
        this.title = title;
    }

    public int getCount() {
        return count;
    }

    public int getPic() {
        return pic;
    }

    public int getSound() {
        return sound;
    }

    public String getTitle() {
        return title;
    }
}

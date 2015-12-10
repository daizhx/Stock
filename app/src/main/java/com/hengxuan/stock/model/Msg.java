package com.hengxuan.stock.model;

import android.util.Pair;

/**
 * Created by Administrator on 2015/8/24.
 */
public class Msg extends Pair<String,String> {
    public int id;
    /**
     * Constructor for a Pair.
     *
     * @param first  the first object in the Pair
     * @param second the second object in the pair
     */
    public Msg(String first, String second) {
        super(first, second);
    }
}

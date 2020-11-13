package com.yucaihuang.seckillspringboot.redis;

public class SeckillKey extends BasePrefix{

    public SeckillKey(String prefix) {
        super(prefix);
    }

    public static SeckillKey isGoodsOver = new SeckillKey("go");
    public static SeckillKey getSeckillUrl = new SeckillKey("mp");
}

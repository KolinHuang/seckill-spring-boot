package com.yucaihuang.seckillspringboot.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 秒杀商品
 */
public class SeckillGoods {
    //秒杀商品ID
    private Long id;
    //商品ID
    private Long goodsId;
    //秒杀价格
    private BigDecimal seckillPrice;
    //商品数量
    private Integer stockCount;
    //开始时间
    private Date startDate;
    //结束时间
    private Date endDate;

    public SeckillGoods() {
    }

    public SeckillGoods(Long id, Long goodsId, BigDecimal seckillPrice, Integer stockCount, Date startDate, Date endDate) {
        this.id = id;
        this.goodsId = goodsId;
        this.seckillPrice = seckillPrice;
        this.stockCount = stockCount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public BigDecimal getSeckillPrice() {
        return seckillPrice;
    }

    public void setSeckillPrice(BigDecimal seckillPrice) {
        this.seckillPrice = seckillPrice;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "SeckillGoods{" +
                "id=" + id +
                ", goodsId=" + goodsId +
                ", seckillPrice=" + seckillPrice +
                ", stockCount=" + stockCount +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}

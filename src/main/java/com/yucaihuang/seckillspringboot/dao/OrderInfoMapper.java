package com.yucaihuang.seckillspringboot.dao;

import com.yucaihuang.seckillspringboot.pojo.OrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderInfoMapper {

    /**
     * 根据ID删除订单信息
     * @param id
     * @return
     */
    public int delete(@Param("orderId") Long id);

    /**
     * 插入订单
     * @param orderInfo
     * @return
     */
    public int insert(OrderInfo orderInfo);

    /**
     * 查询所有订单信息
     * @return
     */
    public List<OrderInfo> queryAllOrderInfo();

    /**
     * 根据订单ID查询订单
     * @param id
     * @return
     */
    public OrderInfo queryOrderInfoById(@Param("orderId") Long id);

    /**
     * 更新订单
     * @param orderInfo
     * @return
     */
    public int update(OrderInfo orderInfo);

    /**
     * 更新订单部分信息
     * @param orderInfo
     * @return
     */
    public int updateSelective(OrderInfo orderInfo);

}

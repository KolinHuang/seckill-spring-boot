package com.yucaihuang.seckillspringboot;

import com.yucaihuang.seckillspringboot.dao.*;
import com.yucaihuang.seckillspringboot.pojo.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class SeckillSpringBootApplicationTests {

	@Autowired
	DataSource dataSource;

	@Resource
	GoodsMapper goodsMapper;

	@Resource
	OrderInfoMapper orderInfoMapper;

	@Resource
	SeckillGoodsMapper seckillGoodsMapper;

	@Resource
	SeckillOrderMapper seckillOrderMapper;

	@Resource
	UserMapper userMapper;

	@Test
	void contextLoads() throws SQLException {
		System.out.println(dataSource.getClass());
		Connection connection = dataSource.getConnection();
		System.out.println(connection);
		connection.close();
	}

	@Test
	void testGoodsMapper(){
		Goods goods = new Goods();
		goods.setCreateDate(new Date());
		goods.setGoodsDetail("更新测试2");
		goods.setGoodsImg("更新测试3");
		goods.setGoodsPrice(BigDecimal.valueOf(10000L));
		goods.setGoodsTitle("更新测试");
		goods.setUpdateDate(new Date());
		goods.setId(5L);
		goodsMapper.update(goods);
	}

	@Test
	void testOrderInfoMapper(){

		OrderInfo orderInfo = orderInfoMapper.queryOrderInfoById(43L);
		System.out.println(orderInfo);

	}

	@Test
	void testSeckillGoodsMapper(){
		int infect = seckillGoodsMapper.delete(5L);
		System.out.println(infect);
	}

	@Test
	void testSeckillOrderMapper(){

		SeckillOrder seckillOrder = seckillOrderMapper.querySeckillOrderByUserIdAndGoodsId(1L,2L);
		System.out.println(seckillOrder);
	}

	@Test
	void testUserMapper(){
		User user = userMapper.checkPhone("18077200000");
		System.out.println(user);
	}

}

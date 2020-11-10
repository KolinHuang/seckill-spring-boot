package com.yucaihuang.seckillspringboot;

import com.yucaihuang.seckillspringboot.bo.GoodsBo;
import com.yucaihuang.seckillspringboot.dao.*;
import com.yucaihuang.seckillspringboot.pojo.*;
import com.yucaihuang.seckillspringboot.service.OrderService;
import com.yucaihuang.seckillspringboot.service.SeckillGoodsService;
import com.yucaihuang.seckillspringboot.service.SeckillOrderService;
import com.yucaihuang.seckillspringboot.service.UserService;
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

		User userget = userMapper.checkPhone("15066729719");
		System.out.println(userget);

	}

	/**************************service层测试*******************************/

	@Resource
	OrderService orderService;

	@Resource
	SeckillGoodsService seckillGoodsService;

	@Resource
	SeckillOrderService seckillOrderService;

	@Resource
	UserService userService;

	@Test
	void testOrderService(){
		OrderInfo orderInfoById = orderService.getOrderInfoById(42);
		System.out.println(orderInfoById);
		orderInfoById.setId(null);
		orderInfoById.setGoodsName("订单服务测试1");
		orderService.addOrder(orderInfoById);
	}

	@Test
	void testSeckillGoodsService(){
		GoodsBo goodsBo = seckillGoodsService.getSeckillGoodsBoByGoodsId(1);
//		System.out.println(goodsBo);

		List<GoodsBo> seckillGoodsList = seckillGoodsService.getSeckillGoodsList();
//		for (GoodsBo bo : seckillGoodsList) {
//			System.out.println(bo);
//		}

		int i = seckillGoodsService.reduceStock(goodsBo.getId());
		System.out.println(i);


	}

	@Test
	void testSeckillOrderService(){
		SeckillOrder seckillOrderByUIdAndGId = seckillOrderService.getSeckillOrderByUIdAndGId(1, 2);
		System.out.println(seckillOrderByUIdAndGId);
		User user = userService.getUserByPhone("18077200000");
		String url = seckillOrderService.createUrl(user, 1);
		System.out.println(url);
	}

	@Test
	void testUserService(){
		User user = new User();
		user.setUserName("test01");
		user.setPhone("15067729719");
		user.setPassword("123456");
		user.setSalt("9d5b364d");
		user.setHead("测试用户");
		user.setLoginCount(1);
		user.setRegisterDate(new Date());
		user.setLastLoginDate(new Date());
		int insert = userService.insert(user);
		System.out.println(insert);
		User userByPhone = userService.getUserByPhone("15067729719");
		System.out.println(userByPhone);
	}






}

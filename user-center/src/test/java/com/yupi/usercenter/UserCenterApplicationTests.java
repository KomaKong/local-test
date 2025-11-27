package com.yupi.usercenter;
import java.util.Date;

import com.yupi.usercenter.model.User;
import com.yupi.usercenter.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = UserCenterApplication.class)
class UserCenterApplicationTests {

	@Autowired
	private UserService userService;
	@Test
	void contextLoads() {
		User user = new User();
		user.setUsername("bbb");
		user.setUserAccount("456");
		user.setAvatarUrl("0");
		user.setGender(0);
		user.setUserPassword("456");
		user.setPhone("456");
		user.setEmail("456");
		user.setUserStatus(0);
		user.setCreateTime(new Date());
		user.setUpdateTime(new Date());
		user.setIsDelete(0);
		boolean result =  userService.save(user);
		System.out.println("-------------------------");
		System.out.println(result);
		System.out.println(user.getId());
	}


}

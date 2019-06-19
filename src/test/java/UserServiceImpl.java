import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service("UserServiceImpl")
public class UserServiceImpl implements UserService {

        @Autowired
        private UserDao userDao;

        public User getUserById(int userId) {
            return this.userDao.selectByPrimaryKey(userId);
            //User user = new User();
            //return user;
        }

        public int createUser(int userId,String name,String email){
            User newuser = new User();
            newuser.setName(name);
            newuser.setEmail(email);
            newuser.setId(userId);
            newuser.setCdate(new Date());
            return this.userDao.insertSelective(newuser);
        }


    }
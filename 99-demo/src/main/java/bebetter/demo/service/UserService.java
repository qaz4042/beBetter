package bebetter.demo.service;

import bebetter.demo.entity.User;
import bebetter.demo.mapper.UserMapper;
import bebetter.mybatisplus.GeneralServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author LiZuBin
 * @date 2021/2/20
 */
@Service
public class UserService extends GeneralServiceImpl<UserMapper, User> {

}

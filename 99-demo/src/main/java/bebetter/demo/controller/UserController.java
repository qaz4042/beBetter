package bebetter.demo.controller;

import bebetter.demo.entity.User;
import bebetter.demo.service.UserService;
import bebetter.mybatisplus.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author LiZuBin
 * @date 2021/2/20
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public R<List<User>> list() {
        return R.ok(userService.list());
    }
}

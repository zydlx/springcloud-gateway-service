package com.zwc.a.controller;

import com.zwc.a.pojo.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 赵育冬
 */
@RestController
public class MyController {
    @RequestMapping("/routezyd/zyd")
    public String routeZyd(@RequestBody User user) {
        return "my name is" + user.toString() ;
    }
    @RequestMapping("/routezyd/zzz")
    public int routeZzz(@RequestBody User user) {
//        return "my name is" + user.toString() ;
        return 1/0;
    }
    @RequestMapping("/{id}")
    public User routeZzz(@PathVariable Long id) {
        return new User("zyd",1);
    }


}

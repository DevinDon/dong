package priv.ls.prisonmanagement.controller;

import org.springframework.web.bind.annotation.*;
import priv.ls.prisonmanagement.common.AjaxResult;
import priv.ls.prisonmanagement.common.Page;
import priv.ls.prisonmanagement.pojo.entity.User;
import priv.ls.prisonmanagement.service.IUserService;

import javax.annotation.Resource;

/**
 * @author XLY
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @PostMapping("/login")
    public AjaxResult login(User user){
        return userService.login(user);
    }

    @PostMapping
    public AjaxResult insert(User user){
        return userService.insert(user);
    }

    @PostMapping("/update")
    public AjaxResult update(User user){
        return userService.update(user);
    }

    @PostMapping("/delete")
    public AjaxResult delete(Integer id){
        return userService.delete(id);
    }

    @GetMapping
    public AjaxResult list(Page page){
        return userService.list(page);
    }
}

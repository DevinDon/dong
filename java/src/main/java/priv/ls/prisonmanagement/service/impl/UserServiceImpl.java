package priv.ls.prisonmanagement.service.impl;

import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import priv.ls.prisonmanagement.common.AjaxResult;
import priv.ls.prisonmanagement.common.BaseResult;
import priv.ls.prisonmanagement.common.Page;
import priv.ls.prisonmanagement.common.ReturnCode;
import priv.ls.prisonmanagement.dao.IUserMapper;
import priv.ls.prisonmanagement.pojo.entity.User;
import priv.ls.prisonmanagement.service.IUserService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author XLY
 */
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private IUserMapper userMapper;

    @Override
    public AjaxResult login(User user) {
        if (user.getUsername() == null || user.getPassword() == null) {
            return BaseResult.failedResult(ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getReturnCode(),
                    ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getDesc());
        }
        User getUser = new User();
        getUser.setUsername(user.getUsername());
        getUser = userMapper.get(getUser);
        if (getUser == null) {
            return BaseResult.failedResult(ReturnCode.ACCOUNT_OR_PASSWORD_WRONG.getReturnCode(),
                    ReturnCode.ACCOUNT_OR_PASSWORD_WRONG.getDesc());
        }
        if (getUser.getPassword().equals(user.getPassword())) {
            getUser.setPassword(null);
            return BaseResult.successResult(getUser);
        }
        return BaseResult.failedResult(ReturnCode.ACCOUNT_OR_PASSWORD_WRONG.getReturnCode(),
                ReturnCode.ACCOUNT_OR_PASSWORD_WRONG.getDesc());
    }

    @Override
    public AjaxResult insert(User user) {
        if (user.getUsername() == null || user.getPassword() == null || user.getName() == null) {
            return BaseResult.failedResult(ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getReturnCode(),
                    ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getDesc());
        }
        User getUser = new User();
        getUser.setUsername(user.getUsername());
        getUser = userMapper.get(getUser);
        if (getUser != null) {
            return BaseResult.failedResult(ReturnCode.USERNAME_EXIST.getReturnCode(),
                    ReturnCode.USERNAME_EXIST.getDesc());
        }
        int result = userMapper.insert(user);
        return result == 1 ? BaseResult.successResult() : BaseResult.failedResult(
                ReturnCode.SYSTEM_ERROR.getReturnCode(), ReturnCode.SYSTEM_ERROR.getDesc());
    }

    @Override
    public AjaxResult update(User user) {
        if (user.getId() == null || (user.getPassword() == null && user.getName() == null)) {
            return BaseResult.failedResult(ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getReturnCode(),
                    ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getDesc());
        }
        user.setUpdateTime(new Date());
        return BaseResult.successResult(userMapper.update(user));
    }

    @Override
    public AjaxResult delete(Integer id) {
        if (id == null) {
            return BaseResult.failedResult(ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getReturnCode(),
                    ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getDesc());
        }
        return BaseResult.successResult(userMapper.delete(id));
    }

    @Override
    public AjaxResult list(Page page) {
        com.github.pagehelper.Page selectPage = PageHelper.startPage(page);
        List<User> users = userMapper.list();
        page.setTotal(selectPage.getTotal());
        return BaseResult.successResult(users, page);
    }
}

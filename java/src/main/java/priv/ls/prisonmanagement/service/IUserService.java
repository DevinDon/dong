package priv.ls.prisonmanagement.service;

import priv.ls.prisonmanagement.common.AjaxResult;
import priv.ls.prisonmanagement.common.Page;
import priv.ls.prisonmanagement.pojo.entity.User;

/**
 * @author XLY
 */
public interface IUserService {
    AjaxResult login(User user);

    AjaxResult insert(User user);

    AjaxResult update(User user);

    AjaxResult delete(Integer id);

    AjaxResult list(Page page);
}

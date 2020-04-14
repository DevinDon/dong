package priv.ls.prisonmanagement.service;

import priv.ls.prisonmanagement.common.AjaxResult;
import priv.ls.prisonmanagement.common.Page;
import priv.ls.prisonmanagement.pojo.entity.Jail;

/**
 * @author XLY
 */
public interface IJailService {

    AjaxResult insert(Jail jail);

    AjaxResult update(Jail jail);

    AjaxResult delete(String id);

    AjaxResult list(Page page);

}

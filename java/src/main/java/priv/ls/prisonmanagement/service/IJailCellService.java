package priv.ls.prisonmanagement.service;

import priv.ls.prisonmanagement.common.AjaxResult;
import priv.ls.prisonmanagement.pojo.dto.JailCellDTO;
import priv.ls.prisonmanagement.pojo.entity.JailCell;

/**
 * @author XLY
 */
public interface IJailCellService {

    AjaxResult insert(JailCell jailCell);

    AjaxResult update(JailCell jailCell);

    AjaxResult delete(Integer id);

    AjaxResult list(JailCellDTO jailCell);

}

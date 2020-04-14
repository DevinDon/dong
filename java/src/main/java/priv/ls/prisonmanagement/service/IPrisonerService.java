package priv.ls.prisonmanagement.service;

import priv.ls.prisonmanagement.common.AjaxResult;
import priv.ls.prisonmanagement.pojo.dto.PrisonerDTO;
import priv.ls.prisonmanagement.pojo.entity.Prisoner;

/**
 * @author XLY
 */
public interface IPrisonerService {

    AjaxResult insert(Prisoner prisoner);

    AjaxResult list(PrisonerDTO prisonerDTO);

    AjaxResult update(Prisoner prisoner);

    AjaxResult delete(Integer id);
}

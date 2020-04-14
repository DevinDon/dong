package priv.ls.prisonmanagement.service;

import priv.ls.prisonmanagement.common.AjaxResult;
import priv.ls.prisonmanagement.pojo.dto.PrisonerInfoDTO;
import priv.ls.prisonmanagement.pojo.entity.PrisonerInfo;

import java.text.ParseException;

/**
 * @author XLY
 */
public interface IPrisonerInfoService {

    AjaxResult insert(PrisonerInfoDTO prisonerInfo) throws ParseException;

    AjaxResult update(PrisonerInfoDTO prisonerInfo) throws ParseException;

    AjaxResult get(Integer id);
}

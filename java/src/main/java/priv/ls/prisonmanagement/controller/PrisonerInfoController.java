package priv.ls.prisonmanagement.controller;

import org.springframework.web.bind.annotation.*;
import priv.ls.prisonmanagement.common.AjaxResult;
import priv.ls.prisonmanagement.pojo.dto.PrisonerInfoDTO;
import priv.ls.prisonmanagement.pojo.entity.PrisonerInfo;
import priv.ls.prisonmanagement.service.IPrisonerInfoService;

import javax.annotation.Resource;
import java.text.ParseException;

/**
 * @author XLY
 */
@RestController
@RequestMapping("/prisoner/info")
public class PrisonerInfoController {

  @Resource
  private IPrisonerInfoService prisonerInfoService;

  @PostMapping
  public AjaxResult insert(PrisonerInfoDTO prisonerInfo) throws ParseException {
    return prisonerInfoService.insert(prisonerInfo);
  };

  @PostMapping("/update")
  public AjaxResult update(PrisonerInfoDTO prisonerInfo) throws ParseException {
    return prisonerInfoService.update(prisonerInfo);
  };

  @GetMapping
  public AjaxResult get(Integer id) {
    return prisonerInfoService.get(id);
  };

}

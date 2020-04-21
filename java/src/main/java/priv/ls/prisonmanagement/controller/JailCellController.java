package priv.ls.prisonmanagement.controller;

import org.springframework.web.bind.annotation.*;
import priv.ls.prisonmanagement.common.AjaxResult;
import priv.ls.prisonmanagement.pojo.dto.JailCellDTO;
import priv.ls.prisonmanagement.pojo.entity.JailCell;
import priv.ls.prisonmanagement.service.IJailCellService;

import javax.annotation.Resource;

/**
 * @author XLY
 */
@RestController
@RequestMapping("/jail/cell")
public class JailCellController {

  @Resource
  private IJailCellService jailCellService;

  @PostMapping
  public AjaxResult insert(JailCell jailCell) {
    return jailCellService.insert(jailCell);
  }

  @PutMapping("/update")
  public AjaxResult update(JailCell jailCell) {
    return jailCellService.update(jailCell);
  }

  @DeleteMapping("/delete")
  public AjaxResult delete(Integer id) {
    return jailCellService.delete(id);
  }

  @PostMapping("/list")
  public AjaxResult list(JailCellDTO jailCell) {
    return jailCellService.list(jailCell);
  }

}

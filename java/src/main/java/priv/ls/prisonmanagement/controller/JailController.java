package priv.ls.prisonmanagement.controller;

import org.springframework.web.bind.annotation.*;
import priv.ls.prisonmanagement.common.AjaxResult;
import priv.ls.prisonmanagement.common.Page;
import priv.ls.prisonmanagement.pojo.entity.Jail;
import priv.ls.prisonmanagement.service.IJailService;

import javax.annotation.Resource;

/**
 * @author XLY
 */
@RestController
@RequestMapping("/jail")
public class JailController {

  @Resource
  private IJailService jailService;

  @PostMapping
  public AjaxResult insert(Jail jail) {
    return jailService.insert(jail);
  }

  @PutMapping("/update")
  public AjaxResult update(Jail jail) {
    return jailService.update(jail);
  }

  @DeleteMapping("/delete")
  public AjaxResult delete(String id) {
    return jailService.delete(id);
  }

  @GetMapping
  public AjaxResult list(Page page) {
    return jailService.list(page);
  }

}

package priv.ls.prisonmanagement.controller;

import org.springframework.web.bind.annotation.*;
import priv.ls.prisonmanagement.common.AjaxResult;
import priv.ls.prisonmanagement.pojo.dto.PrisonerDTO;
import priv.ls.prisonmanagement.pojo.entity.Prisoner;
import priv.ls.prisonmanagement.service.IPrisonerService;

import javax.annotation.Resource;

/**
 * @author XLY
 */
@RestController
@RequestMapping("/prisoner")
public class PrisonerController {

    @Resource
    private IPrisonerService prisonerService;

    @PostMapping
    public AjaxResult insert(Prisoner prisoner){
        return prisonerService.insert(prisoner);
    }

    @GetMapping
    public AjaxResult list(PrisonerDTO prisonerDTO){
        return prisonerService.list(prisonerDTO);
    };

    @PostMapping("/update")
    public AjaxResult update(Prisoner prisoner){
        return prisonerService.update(prisoner);
    }

    @PostMapping("/delete")
    public AjaxResult delete(Integer id){
        return prisonerService.delete(id);
    };
}

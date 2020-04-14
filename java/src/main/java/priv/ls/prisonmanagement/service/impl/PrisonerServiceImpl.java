package priv.ls.prisonmanagement.service.impl;

import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import priv.ls.prisonmanagement.common.AjaxResult;
import priv.ls.prisonmanagement.common.BaseResult;
import priv.ls.prisonmanagement.common.Page;
import priv.ls.prisonmanagement.common.ReturnCode;
import priv.ls.prisonmanagement.dao.IJailCellMapper;
import priv.ls.prisonmanagement.dao.IPrisonerMapper;
import priv.ls.prisonmanagement.pojo.dto.PrisonerDTO;
import priv.ls.prisonmanagement.pojo.entity.JailCell;
import priv.ls.prisonmanagement.pojo.entity.Prisoner;
import priv.ls.prisonmanagement.pojo.vo.PrisonerVO;
import priv.ls.prisonmanagement.service.IPrisonerService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author XLY
 */
@Service
public class PrisonerServiceImpl implements IPrisonerService {

    @Resource
    private IPrisonerMapper prisonerMapper;

    @Resource
    private IJailCellMapper jailCellMapper;

    @Override
    public AjaxResult insert(Prisoner prisoner) {
        if (prisoner.getViolation() == null) {
            return BaseResult.failedResult(ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getReturnCode(),
                    ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getDesc());
        }
        return BaseResult.successResult(prisonerMapper.insert(prisoner));
    }

    @Override
    public AjaxResult list(PrisonerDTO prisonerDTO) {
        if (prisonerDTO.getPage() == null) {
            return BaseResult.failedResult(ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getReturnCode(),
                    ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getDesc());
        }
        Page page = prisonerDTO.getPage();
        com.github.pagehelper.Page selectPage = PageHelper.startPage(page);
        Prisoner prisoner = new Prisoner();
        if (prisonerDTO.getInfoId() != null) {
            prisoner.setInfoId(prisonerDTO.getId());
        }
        if (prisonerDTO.getJailCellId() != null) {
            prisoner.setJailCellId(prisonerDTO.getJailCellId());
        }
        List<PrisonerVO> prisonerVOS = prisonerMapper.list(prisoner);
        page.setTotal(selectPage.getTotal());
        return BaseResult.successResult(prisonerVOS, page);
    }

    @Override
    public AjaxResult update(Prisoner prisoner) {
        if (prisoner.getId() == null) {
            return BaseResult.failedResult(ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getReturnCode(),
                    ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getDesc());
        }
        Integer jailCellId = prisoner.getJailCellId();
        if (jailCellId != null && jailCellId != -1) {
            JailCell jailCell = jailCellMapper.get(jailCellId);
            if (jailCell == null) {
                return BaseResult.failedResult(ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getReturnCode(),
                        ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getDesc());
            }
            Integer studentCount = prisonerMapper.countByJailId(jailCellId);
            if (studentCount.equals(jailCell.getSize())) {
                return BaseResult.failedResult(ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getReturnCode(),
                        "该牢房已满");
            }
        }
        prisoner.setUpdateTime(new Date());
        return BaseResult.successResult(prisonerMapper.update(prisoner));
    }

    @Override
    public AjaxResult delete(Integer id) {
        return BaseResult.successResult(prisonerMapper.delete(id));
    }

}

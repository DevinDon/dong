package priv.ls.prisonmanagement.service.impl;

import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import priv.ls.prisonmanagement.common.AjaxResult;
import priv.ls.prisonmanagement.common.BaseResult;
import priv.ls.prisonmanagement.common.Page;
import priv.ls.prisonmanagement.common.ReturnCode;
import priv.ls.prisonmanagement.dao.IJailCellMapper;
import priv.ls.prisonmanagement.dao.IJailMapper;
import priv.ls.prisonmanagement.dao.IPrisonerMapper;
import priv.ls.prisonmanagement.pojo.dto.JailCellDTO;
import priv.ls.prisonmanagement.pojo.entity.Jail;
import priv.ls.prisonmanagement.pojo.entity.JailCell;
import priv.ls.prisonmanagement.pojo.entity.Prisoner;
import priv.ls.prisonmanagement.pojo.vo.JailCellVO;
import priv.ls.prisonmanagement.pojo.vo.PrisonerVO;
import priv.ls.prisonmanagement.service.IJailCellService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author XLY
 */
@Service
public class JailCellServiceImpl implements IJailCellService{

    @Resource
    private IJailMapper jailMapper;

    @Resource
    private IJailCellMapper jailCellMapper;

    @Resource
    private IPrisonerMapper prisonerMapper;

    @Override
    public AjaxResult insert(JailCell jailCell) {
        if (jailCell.getJailId() == null || jailCell.getSize() == null) {
            return BaseResult.failedResult(ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getReturnCode(),
                    ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getDesc());
        }
        return BaseResult.successResult(jailCellMapper.insert(jailCell));
    }

    @Override
    public AjaxResult update(JailCell jailCell) {
        if (jailCell.getJailId() == null || jailCell.getSize() == null) {
            return BaseResult.failedResult(ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getReturnCode(),
                    ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getDesc());
        }
        jailCell.setUpdateTime(new Date());
        return BaseResult.successResult(jailCellMapper.update(jailCell));
    }

    @Override
    @Transactional
    public AjaxResult delete(Integer id) {
        if (id == null) {
            return BaseResult.failedResult(ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getReturnCode(),
                    ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getDesc());
        }
        Prisoner prisoner = new Prisoner();
        prisoner.setJailCellId(id);
        List<PrisonerVO> prisoners = prisonerMapper.list(prisoner);
        for (PrisonerVO prisonerVO : prisoners){
            Prisoner updatePrisoner = new Prisoner();
            updatePrisoner.setId(prisonerVO.getId());
            updatePrisoner.setJailCellId(-1);
            updatePrisoner.setUpdateTime(new Date());
            prisonerMapper.update(updatePrisoner);
        }
        JailCell jailCell = new JailCell();
        jailCell.setId(id);
        return BaseResult.successResult(jailCellMapper.delete(jailCell));
    }

    @Override
    public AjaxResult list(JailCellDTO jailCell) {
        if (jailCell.getPage() == null) {
            return BaseResult.failedResult(ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getReturnCode(),
                    ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getDesc());
        }
        Page page = jailCell.getPage();
        com.github.pagehelper.Page selectPage = PageHelper.startPage(page);
        List<JailCell> jailCells = jailCellMapper.list(jailCell.getJailId());
        page.setTotal(selectPage.getTotal());
        List<JailCellVO> jailCellVOS = new ArrayList<>();
        for (JailCell cell : jailCells) {
            JailCellVO jailCellVO = new JailCellVO();
            Jail jail = jailMapper.get(cell.getJailId());
            jailCellVO.setId(cell.getId());
            jailCellVO.setJailId(cell.getJailId());
            jailCellVO.setJailName(jail.getName());
            jailCellVO.setAddress(jail.getAddress());
            jailCellVO.setSize(cell.getSize());
            jailCellVOS.add(jailCellVO);
        }
        return BaseResult.successResult(jailCellVOS, page);
    }
}

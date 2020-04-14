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
import priv.ls.prisonmanagement.pojo.entity.Jail;
import priv.ls.prisonmanagement.pojo.entity.JailCell;
import priv.ls.prisonmanagement.pojo.entity.Prisoner;
import priv.ls.prisonmanagement.pojo.vo.PrisonerVO;
import priv.ls.prisonmanagement.service.IJailService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author XLY
 */
@Service
public class JailServiceImpl implements IJailService {

    @Resource
    private IJailMapper jailMapper;

    @Resource
    private IJailCellMapper jailCellMapper;

    @Resource
    private IPrisonerMapper prisonerMapper;

    @Override
    public AjaxResult insert(Jail jail) {
        if (jail.getName() == null || jail.getAddress() == null) {
            return BaseResult.failedResult(ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getReturnCode(),
                    ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getDesc());
        }
        return BaseResult.successResult(jailMapper.insert(jail));
    }

    @Override
    public AjaxResult update(Jail jail) {
        if (jail.getName() == null || jail.getAddress() == null) {
            return BaseResult.failedResult(ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getReturnCode(),
                    ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getDesc());
        }
        jail.setUpdateTime(new Date());
        return BaseResult.successResult(jailMapper.update(jail));
    }

    @Override
    @Transactional
    public AjaxResult delete(String idStr) {
        Integer id = new Integer(idStr);
        List<JailCell> jailCells = jailCellMapper.list(id);
        for (JailCell jailCell : jailCells) {
            Prisoner prisoner = new Prisoner();
            prisoner.setJailCellId(jailCell.getId());
            List<PrisonerVO> prisonerList = prisonerMapper.list(prisoner);
            for (PrisonerVO prisonerVO : prisonerList) {
                prisoner.setId(prisonerVO.getId());
                prisoner.setJailCellId(-1);
                prisoner.setUpdateTime(new Date());
                prisonerMapper.update(prisoner);
            }
        }
        JailCell jailCell = new JailCell();
        jailCell.setJailId(id);
        jailCellMapper.delete(jailCell);
        jailMapper.delete(id);
        return BaseResult.successResult();
    }

    @Override
    public AjaxResult list(Page page) {
        if (page == null) {
            return BaseResult.failedResult(ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getReturnCode(),
                    ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getDesc());
        }
        com.github.pagehelper.Page selectPage = PageHelper.startPage(page);
        List<Jail> jails = jailMapper.list(new Jail());
        page.setTotal(selectPage.getTotal());
        return BaseResult.successResult(jails, page);
    }
}

package priv.ls.prisonmanagement.service.impl;

import org.springframework.stereotype.Service;
import priv.ls.prisonmanagement.common.AjaxResult;
import priv.ls.prisonmanagement.common.BaseResult;
import priv.ls.prisonmanagement.common.ReturnCode;
import priv.ls.prisonmanagement.dao.IPrisonerInfoMapper;
import priv.ls.prisonmanagement.dao.IPrisonerMapper;
import priv.ls.prisonmanagement.pojo.dto.PrisonerInfoDTO;
import priv.ls.prisonmanagement.pojo.entity.Prisoner;
import priv.ls.prisonmanagement.pojo.entity.PrisonerInfo;
import priv.ls.prisonmanagement.service.IPrisonerInfoService;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author XLY
 */
@Service
public class PrisonerInfoServiceImpl implements IPrisonerInfoService {

    @Resource
    private IPrisonerInfoMapper prisonerInfoMapper;

    @Resource
    private IPrisonerMapper prisonerMapper;

    @Override
    public AjaxResult insert(PrisonerInfoDTO prisonerInfo) throws ParseException {
        if (prisonerInfo.getIdCard() == null || prisonerInfo.getName() == null || prisonerInfo.getBirthTime() == null) {
            return BaseResult.failedResult(ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getReturnCode(),
                    ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getDesc());
        }
        PrisonerInfo entity = new PrisonerInfo();
        entity.setIdCard(prisonerInfo.getIdCard());
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
        entity.setBirthTime(formatter.parse(prisonerInfo.getBirthTime()));
        entity.setName(prisonerInfo.getName());
        prisonerInfoMapper.insert(entity);
        Prisoner prisoner = new Prisoner();
        prisoner.setId(prisonerInfo.getPrisonerId());
        prisoner.setInfoId(entity.getId());
        prisoner.setUpdateTime(new Date());
        return BaseResult.successResult(prisonerMapper.update(prisoner));
    }

    @Override
    public AjaxResult update(PrisonerInfoDTO prisonerInfo) throws ParseException {
        if (prisonerInfo.getIdCard() == null || prisonerInfo.getName() == null || prisonerInfo.getBirthTime() == null
                || prisonerInfo.getId() == null) {
            return BaseResult.failedResult(ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getReturnCode(),
                    ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getDesc());
        }
        prisonerInfo.setUpdateTime(new Date());
        PrisonerInfo info = new PrisonerInfo();
        info.setId(prisonerInfo.getId());
        info.setName(prisonerInfo.getName());
        info.setIdCard(prisonerInfo.getIdCard());
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
        info.setBirthTime(formatter.parse(prisonerInfo.getBirthTime().replace("T1", " ")));
        return BaseResult.successResult(prisonerInfoMapper.update(info));
    }

    @Override
    public AjaxResult get(Integer id) {
        if (id == null) {
            return BaseResult.failedResult(ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getReturnCode(),
                    ReturnCode.MISSING_SERVLET_REQUEST_PARAMETER.getDesc());
        }
        return BaseResult.successResult((prisonerInfoMapper.get(id)));
    }
}

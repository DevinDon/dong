package priv.ls.prisonmanagement.dao;

import org.apache.ibatis.annotations.Mapper;
import priv.ls.prisonmanagement.pojo.entity.Prisoner;
import priv.ls.prisonmanagement.pojo.vo.PrisonerVO;

import java.util.List;

/**
 * @author XLY
 */
@Mapper
public interface IPrisonerMapper {
    int insert(Prisoner prisoner);

    int update(Prisoner prisoner);

    int delete(Integer id);

    List<PrisonerVO> list(Prisoner prisoner);

    Prisoner get(Prisoner prisoner);

    Integer countByJailId(Integer jailId);
}

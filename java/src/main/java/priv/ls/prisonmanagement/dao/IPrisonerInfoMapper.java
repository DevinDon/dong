package priv.ls.prisonmanagement.dao;

import org.apache.ibatis.annotations.Mapper;
import priv.ls.prisonmanagement.pojo.entity.PrisonerInfo;

/**
 * @author XLY
 */
@Mapper
public interface IPrisonerInfoMapper {
    int insert(PrisonerInfo prisonerInfo);

    int update(PrisonerInfo prisonerInfo);

    int delete(Integer id);

    PrisonerInfo get(Integer id);
}

package priv.ls.prisonmanagement.dao;

import org.apache.ibatis.annotations.Mapper;
import priv.ls.prisonmanagement.pojo.entity.Jail;

import java.util.List;

/**
 * @author XLY
 */
@Mapper
public interface IJailMapper {

    int insert(Jail jail);

    int update(Jail jail);

    List<Jail> list(Jail jail);

    Jail get(Integer id);

    int delete(Integer id);
}

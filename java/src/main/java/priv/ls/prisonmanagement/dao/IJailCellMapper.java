package priv.ls.prisonmanagement.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import priv.ls.prisonmanagement.pojo.entity.JailCell;

import java.util.List;

/**
 * @author XLY
 */
@Mapper
public interface IJailCellMapper {

    int insert(JailCell jailCell);

    int update(JailCell jailCell);

    int delete(JailCell jailCell);

    List<JailCell> list(@Param("jailId") Integer jailId);

    JailCell get(@Param("id") Integer id);
}

package priv.ls.prisonmanagement.dao;

import org.apache.ibatis.annotations.Mapper;
import priv.ls.prisonmanagement.pojo.entity.User;

import java.util.List;


/**
 * @author XLY
 */
@Mapper
public interface IUserMapper {

    int insert(User user);

    int update(User user);

    int delete(Integer id);

    User get(User user);

    List<User> list();
}

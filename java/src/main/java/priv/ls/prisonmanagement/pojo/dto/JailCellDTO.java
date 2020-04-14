package priv.ls.prisonmanagement.pojo.dto;


import priv.ls.prisonmanagement.common.Page;

/**
 * @author XLY
 */
public class JailCellDTO {
    private Integer id;
    private Integer jailId;
    private Integer size;
    private Page page;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getJailId() {
        return jailId;
    }

    public void setJailId(Integer jailId) {
        this.jailId = jailId;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}

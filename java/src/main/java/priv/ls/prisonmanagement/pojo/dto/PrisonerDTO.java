package priv.ls.prisonmanagement.pojo.dto;

import priv.ls.prisonmanagement.common.Page;

import java.util.Date;

/**
 * @author XLY
 */
public class PrisonerDTO {
    private Integer id;
    private Integer infoId;
    private String  violation;
    private Integer jailCellId;
    private Date    createTime;
    private Date    updateTime;
    private Page page;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getInfoId() {
        return infoId;
    }

    public void setInfoId(Integer infoId) {
        this.infoId = infoId;
    }

    public String getViolation() {
        return violation;
    }

    public void setViolation(String violation) {
        this.violation = violation;
    }

    public Integer getJailCellId() {
        return jailCellId;
    }

    public void setJailCellId(Integer jailCellId) {
        this.jailCellId = jailCellId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}

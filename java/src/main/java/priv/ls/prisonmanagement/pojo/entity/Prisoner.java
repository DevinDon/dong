package priv.ls.prisonmanagement.pojo.entity;

import java.util.Date;

/**
 * @author XLY
 */
public class Prisoner {
    private Integer id;
    private Integer infoId;
    private String  violation;
    private Integer jailCellId;
    private Date    createTime;
    private Date    updateTime;

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
}

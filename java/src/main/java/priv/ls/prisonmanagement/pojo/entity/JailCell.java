package priv.ls.prisonmanagement.pojo.entity;


import java.util.Date;

/**
 * @author XLY
 */
public class JailCell {
    private Integer id;
    private Integer jailId;
    private Integer size;
    private Date    createTime;
    private Date    updateTime;

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

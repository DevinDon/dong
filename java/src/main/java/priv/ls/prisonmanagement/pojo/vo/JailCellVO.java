package priv.ls.prisonmanagement.pojo.vo;

/**
 * @author XLY
 */
public class JailCellVO {
    private Integer id;
    private Integer jailId;
    private String jailName;
    private String address;
    private Integer size;

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

    public String getJailName() {
        return jailName;
    }

    public void setJailName(String jailName) {
        this.jailName = jailName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}

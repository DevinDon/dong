package priv.ls.prisonmanagement.pojo.vo;

/**
 * @author XLY
 */
public class PrisonerVO {

    private Integer id;
    private String violation;
    private Integer jailCellId;
    private String jailName;
    private String address;
    private Integer infoId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getInfoId() {
        return infoId;
    }

    public void setInfoId(Integer infoId) {
        this.infoId = infoId;
    }
}

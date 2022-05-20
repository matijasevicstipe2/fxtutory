package sample;


import java.sql.Date;

public class AdClient {
    private Long id;
    private String title;
    private String info;
    private Long price;
    private Date published;
    private Long locationId;
    private Long fieldId;
    private Long userId;


    public AdClient(){

    }

    public AdClient(Long id, String title, String info, Long price, Date published, Long locationId, Long fieldId, Long userId) {
        this.id = id;
        this.title = title;
        this.info = info;
        this.price = price;
        this.published = published;
        this.locationId = locationId;
        this.fieldId = fieldId;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "AdClient{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", info='" + info + '\'' +
                ", price=" + price +
                ", published=" + published +
                ", locationId=" + locationId +
                ", fieldId=" + fieldId +
                ", userId=" + userId +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

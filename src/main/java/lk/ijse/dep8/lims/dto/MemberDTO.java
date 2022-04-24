package lk.ijse.dep8.lims.dto;

import jakarta.json.bind.annotation.JsonbProperty;

public class MemberDTO {
    private String id;
    private String name;
    private String email;
    private String address;
    @JsonbProperty("contact")
    private String contact;

    public MemberDTO() {
    }

    public MemberDTO(String name, String email, String address, String contactNo) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.contact = contactNo;
    }

    public MemberDTO(String id, String name, String email, String address, String contactNo) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.contact = contactNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contactNo) {
        this.contact = contactNo;
    }

    @Override
    public String toString() {
        return "MemberDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", contactNo='" + contact + '\'' +
                '}';
    }
}

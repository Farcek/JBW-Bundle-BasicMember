/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mn.le.farcek.jbw.bundle.member;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import mn.le.farcek.common.entity.FCoreEntity;
import mn.le.farcek.jbw.api.security.IUser;
import mn.le.farcek.jbw.bundle.beanForm.Form;
import mn.le.farcek.jbw.bundle.beanView.DataTable;
import mn.le.farcek.validation.Email;
import mn.le.farcek.validation.NotEmpty;

/**
 *
 * @author Farcek
 */
@Entity
@Form(fields = {"email","roles"})
@DataTable(pk = "id", fields = {"email","roles"})

public class MemberEntity extends FCoreEntity implements IUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "MemberEntity")
    private Integer id;

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String password;
    private List<String> roles;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return getEmail();
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public List<String> getRoles() {
        return roles;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mn.le.farcek.jbw.bundle.member;

import com.google.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mn.le.farcek.common.entity.criteria.BinaryOperator;
import mn.le.farcek.common.entity.ejb.FServiceException;
import mn.le.farcek.jbw.api.Bundle;
import mn.le.farcek.jbw.api.IService;
import mn.le.farcek.jbw.api.bundle.JBWBundle;
import mn.le.farcek.jbw.api.managers.IUrlBuilder;
import mn.le.farcek.jbw.api.security.ISecurityManager;
import mn.le.farcek.jbw.bundle.admin.JBWAdminMenuManager;

/**
 *
 * @author Farcek
 */
@Bundle(name = "member")
public class JBWBundleMember extends JBWBundle {

    @Override
    protected void setupControllers(List<Class<?>> controllers) {
        controllers.add(Main.class);
        controllers.add(Admin.class);
    }

    @Inject
    void init(IService service, ISecurityManager securityManager) {
        String email = "admin@admin.com";
        MemberEntity m = service.entityBy(MemberEntity.class, new BinaryOperator("email", email));
        if (m == null) {
            m = new MemberEntity();
            m.setEmail(email);
            m.setPassword(securityManager.encryptPassword("11", email));
            m.setRoles(Arrays.asList("admin"));
            try {
                service.doCreate(MemberEntity.class, m);
            } catch (FServiceException ex) {
                
            }
        }

    }

    @Inject
    void init(JBWAdminMenuManager adminMenuManager, IUrlBuilder urlBuilder) {
        adminMenuManager.add("member/index", "member/admin/index.html");
        adminMenuManager.add("member/pass", "member/admin/pass.html");
    }

}

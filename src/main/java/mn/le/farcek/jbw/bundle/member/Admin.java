/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mn.le.farcek.jbw.bundle.member;

import com.google.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;
import mn.le.farcek.common.entity.criteria.BinaryOperator;
import mn.le.farcek.common.entity.ejb.FServiceException;
import mn.le.farcek.common.utils.FCollectionUtils;
import mn.le.farcek.common.utils.FStringUtils;
import mn.le.farcek.jbw.api.Action;
import mn.le.farcek.jbw.api.Controller;
import mn.le.farcek.jbw.api.action.IActionMethod;
import mn.le.farcek.jbw.api.action.IActionRequest;
import mn.le.farcek.jbw.api.security.ISecurityManager;
import mn.le.farcek.jbw.bundle.core.utils.SampleCrudController;

/**
 *
 * @author Farcek
 */
@Controller()
public class Admin extends SampleCrudController<MemberEntity> {

    public Admin() {
        super(MemberEntity.class);
    }

    @Inject
    ISecurityManager securityManager;

    @Action(role = "admin")
    public Object pass(IActionRequest request) {
        if (request.getMethod() == IActionMethod.Post) {
            String email = request.getParameter("email");

            MemberEntity i = service.entityBy(MemberEntity.class, new BinaryOperator("email", email));
            if (i == null)
                return new FCollectionUtils.HashMapBuilder<String, Object>()
                        .put("msg", "email not found")
                        .put("error", true)
                        .build();

            String pass = request.getParameter("pass");
            if (FStringUtils.isEmptyOrNull(pass, true))
                return new FCollectionUtils.HashMapBuilder<String, Object>()
                        .put("msg", "password is blank")
                        .put("error", true)
                        .build();

            i.setPassword(securityManager.encryptPassword(pass.trim(), email));

            try {
                service.doUpdate(MemberEntity.class, i);
                return new FCollectionUtils.HashMapBuilder<String, Object>()
                        .put("success", true)
                        .build();
            } catch (FServiceException ex) {
                return new FCollectionUtils.HashMapBuilder<String, Object>()
                        .put("msg", ex.getMessage())
                        .put("error", true)
                        .build();
            }
        }
        return null;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mn.le.farcek.jbw.bundle.member;

import com.google.inject.Inject;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.constraints.NotNull;
import mn.le.farcek.beanform.BeanForm;
import mn.le.farcek.common.entity.criteria.BinaryOperator;
import mn.le.farcek.jbw.api.Action;
import mn.le.farcek.jbw.api.Controller;
import mn.le.farcek.jbw.api.IService;
import mn.le.farcek.jbw.api.action.IActionMethod;
import mn.le.farcek.jbw.api.action.IActionRequest;
import mn.le.farcek.jbw.api.action.IActionSession;
import mn.le.farcek.jbw.api.action.IActionView;
import mn.le.farcek.jbw.api.action.view.RedirectView;
import mn.le.farcek.jbw.api.managers.IUrlBuilder;
import mn.le.farcek.jbw.api.security.ISecurityManager;
import mn.le.farcek.jbw.api.validation.ValidationManager;
import mn.le.farcek.jbw.bundle.beanForm.BeanFormFactory;
import mn.le.farcek.jbw.bundle.beanForm.elements.Input;
import mn.le.farcek.validation.Email;
import mn.le.farcek.validation.NotEmpty;

/**
 *
 * @author Farcek
 */
@Controller()
public class Main {

    @Inject
    BeanFormFactory formFactory;

    @Inject
    ValidationManager validationManager;

    @Inject
    ISecurityManager securityManager;

    @Inject
    IService service;
    @Inject
    IUrlBuilder urlBuilder;

    //<editor-fold defaultstate="collapsed" desc="Login">
    @Action()
    public Object login(IActionRequest request, IActionSession session) {
        FrmLogin bean = new FrmLogin();
        BeanForm<?> frm = formFactory.factory(bean);

        if (request.getMethod() == IActionMethod.Post) {
            frm.load(formFactory.factoryLoader(request));

            Set<ConstraintViolation<FrmLogin>> validate = validationManager.validate(bean);

            if (frm.valid()) {
                String encryptPassword = securityManager.encryptPassword(bean.password, bean.email);

                MemberEntity en = service.entityBy(MemberEntity.class, new BinaryOperator("email", bean.email), new BinaryOperator("password", encryptPassword));
                if (en != null) {
                    securityManager.setAuthentication(en, session);

                    String url = request.getParameter("return", null);
                    if (url == null)
                        url = urlBuilder.buildBundle("/", IUrlBuilder.Mode.SHORT);

                    return new RedirectView(url);
                } else
                    frm.addError("invalid email or password");
            }

        }
        String msg = request.getParameter("msg", null);
        if (msg != null)
            frm.addError(msg);
        return frm;
    }

    public class FrmLogin {

        @NotEmpty
        @Email
        public String email;

        @NotEmpty
        @Input(type = Input.Type.Password)
        public String password;
        public Boolean saveMe;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Logout">
    @Action()
    public Object logout(IActionRequest request, IActionSession session) {

        securityManager.clearAuthentication(session);

        String url = request.getParameter("return", null);
        if (url == null)
            url = urlBuilder.buildBundle("/", IUrlBuilder.Mode.SHORT);
        return new RedirectView(url);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="profile">
    @Action(member = true)
    public Object profile(IActionRequest request, IActionSession session) {
        return securityManager.getAuthentication(session);
    }

    
    //</editor-fold>

}

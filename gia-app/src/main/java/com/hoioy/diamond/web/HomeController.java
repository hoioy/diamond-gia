package com.hoioy.diamond.web;

import com.hoioy.diamond.common.dto.ResultDTO;
import com.hoioy.diamond.common.service.CommonSecurityService;
import com.hoioy.diamond.domain.User;
import com.hoioy.diamond.dto.MenuDTO;
import com.hoioy.diamond.dto.UserDTO;
import com.hoioy.diamond.oauth2.config.GIAConfig;
import com.hoioy.diamond.oauth2.dto.OauthClientDetailsDTO;
import com.hoioy.diamond.oauth2.service.IOauthClientDetailsService;
import com.hoioy.diamond.oauth2.service.IRoleClientService;
import com.hoioy.diamond.oauth2.support.DBAuthenticationProvider;
import com.hoioy.diamond.service.MenuService;
import com.hoioy.diamond.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.*;


/**
 * @author chixue
 */
@Controller
@SessionAttributes("authorizationRequest")
public class HomeController {

    @Autowired
    private MenuService menuService;
    @Autowired
    private IOauthClientDetailsService oauthClientDetailsService;

    @Autowired
    private IRoleClientService iRoleClientService;

    @Autowired
    private HttpSession session;

    @Autowired
    private UserService userService;

    @Autowired
    private DBAuthenticationProvider dbAuthenticationProvider;

    //???????????????,???/exit???????????????????????????????????????????????????
    @RequestMapping("oauth/exit")
    public void exitbak(HttpServletRequest request, HttpServletResponse response) {
        // token can be revoked here if needed
        new SecurityContextLogoutHandler().logout(request, response, null);
        try {
            //sending back to client app
            response.sendRedirect(request.getHeader("referer"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ???????????????,?????????root????????????????????????menus???????????????
     *
     * @return
     */
    @GetMapping(value = {"/", "/index"})
    public String home(Model model, HttpServletRequest request) throws IOException {
        List<OauthClientDetailsDTO> clients = new ArrayList();
        Set<String> roleNames = CommonSecurityService.instance.getCurrentAuthorities();
        if (!CollectionUtils.isEmpty(roleNames)) {
            List<String> clientIds = iRoleClientService.findFirstIdsBySecondIds(new ArrayList<>(roleNames));
            clients = oauthClientDetailsService.findByIds(clientIds);
        }

        model.addAttribute("clients", clients);
        return "index";
    }

    private UserDTO currentUser(HttpServletRequest request) {
        String loginName = CommonSecurityService.instance.getCurrentLoginName();
        UserDTO userDTO = userService.findByLoginName(loginName);
        return userDTO;
    }

    /**
     * @param model
     * @param request
     * @return
     * @throws Exception String
     * @throws
     * @Description: ??????
     * @author chixue
     * @date 2017???7???10???
     */
    @RequestMapping("/custom/oauth/confirm_access")
    public String getAccessConfirmation(Map<String, Object> model, @ModelAttribute AuthorizationRequest authorizationRequest,
                                        HttpServletRequest request) throws Exception {
        SecurityContext context = SecurityContextHolder.getContext();
        WebAuthenticationDetails details = (WebAuthenticationDetails) context.getAuthentication().getDetails();

        if (request.getAttribute("_csrf") != null) {
            model.put("_csrf", request.getAttribute("_csrf"));
        }

        //??????
        String clientID = authorizationRequest.getClientId();
        OauthClientDetailsDTO dto = (OauthClientDetailsDTO) this.oauthClientDetailsService.findById(clientID);
        model.put("client", dto);
        return "auth";
    }

    @GetMapping(value = "/admin")
    public String admin(Model model, HttpServletRequest request, Principal user) {
        UserDTO userDTO = currentUser(request);
        if (userDTO != null) {
            if (userDTO.getId().equals("6613831cac9e4597abbd0138116a8f3c")) {
                model.addAttribute("avatar", userDTO.getAvatar());
                List<MenuDTO> menus = menuService.findMenuByMark("sys");
                model.addAttribute("rootMenus", menus);
                return "admin";
            }
        }

        model.addAttribute("msg", "??????????????????????????????");
        model.addAttribute("authentication", user);
        return "error";
    }

    @GetMapping(value = "/changePassword")
    public String changePassword(Model model, Principal principal, Authentication authentication, UsernamePasswordAuthenticationToken token) {
        // OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
        // Authentication authentication = oAuth2Authentication.getUserAuthentication();
        if (!CollectionUtils.isEmpty(authentication.getAuthorities())) {
            for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                if ((GIAConfig.providerTypeAuthorityName + GIAConfig.ProviderType.DB)
                        .equals(grantedAuthority.getAuthority())) {
                    model.addAttribute("authentication", principal);
                    return "changePassword";
                }
            }
        }
        model.addAttribute("msg", "????????????????????????????????????????????????,??????????????????????????????????????????");
        return "error";
    }

    @PostMapping(value = "/changePassword")
    @ResponseBody
    public ResultDTO changePassword(@RequestParam("id") String id, @RequestParam("password") String password
            , @RequestParam("newPassword") String newPassword) {
        ResultDTO result = dbAuthenticationProvider.changePassword(id, password, newPassword);
        return result;
    }


    /**
     * ???????????????
     *
     * @param model
     * @return
     */
    @GetMapping(value = "/authorization-menu")
    public String authorizationMenu(Model model) {
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user != null) {
            List<MenuDTO> menus = menuService.findRootByAuthorization(user.getRoles());
            model.addAttribute("menuTree", menus);
        }
        return "index";//semantic
    }

    /**
     * ???????????????????????????menu
     *
     * @param model
     * @param rootId
     * @return
     */
    @GetMapping("/authorization-menu-byRoot")
    public String authorizationMenuByRootId(Model model, @RequestParam(value = "rootId", required = false, defaultValue = "") String rootId) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            List<MenuDTO> menus = menuService.findMenuByRootId(rootId);
            model.addAttribute("menus", menus);
        }
        return "layout/left";//semantic
    }

    /**
     * @param model
     * @param mark
     * @return String
     * @throws
     * @Description: ?????????????????????????????????????????????????????????
     * @author chixue
     * @date 2016???5???9???
     */
    @GetMapping("/authorization-menu-mark")
    public String authorizationMenuByMark(Model model, @RequestParam(value = "mark", required = false, defaultValue = "") String mark) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            List<MenuDTO> menus = menuService.findMenuByMark(mark);
            model.addAttribute("menus", menus);
        }
        return "layout/left";
    }


//    /**
//     * @param user
//     * @return
//     */
//    // for 403 access denied page
//    @GetMapping("/403")
//    public String accesssDenied(Model model, Principal user) {
//        if (user == null) {
//            model.addAttribute("msg", "Hi you do not have permission to access this page!");
//        } else {
//            model.addAttribute("msg", "Hi " + user.getName()
//                    + ", You do not have permission to access this page");
//        }
//        model.addAttribute("authentication", user);
//        return "403";
//    }

//    /**
//     * @param request
//     * @param response
//     * @return
//     */
//    @GetMapping(value = "/image")
//    public String image(HttpServletRequest request, HttpServletResponse response) {
//
//        BufferedImage img = new BufferedImage(68, 22, BufferedImage.TYPE_INT_RGB);
//        // ??????????????????????????????
//        Graphics g = img.getGraphics();
//        Random r = new Random();
//        Color c = new Color(255, 255, 255);
//        g.setColor(c);
//        // ???????????????????????????
//        g.fillRect(0, 0, 68, 22);
//        // ?????????????????????????????????
//        StringBuffer sb = new StringBuffer();
//        char[] ch = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
//        int index, len = ch.length;
//        for (int i = 0; i < 4; i++) {
//            index = r.nextInt(len);
//            g.setColor(new Color(r.nextInt(88), r.nextInt(188), r.nextInt
//                    (255)));
//            g.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 22));
//            // ?????????  ???????????????
//            g.drawString("" + ch[index], (i * 15) + 3, 18);
//            //??????????????????????????? ??????????????????
//            sb.append(ch[index]);
//        }
//        request.getSession().setAttribute("j_captcha", sb.toString());
//        try {
//            response.setHeader("content-type", "image/jpeg");
//            response.setContentType("image/jpeg");
//            response.setHeader("Param", "no-cache");
//            response.setHeader("Cache-Control", "no-cache");
//            response.setIntHeader("Expries", -1);
//
//            ImageIO.write(img, "jpg", response.getOutputStream());
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return null;
//    }

}
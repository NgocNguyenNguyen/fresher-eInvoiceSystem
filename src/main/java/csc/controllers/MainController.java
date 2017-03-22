package csc.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import csc.repository.UserRepository;

@Controller
public class MainController {
	private static final String ISACTIVE = "1";
	@Autowired
	UserRepository userRepository;
	
	@RequestMapping(value = "/") // , method = RequestMethod.GET
	public String index() {
		return "index";
	}

	@RequestMapping("/admin")
	public String admin() {
		return "admin";
	}

	@RequestMapping("/403")
	public String accessDenied() {
		return "login";
	}

	@RequestMapping("/404")
	public String pageNotFound() {
		return "index";
	}

	@ExceptionHandler(Exception.class)
	public String globalExceptionHandler(Exception e) {
		// ModelAndView modelAndView = new ModelAndView("error");
		// modelAndView.addObject("message", e.getMessage());
		// return modelAndView;
		return "index";
	}

	@RequestMapping("/login")
	public String getLogin() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		System.out.println("auth.getAuthorities():" +auth.getAuthorities()); 
		if (username != "anonymousUser") {
			String role = userRepository.findByUsername(username).getActive();
			System.out.println(role);
			if (role.equals(ISACTIVE)) {
				System.out.println("go on login " + username);
				return "dashboard";
			}
		}
		return "login";
	}  
	  // Login form with error
	  @RequestMapping("/login-error.html")
	  public String loginError(Model model) {
		  model.addAttribute("loginError", true);
		  return "login";
	  }

	@RequestMapping("/logout")
	public String getLogout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			System.out.println("go on logout " + auth.getName());
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout";
	}

	@RequestMapping(value = "/home") // , method = RequestMethod.GET
	public String home() {
		return "index";
	}

	@RequestMapping(value = "/register") // , method = RequestMethod.GET
	public String register() {
		return "register";
	}

	@RequestMapping("/404.html")
	public String render404() {
		// Add model attributes
		return "login";
	}

	@RequestMapping("/dashboard")
	public String dashboard() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		if (username != "anonymousUser") {
			String role = userRepository.findByUsername(username).getActive();
			System.out.println(role);
			if (role.equals(ISACTIVE)) {
				System.out.println("go on dashboard " + username);
				return "dashboard";
			}
		}
		return "login";
	}
}

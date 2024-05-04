package vn.hoidanit.laptopshop.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.UserService;

@Controller
public class UserController {

	final private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping("/")
	public String getHomePage(Model model) {
		String test = this.userService.handleHello();
		List<User> userByEmail = this.userService.getAllUserByEmail("1@gmail.com");
		System.out.println(userByEmail);
		model.addAttribute("eric", test);
		model.addAttribute("demo", "Đây là câu demo");
		return "hello";
	}

	@RequestMapping("/admin/user")
	public String getUserPage(Model model) {
		List<User> users = this.userService.getAllUser();
		model.addAttribute("users", users);
		return "/admin/user/table-user";
	}

	@RequestMapping("/admin/user/{id}")
	public String getUserDetailPage(Model model, @PathVariable long id) {
		System.out.println("ID: " + id);
		User user = this.userService.getUserById(id);
		model.addAttribute("user", user);
		return "/admin/user/show";
	}

	@RequestMapping("/admin/user/create")
	public String getAdminCreatePage(Model model) {
		model.addAttribute("newUser", new User());
		return "/admin/user/create";
	}

	@RequestMapping(value = "/admin/user/create", method = RequestMethod.POST)
	public String createUser(Model model, @ModelAttribute("newUser") User hoidanit) {
		System.out.println("create user" + hoidanit);
		this.userService.handleSaveUser(hoidanit);
		return "redirect:/admin/user";
	}

	@RequestMapping("/admin/user/update/{id}")
	public String getUpdatePage(Model model, @PathVariable long id) {
		User user = this.userService.getUserById(id);
		System.out.println("Current User: " + user);
		model.addAttribute("newUser", user);
		return "/admin/user/update";
	}

	@RequestMapping(value = "/admin/user/update", method = RequestMethod.POST)
	public String postUpdateUser(Model model, @ModelAttribute("newUser") User user) {
		User currentUser = this.userService.getUserById(user.getId());
		System.out.println("Current user: " + currentUser);
		if (currentUser != null) {
			currentUser.setFullName(user.getFullName());
			currentUser.setAddress(user.getAddress());
			currentUser.setPhone(user.getPhone());
			this.userService.handleSaveUser(currentUser);
		}

		return "redirect:/admin/user";
	}
	
	@GetMapping("/admin/user/delete/{id}")
	public String getDeleteUserPage(Model model, @PathVariable long id) {
		User user = this.userService.getUserById(id);
		model.addAttribute("newUser", user);
		return "/admin/user/delete";
	}

	@RequestMapping(value = "/admin/user/delete", method = RequestMethod.POST)
	public String postDeleteUser(Model model, @ModelAttribute("newUser") User user) {
		this.userService.deleteUserById(user.getId());
		return "redirect:/admin/user";
	}
}

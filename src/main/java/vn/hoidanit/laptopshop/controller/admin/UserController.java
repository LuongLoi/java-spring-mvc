package vn.hoidanit.laptopshop.controller.admin;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletContext;
import jakarta.validation.Valid;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.UploadService;
import vn.hoidanit.laptopshop.service.UserService;

@Controller
public class UserController {

	private final UserService userService;
	private final ServletContext servletContext;
	private final UploadService uploadService;
	private final PasswordEncoder passwordEncoder;

	public UserController(UserService userService, ServletContext servletContext,
						UploadService uploadService, PasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.servletContext = servletContext;
		this.uploadService = uploadService;
		this.passwordEncoder = passwordEncoder;
	}

	// @RequestMapping("/")
	// public String getHomePage(Model model) {
	// 	String test = this.userService.handleHello();
	// 	List<User> userByEmail = this.userService.getAllUserByEmail("1@gmail.com");
	// 	System.out.println(userByEmail);
	// 	model.addAttribute("eric", test);
	// 	model.addAttribute("demo", "Đây là câu demo");
	// 	return "hello";
	// }

	@RequestMapping("/admin/user")
	public String getUserPage(Model model) {
		List<User> users = this.userService.getAllUser();
		model.addAttribute("users", users);
		return "/admin/user/show";
	}

	@RequestMapping("/admin/user/{id}")
	public String getUserDetailPage(Model model, @PathVariable long id) {
		System.out.println("ID: " + id);
		User user = this.userService.getUserById(id);
		model.addAttribute("user", user);
		return "/admin/user/detail";
	}

	@RequestMapping("/admin/user/create")
	public String getAdminCreatePage(Model model) {
		model.addAttribute("newUser", new User());
		return "/admin/user/create";
	}

	@PostMapping("/admin/user/create")
	public String createUser(Model model,
			@ModelAttribute("newUser") @Valid User hoidanit,
			BindingResult newUserBindingResult ,
			@RequestParam("hoidanitFile") MultipartFile file
			
	) {

		List<FieldError> errors = newUserBindingResult.getFieldErrors();
		for (FieldError error : errors) {
			System.out.println(error.getField() + " - " + error.getDefaultMessage());
		}

		if (newUserBindingResult.hasErrors()) {
			return "/admin/user/create";
		}

		String avatar = this.uploadService.handleUploadFileSave(file, "avatar");
		String hashPassword = this.passwordEncoder.encode(hoidanit.getPassword());
		hoidanit.setAvatar(avatar);
		hoidanit.setPassword(hashPassword);
		hoidanit.setRole(this.userService.getRoleByName(hoidanit.getRole().getName()));
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

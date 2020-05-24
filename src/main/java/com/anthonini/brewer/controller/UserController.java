package com.anthonini.brewer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anthonini.brewer.controller.page.PageWrapper;
import com.anthonini.brewer.model.User;
import com.anthonini.brewer.repository.UserGroupRepository;
import com.anthonini.brewer.repository.UserRepository;
import com.anthonini.brewer.repository.filter.UserFilter;
import com.anthonini.brewer.service.UserService;
import com.anthonini.brewer.service.UserStatus;
import com.anthonini.brewer.service.exception.UserEmailAlreadyRegisteredException;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserGroupRepository userGroupRepository;
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/new")
	public ModelAndView create(User user) {
		ModelAndView mv = new ModelAndView("user/form");
		mv.addObject("userGroups", userGroupRepository.findAll());
		
		return mv;
	}
	
	@PostMapping("/new")
	public ModelAndView save(@Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if(bindingResult.hasErrors()) {
			return create(user);
		}
		
		try {
			userService.save(user);
			redirectAttributes.addFlashAttribute("successMessage", "User succesffuly saved!");
			return new ModelAndView("redirect:new");
		}catch (UserEmailAlreadyRegisteredException e) {
			bindingResult.rejectValue("email", e.getMessage(), e.getMessage());
			return create(user);
		}
	}

	@GetMapping
	public ModelAndView list(UserFilter userFilter, BindingResult bindingResult,
			@PageableDefault(size = 5) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("user/list");
		mv.addObject("userGroups", userGroupRepository.findAll());
		
		PageWrapper<User> pageWrapper = new PageWrapper<>(userRepository.filter(userFilter, pageable), httpServletRequest);
		mv.addObject("page", pageWrapper);
		
		return mv;
	}
	
	@PutMapping("/status")
	@ResponseStatus(HttpStatus.OK)
	public void updateStatus(@RequestParam("ids[]") Long[] ids, @RequestParam("status") UserStatus userStatus) {
		userService.updateStatus(ids, userStatus);
	}
}

package com.lavacro.songlist.controllers;

import com.lavacro.songlist.repository.ActiveServicesRepository;
import com.lavacro.songlist.repository.ServicesRepository;
import com.lavacro.songlist.model.ActiveService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Controller
public class OldPages {
	private static final Logger logger = LoggerFactory.getLogger(OldPages.class);

	private final ServicesRepository servicesRepository;
	private final ActiveServicesRepository activeServicesRepository;

	@Autowired
	public OldPages(ServicesRepository servicesRepository, ActiveServicesRepository activeServicesRepository) {
		this.servicesRepository = servicesRepository;
		this.activeServicesRepository = activeServicesRepository;
	}

	@GetMapping(path = {"/calendar_old"})
	public String calendar(Model model) {
		return "calendar_old";
	}

	@GetMapping(path = {"/makelist_old"})
	public String makeList(Model model, @RequestParam("id") Integer id) {
		model.addAttribute("service", activeServicesRepository.getOneService(id));
		return "make_list_old";
	}

	@PostMapping(path = {"/newlist_old"})
	public String newList(
			Model model,
			@RequestParam("month") Integer month,
			@RequestParam("day") Integer day,
			@RequestParam("year") Integer year,
			@RequestParam("service") Integer serviceId,
			@RequestParam("hour") Integer hour,
			@RequestParam("min") Integer minute
	) {
		logger.info("month={}, day={}, year={}, service={}", month, day, year, serviceId);
		ActiveService service = new ActiveService();
		LocalDate ld = LocalDate.of(year, month, day);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM d, y");
		String formatted = ld.format(dtf);
		service.setFormattedDate(formatted);
		service.setId(0);
		service.setService(Objects.requireNonNull(servicesRepository.findById(serviceId).orElse(null)).getDescription());
		model.addAttribute("service", service);
		model.addAttribute("month", month);
		model.addAttribute("day", day);
		model.addAttribute("year", year);
		model.addAttribute("serviceType", serviceId);
		model.addAttribute("hour", hour);
		model.addAttribute("minute", minute);
		return "make_list_old";
	}
}

package com.lavacro.songlist.controllers;

import com.lavacro.songlist.repository.ServicesRepository;

import com.lavacro.songlist.service.ServicesService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
@Slf4j
public class OldPages {
	private final ServicesRepository servicesRepository;
	private final ServicesService servicesService;

	private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEE, MMM dd, yyyy");

	public OldPages(ServicesRepository servicesRepository, ServicesService servicesService) {
		this.servicesRepository = servicesRepository;
		this.servicesService = servicesService;
	}

	@GetMapping(path = {"/calendar_old"})
	public String calendar(Model model) {
		return "calendar_old";
	}

	@GetMapping(path = {"/makelist_old"})
	public String makeList(Model model, @RequestParam("id") Integer id) {
		model.addAttribute("service", servicesService.getActiveServiceById(id));
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
		log.info("month={}, day={}, year={}, service={}", month, day, year, serviceId);
		LocalDate ld = LocalDate.of(year, month, day);
		String formatted = ld.format(dtf);

		String serviceName = Objects.requireNonNull(servicesRepository.findById(serviceId).orElse(null)).getDescription();

		Map<String, Object> service = new HashMap<>();
		service.put("formattedDateTime", formatted);
		service.put("id", "0");
		service.put("service", Map.of("description", serviceName));

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

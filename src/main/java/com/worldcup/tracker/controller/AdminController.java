package com.worldcup.tracker.controller;

import com.worldcup.tracker.repository.UserRepository;
import com.worldcup.tracker.service.MatchService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    private final UserRepository userRepository;
    private final MatchService matchService;

    AdminController(UserRepository userRepository, MatchService matchService){
        this.userRepository = userRepository;
        this.matchService = matchService;
    }

    @GetMapping
    public String dashboard(Model model){
        model.addAttribute("matches", matchService.getAllMatches());
        model.addAttribute("groups", matchService.getDistinctGroups());
        return "admin/dashboard";
    }

    @GetMapping("/matches/create")
    public String createMatchForm(Model model){
        model.addAttribute("groups", List.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"));

        return "admin/matches/create";
    }

    @PostMapping("/matches/create")
    public String createMatch(
            @RequestParam String homeTeam,
            @RequestParam String awayTeam,
            @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime kickOffTime,
            @RequestParam String groupName) {

        matchService.createMatch(homeTeam, awayTeam, kickOffTime, groupName);
        return "redirect:/admin?success=created";
    }

    // Show edit match form
    @GetMapping("/matches/{id}/edit")
    public String editMatchForm(@PathVariable Long id, Model model) {
        model.addAttribute("match", matchService.getMatchById(id));
        model.addAttribute("groups", List.of(
                "A", "B", "C", "D", "E", "F",
                "G", "H", "I", "J", "K", "L"));
        return "admin/matches/edit";
    }

    // Handle single form submission for all match fields
    @PostMapping("/matches/{id}/edit")
    public String updateMatch(
            @PathVariable Long id,
            @RequestParam String homeTeam,
            @RequestParam String awayTeam,
            @RequestParam @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime kickOffTime,
            @RequestParam String groupName,
            @RequestParam String status,
            @RequestParam(required = false) Integer homeScore,
            @RequestParam(required = false) Integer awayScore) {

        try {
            matchService.updateMatch(id, homeTeam, awayTeam,
                    kickOffTime, groupName, status,
                    homeScore, awayScore);
            return "redirect:/admin?success=updated";
        } catch (Exception e) {
            return "redirect:/admin/matches/" + id
                    + "/edit?error=true";
        }
    }

    @PostMapping("/matches/{id}/delete")
    public String deleteMatch(@PathVariable Long id){
        try {
            matchService.deleteMatch(id);
            return "redirect:/admin?success=deleted";
        } catch (IllegalStateException e) {
            return "redirect:/admin/matches/" + id + "/edit?error=true";
        }
    }
}

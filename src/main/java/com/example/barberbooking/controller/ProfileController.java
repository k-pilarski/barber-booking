package com.example.barberbooking.controller;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
public class ProfileController {

    private final Environment env;

    public ProfileController(Environment env) {
        this.env = env;
    }

    @GetMapping("/profile-info")
    public Map<String, String> getProfileInfo() {
        Map<String, String> info = new HashMap<>();

        String[] activeProfiles = env.getActiveProfiles();
        String activeProfile = activeProfiles.length > 0 ? activeProfiles[0] : "undefined";

        String[] defaultProfiles = env.getDefaultProfiles();
        String defaultProfile = defaultProfiles.length > 0 ? defaultProfiles[0] : "undefined";

        String serviceName = env.getProperty("servicename", "undefined");

        info.put("activeProfile", activeProfile);
        info.put("allActiveProfiles", activeProfiles.length > 0 ? String.join(",", activeProfiles) : "");
        info.put("defaultProfile", defaultProfile);
        info.put("serviceName", serviceName);

        log.info("Profile info requested: {}", info);

        return info;
    }
}

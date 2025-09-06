package com.likelionsg13th.cardinal.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.oauth2")
public class OAuth2RedirectProps {
    private List<String> allowedRedirectUris;
    private String defaultRedirectUri;

    public boolean isAllowed(String uri) {
        if (uri == null || allowedRedirectUris == null) return false;
        return allowedRedirectUris.stream().anyMatch(uri::startsWith);
    }
}

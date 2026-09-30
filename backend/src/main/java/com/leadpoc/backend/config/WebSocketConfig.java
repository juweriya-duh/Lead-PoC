package com.leadpoc.backend.config;

import com.leadpoc.backend.websocket.LeadWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final LeadWebSocketHandler leadWebSocketHandler;

    public WebSocketConfig(LeadWebSocketHandler leadWebSocketHandler) {
        this.leadWebSocketHandler = leadWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(leadWebSocketHandler, "/ws/leads").setAllowedOrigins("*");
    }
}

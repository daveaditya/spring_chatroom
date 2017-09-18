package com.spring.chatroom.config;

/*@Configuration
@EnableWebSocketMessageBroker*/
/*
@SuppressWarnings({"unused"})
public class MyMessageBrokerConfigurer extends AbstractWebSocketMessageBrokerConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyMessageBrokerConfigurer.class);

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        stompEndpointRegistry.addEndpoint("/chat").setAllowedOrigins("*");
        stompEndpointRegistry.addEndpoint("/chat").setAllowedOrigins("*").withSockJS();
    }

}*/

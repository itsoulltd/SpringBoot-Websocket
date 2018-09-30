package com.example.lab.config;

import com.itsoul.lab.client.GeoTracker;
import com.itsoul.lab.client.WebResource;
import com.itsoul.lab.interactor.factory.Interactors;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.util.ResourceBundle;

@Component
public class ContextRefreshEventHandler {

    @EventListener({ContextRefreshedEvent.class})
    public void contextRefreshed(){
        GeoTracker.shared().loadProperties(ResourceBundle.getBundle("application"));
        System.out.println("ContextRefreshedEvent:" + WebResource.API_GATEWAY.value());
    }

}

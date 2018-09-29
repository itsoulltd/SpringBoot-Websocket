package com.example.lab.config;

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
        Interactors.loadProperties(ResourceBundle.getBundle("application"));
        try {
            System.out.println("ContextRefreshedEvent:" + Interactors.getWebResource(Interactors.WebResource.API_GATEWAY));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}

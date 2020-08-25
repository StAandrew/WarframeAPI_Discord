package com.example.api;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

public class Main extends ListenerAdapter {
    private static Fetcher fetcher;

    public static void main(String[] args) throws LoginException {
        String token = "";
        String apiUrl = "https://api.warframestat.us/pc/invasions";
        fetcher = new Fetcher(apiUrl);
        JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT);
        jdaBuilder.setToken(token);
        jdaBuilder.addEventListener(new Main());
        jdaBuilder.buildAsync();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent messageReceivedEvent) {
        if (messageReceivedEvent.getAuthor().isBot()) {
            return;
        }
        System.out.println("Got message from " +
                messageReceivedEvent.getAuthor().getName() +
                messageReceivedEvent.getMessage().getContentDisplay());
        if (messageReceivedEvent.getMessage().getContentRaw().equals("ping")) {
            messageReceivedEvent.getChannel().sendMessage("pong").queue();
        }
        if (messageReceivedEvent.getMessage().getContentRaw().equals("bot update")) {
            String result = null;
            try {
                result = fetcher.fetch().getUniqueItems().toString();
            } catch (Exception e) {
                e.printStackTrace();
                result = "Failed to fetch items";
            }
            messageReceivedEvent.getChannel().sendMessage(result).queue();
        }
    }
}
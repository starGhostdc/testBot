package com.minelandbot.testing.listeners;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public class EventListener_AntiSpamSystem extends ListenerAdapter {
    private Map<String, Integer> userMessageCount = new HashMap<>();
    private Map<String, Long> userLastMessageTimes = new HashMap<>();
    private ScheduledExecutorService cleanupScheduler;
    private static final int banMessageTime = 5;
    private static final long messageExpireTime = 3000;

    public EventListener_AntiSpamSystem() {
        cleanupScheduler = Executors.newSingleThreadScheduledExecutor();
        ScheduledFuture<?> cleanupTask = cleanupScheduler.scheduleWithFixedDelay(this::cleanUpCache, 0, messageExpireTime, TimeUnit.MILLISECONDS);
    }

    // 10/14/2023 - Override.onMessageReceived.
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User user = event.getAuthor();

        if (user.isBot()) {
            return;
        }

        String userId = user.getId();
        long currentTime = System.currentTimeMillis();

        if (userLastMessageTimes.containsKey(userId)) {
            long lastMessageTime = userLastMessageTimes.get(userId);
            long timeDifference = currentTime - lastMessageTime;

            if (timeDifference < messageExpireTime && userMessageCount.getOrDefault(userId, 0) > banMessageTime) {
                // Bans the user.
                event.getGuild().ban(user, 0, TimeUnit.DAYS).queue();
            } else {
                // Increments the cache count by 1.
                userMessageCount.put(userId, userMessageCount.getOrDefault(userId, 0) + 1);
            }
        }

        userLastMessageTimes.put(userId, currentTime);
    }

    private void cleanUpCache() {
        long currentTime = System.currentTimeMillis();
        userMessageCount.entrySet().removeIf(entry -> (currentTime - userLastMessageTimes.getOrDefault(entry.getKey(), 0L) >= messageExpireTime));
        userLastMessageTimes.keySet().removeIf(userId -> (currentTime - userLastMessageTimes.getOrDefault(userId, 0L) >= messageExpireTime));
    }
}
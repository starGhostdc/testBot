package com.minelandbot.testing;

import com.minelandbot.testing.listeners.EventListener_AntiSpamSystem;
import com.minelandbot.testing.listeners.EventListener_WelcomeSenderSystem;
import com.minelandbot.testing.listeners.commands.SlashCommand_EnCheckerSystem;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

public class testBotMineland {

    public ShardManager shardManager;

    public testBotMineland(String[] args) {

        String token = "Replance - TOKEN";
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        shardManager = builder.build();

        shardManager.addEventListener(
            new EventListener_AntiSpamSystem(),
            new EventListener_WelcomeSenderSystem(),
            new SlashCommand_EnCheckerSystem());
    }

    public static void main(String[] args) {
        testBotMineland bot = new testBotMineland(args);
    }
}
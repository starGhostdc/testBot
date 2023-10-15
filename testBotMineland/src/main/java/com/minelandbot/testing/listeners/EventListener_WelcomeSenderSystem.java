package com.minelandbot.testing.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.Color;

public class EventListener_WelcomeSenderSystem extends ListenerAdapter {

    // 10/14/2023 - Override.onGuildMemberJoin.
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        User user = event.getUser();
        String welcomeMessage = "We expect you to follow the Rules imposed by our servers before you finally join us. " +
            "\n**For more information please refer below:** " +
            "\n**ю** Discord ToS - https://discordapp.com/terms " +
            "\n**ю** Discord Rules - https://discord.com/channels/310217885203300358/369095328261210112 " +
            "\n**ю** Flagged words - https://docs.google.com/document/d/1dP4UBnF2PSpE23S5hrUubh-SI80sHLj5PgZmsi-2bU0/edit?usp=sharing " +
            "\n**ю** Forum Rules - https://forum.mineland.net/t/forum-site-rules/3518";

        if (user.hasPrivateChannel()) {
            // Sends a welcome message in the user Direct Messages as an embed.
            sendWelcomeMessage(user, welcomeMessage);
        } else {
            // Sends the message to the specified server channel.
            sendWelcomeMessage(event, welcomeMessage);
        }
    }

    private void sendWelcomeMessage(User user, String welcomeMessage) {
        EmbedBuilder embed = new EmbedBuilder()
            .setColor(Color.GREEN)
            .setTitle("**System** » Welcome!")
            .setDescription(welcomeMessage)
            .setFooter("- Enjoy your stay!");

        user.openPrivateChannel()
            .flatMap(privateChannel -> privateChannel.sendMessageEmbeds(embed.build()))
            .queue();
    }

    private void sendWelcomeMessage(GuildMemberJoinEvent event, String welcomeMessage) {
        String serverChannelId = "Replace - Server ChannelID";
        EmbedBuilder embed = new EmbedBuilder()
            .setColor(Color.GREEN)
            .setTitle("**System** » Welcome!")
            .setDescription(welcomeMessage)
            .setFooter("- Enjoy your stay!");

        event.getGuild().getTextChannelById(serverChannelId)
            .sendMessageEmbeds(embed.build())
            .queue();
    }
}
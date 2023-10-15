package com.minelandbot.testing.listeners.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashCommand_EnCheckerSystem extends ListenerAdapter {

    // 10/15/23 Override.onSlashCommandInteraction.
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("corecheck")) {
            Member member = event.getMember();
            if (member != null && isCoreStaff(member)) {
                event.deferReply(true).queue(hook -> {
                    int roleAssignCount = executeCoreCheckLogic(event.getGuild());

                    event.getHook().sendMessage("** **\n**System** » Added " + roleAssignCount + " Member role(s) to users.\n** **").setEphemeral(false).queue();
                });
            } else {
                event.reply("** **\n**System** » You do not have permission to execute this command!\n** **").setEphemeral(true).queue();
            }
        }
    }

    private int executeCoreCheckLogic(net.dv8tion.jda.api.entities.Guild guild) {
        Role memberRoleId = guild.getRoleById("Replace - Member RoleID");
        if (memberRoleId == null) return 0;

        int roleAssignCount = 0;

        for (Member member : guild.getMembers()) {
            if (!member.getRoles().contains(memberRoleId)) {
                guild.addRoleToMember(member, memberRoleId).queue();
                roleAssignCount++;
            }
        }

        return roleAssignCount;
    }

    private boolean isCoreStaff(Member member) {
        for (Role role : member.getRoles()) {
            String coreStaffRoleId = "Replace - Core Staff RoleID";

            if (role.getId().equals(coreStaffRoleId)) {
                return true;
            }
        }
        return false;
    }
}
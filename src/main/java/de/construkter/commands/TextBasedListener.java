package de.construkter.commands;

import de.construkter.modules.uptimeMonitor.Uptimes;
import de.construkter.utils.FakeJSONResponse;
import de.construkter.utils.Logger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Objects;

public class TextBasedListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        switch (event.getMessage().getContentRaw()) {
            case "!stop":
                stop(event);
                break;
            case "!debug-info":
                debug(event);
                break;
            case "!repeat":
                String message = event.getMessage().getContentRaw();
                repeat(event, message);
                break;
            case "!spam":
                spam(event);
                break;
        }
        Uptimes.runTask(event);
    }

    private static void stop(MessageReceivedEvent event) {
        if (event.getAuthor().getName().equalsIgnoreCase("construkter")) {
            event.getMessage().delete().queue();
            Logger.event("Stopping...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Logger.event("Failed to stop");
            }
            System.exit(130);
        } else {
            event.getMessage().delete().queue();
            Logger.event(event.getAuthor().getName() + " versuchte zu stoppen. Response: "  + FakeJSONResponse.setResponse(500, "error", "Missing Permission: de.construkter.permissions.zimblu.bot.poweroff"));
        }
    }

    private static void debug(MessageReceivedEvent event) {
        if (event.getAuthor().getName().equalsIgnoreCase("construkter")) {
            Message msg = event.getMessage();
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Debug-Infos");
            eb.setDescription("RAM Usage: " + SlashCommandListener.getRAM() + "MB / 512MB\n" +
                    "Status: " + event.getJDA().getStatus() + "\n" +
                    "Gateway Ping: " + event.getJDA().getGatewayPing() + "ms \n" +
                    "Enabled Intents: " + event.getJDA().getGatewayIntents());
            eb.setFooter(msg.getAuthor().getName(), msg.getAuthor().getAvatarUrl());
            eb.setColor(Color.BLUE);
            msg.replyEmbeds(eb.build()).queue();
        }
    }

    private static void repeat(MessageReceivedEvent event, String message) {
        message = message.substring(0, 7);
        if (event.getAuthor().getName().equalsIgnoreCase("construkter") || Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
            event.getMessage().delete().queue();
            event.getChannel().sendMessage(message).queue();
        } else {
            event.getMessage().delete().queue();
        }
    }

    private static void spam(MessageReceivedEvent event) {
        if (event.getAuthor().getName().equalsIgnoreCase("construkter")) {
            for (int i = 0; i < 100; i++) {
                event.getChannel().sendMessage("<@809550343436107846>").queue();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Logger.error("Failed to spam");
                }
            }
        }
    }
}

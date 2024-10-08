package de.construkter.events;

import de.construkter.ressources.BotConfig;
import de.construkter.utils.ColorManager;
import de.construkter.utils.Logger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class OnReady extends ListenerAdapter {
    BotConfig config = new BotConfig();
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        Logger.event("Bot is ready!");
        Logger.event(event.getJDA().getInviteUrl());
        System.out.println("");
        Logger.event("Servers: " + event.getJDA().getGuilds().size());
        Logger.event("Status: " + event.getJDA().getStatus());
        Logger.event(ColorManager.BOLD + "Loading Done!");

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("🧾 • ZimbluBot Logging");
        eb.setDescription("Bot loaded and is ready");
        eb.addField("Gateway-Ping", event.getJDA().getGatewayPing() + "ms", true);
        eb.addField("Intents", event.getJDA().getGatewayIntents().toString(), true);
        eb.addField("Shards", event.getJDA().getShardInfo().getShardString(), true);
        eb.addField("Users", event.getJDA().getUsers().size() + "", true);
        eb.addField("Guilds", event.getJDA().getGuilds().size() + "", true);
        eb.addField("Eingeloggt als" , event.getJDA().getSelfUser().getAsTag(), true);
        eb.setColor(Color.BLUE);
        eb.setFooter("\uD83E\uDD16 • ZimbluBot Logger", event.getJDA().getSelfUser().getAvatarUrl());
        TextChannel logChannel = event.getJDA().getTextChannelById(config.getProperty("logging-channel"));
        assert logChannel != null;
        logChannel.sendMessageEmbeds(eb.build()).queue();

        Guild guild = event.getJDA().getGuildById("1199634208524599347");
        assert guild != null;
        event.getJDA().getPresence().setActivity(Activity.watching(guild.getMembers().size() + " Members"));
    }
}
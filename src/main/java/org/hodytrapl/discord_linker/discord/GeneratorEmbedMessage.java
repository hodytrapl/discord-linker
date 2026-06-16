package org.hodytrapl.discord_linker.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.hodytrapl.discord_linker.config.events.EventEntryConfig;
import org.hodytrapl.discord_linker.utils.config.EventsConfigHelper;

import java.awt.*;
import java.util.List;

import static org.hodytrapl.discord_linker.utils.config.EventsConfigHelper.replaceHeadPlayer;

public class GeneratorEmbedMessage extends ListenerAdapter {

    public static MessageEmbed buildEmbed(EventEntryConfig event, String username) {
        EmbedBuilder builder = new EmbedBuilder();

        // Замена плейсхолдеров в текстовых полях
        String title = EventsConfigHelper.getEmbedTitle(event);
        String description = EventsConfigHelper.getEmbedDescription(event);
        String authorName = EventsConfigHelper.getEmbedAuthorName(event);
        String authorIcon = EventsConfigHelper.getEmbedAuthorIcon(event);
        String thumbnail = EventsConfigHelper.getEmbedThumbnail(event);
        String image = EventsConfigHelper.getEmbedImage(event);
        String footer = EventsConfigHelper.getEmbedFooter(event);
        String footerIcon = EventsConfigHelper.getEmbedFooterIcon(event);
        String colorStr = EventsConfigHelper.getEmbedColor(event);
        boolean timestamp = EventsConfigHelper.isEmbedTimestampEnabled(event);
        List<? extends String> fields = EventsConfigHelper.getEmbedFields(event);

        // Заменяем %username% в текстовых полях
        if (username != null) {
            title = EventsConfigHelper.replaceUsername(title, username);
            description = EventsConfigHelper.replaceUsername(description, username);
            authorName = EventsConfigHelper.replaceUsername(authorName, username);
            footer = EventsConfigHelper.replaceUsername(footer, username);
        }

        // Устанавливаем поля
        if (authorName != null && !authorName.isEmpty()) {
            builder.setAuthor(authorName, null, authorIcon != null && !authorIcon.isEmpty() ? authorIcon : null);
        }

        if (title != null && !title.isEmpty()) {
            builder.setTitle(title);
        }

        if (description != null && !description.isEmpty()) {
            builder.setDescription(description);
        }

        // Цвет
        if (colorStr != null && !colorStr.isEmpty()) {
            try {
                Color color = Color.decode(colorStr);
                builder.setColor(color);
            } catch (NumberFormatException e) {
                // Если цвет невалидный, пропускаем
            }
        }

        if (thumbnail != null && !thumbnail.isEmpty()) {
            builder.setThumbnail(thumbnail);
        }

        if (image != null && !image.isEmpty()) {
            if(image.equals("%headplayer%")&&username!=null&&!username.isEmpty()){
                image=replaceHeadPlayer(image,username);
            }
            builder.setImage(image);
        }

        if (footer != null && !footer.isEmpty()) {
            builder.setFooter(footer, footerIcon != null && !footerIcon.isEmpty() ? footerIcon : null);
        }

        if (timestamp) {
            builder.setTimestamp(java.time.Instant.now());
        }

        // Добавляем поля
        if (fields != null) {
            for (String fieldStr : fields) {
                String[] parts = fieldStr.split("\\|", 3);
                if (parts.length == 3) {
                    String name = parts[0];
                    String value = parts[1];
                    boolean inline = Boolean.parseBoolean(parts[2]);
                    // Заменяем плейсхолдеры в name и value
                    if (username != null) {
                        name = EventsConfigHelper.replaceUsername(name, username);
                        value = EventsConfigHelper.replaceUsername(value, username);
                    }
                    builder.addField(name, value, inline);
                }
            }
        }

        return builder.build();
    }
}

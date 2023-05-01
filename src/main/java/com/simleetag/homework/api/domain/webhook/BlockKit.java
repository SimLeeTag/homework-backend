package com.simleetag.homework.api.domain.webhook;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * https://api.slack.com/reference/block-kit
 */
public class BlockKit {

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXISTING_PROPERTY,
            property = "type",
            visible = true
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Context.class, name = "context"),
            @JsonSubTypes.Type(value = Header.class, name = "header"),
            @JsonSubTypes.Type(value = Section.class, name = "section"),
            @JsonSubTypes.Type(value = Divider.class, name = "divider"),
            @JsonSubTypes.Type(value = Image.class, name = "checkboxes"),
            @JsonSubTypes.Type(value = Text.class, name = "plain_text"),
            @JsonSubTypes.Type(value = Text.class, name = "mrkdwn"),
            @JsonSubTypes.Type(value = Button.class, name = "button"),
            @JsonSubTypes.Type(value = Checkbox.class, name = "checkboxes"),
            @JsonSubTypes.Type(value = Image.class, name = "image"),
    })
    public interface Block {

    }

    public interface BlockKitElement extends Block {

    }

    /**
     * https://api.slack.com/reference/block-kit#objects
     */
    public interface BlockKitObject extends Block {

    }

    /**
     * https://api.slack.com/reference/block-kit#blocks
     */
    public interface BlockKitBlock extends Block {

    }

    @AllArgsConstructor
    public static class Context implements BlockKitBlock {
        /**
         * Only images BlockElement and text CompositionObject are allowed.
         */
        public String type;
        public List<BlockKitObject> elements;
        public String blockId;

        public Context(List<BlockKitObject> elements) {
            this("context", elements, null);
        }
    }

    public record Header(
            Text text
    ) implements BlockKitBlock {
        public static String type = "header";
    }

    @AllArgsConstructor
    public static class Section implements BlockKitBlock {
        public String type;
        public Text text;
        public String blockId;
        public List<Text> fields;
        public BlockKitElement accessor;

        public Section(Text text) {
            this("section", text, null, null, null);
        }
    }

    public class Divider implements BlockKitBlock {
        public static String type = "divider";
    }

    public record Button(
            Text text,
            String actionId,
            String url,
            String value,
            Style style,
            ConfirmationDialog confirm,
            Text accessibilityLabel
    ) implements BlockKitElement {
        public static String type = "button";

        public enum Style {
            primary,
            danger
        }
    }

    public record Checkbox(
            String actionId,
            List<Option> options,
            List<Option> initialOptions,
            ConfirmationDialog confirm,
            Boolean focusOnLoad
    ) implements BlockKitElement {
        public static String type = "checkboxes";
    }

    public record Image(
            String imageUrl,
            String altText
    ) implements BlockKitElement, BlockKitObject {
        public static String type = "image";
    }

    public record Text(
            Type type,
            String text,
            Boolean emoji,
            Boolean verbatim
    ) implements BlockKitObject {
        public enum Type {
            plain_text,
            mrkdwn
        }

        public Text(Type type, String text) {
            this(type, text, null, null);
        }
    }

    public record ConfirmationDialog(
            Text title,
            Text text,
            Text confirm,
            Text deny,
            Style style
    ) implements BlockKitObject {
        public enum Style {
            danger,
            primary
        }
    }

    public record Option(
            Text text,
            String value
    ) implements BlockKitObject {

    }

    public record OptionGroup(
            Text label,
            List<Option> options
    ) implements BlockKitObject {

    }
}

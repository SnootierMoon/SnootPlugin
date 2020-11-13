package snoot.commands.parents;

import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import snoot.Colors;
import snoot.Main;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.BiConsumer;

public class SnootCommandExecutor implements CommandExecutor {

    static protected class HelpArg {

        final String name, description, format;
        final boolean required;

        public HelpArg(String name, String description, String format) {
            this.name = name;
            this.description = description;
            this.format = format;
            required = true;
        }

        public HelpArg(String name, String description, String format, boolean required) {
            this.name = name;
            this.description = description;
            this.format = format;
            this.required = required;
        }

        String bracketed() {
            if (required) {
                return "<" + name + ">";
            }
            return "[" + name + "]";
        }

    }

    static protected class SubCommand {
        private final String name, help, perms;
        private final List<HelpArg> helpArgs;
        private final Map<Integer,  BiConsumer<CommandSender, List<String>>> fns;
        private final boolean playerOnly;

        public SubCommand(String name, List<HelpArg> helpArgs, String help, String perms, Map<Integer, BiConsumer<CommandSender, List<String>>> fns) {
            this.name = name;
            this.helpArgs = helpArgs;
            this.help = help;
            this.perms = perms;
            this.fns = fns;
            this.playerOnly = false;
        }

        public SubCommand(String name, List<HelpArg> helpArgs, String help, String perms, Map<Integer, BiConsumer<CommandSender, List<String>>> fns, boolean playerOnly) {
            this.name = name;
            this.helpArgs = helpArgs;
            this.help = help;
            this.perms = perms;
            this.fns = fns;
            this.playerOnly = playerOnly;
        }

    }

    private final List<SubCommand> subCommands = new ArrayList<>();
    private final String help;
    private final String perms;
    private final boolean playerOnly;
    private final boolean hasSubCommands;

    private final List<HelpArg> helpArgs;
    private final Map<Integer,  BiConsumer<CommandSender, List<String>>> fns;

    private static final String desc = Colors.title + "Desc:";
    private static final String usage = Colors.title + "Usage:";

    private static final BaseComponent[] horizontalBar = new ComponentBuilder()
            .append("---- SnootPlugin Help ----")
            .bold(true)
            .color(Colors.titleColor.asBungee())
            .create();

    protected SnootCommandExecutor(String help, String perms, boolean playerOnly) {
        this.help = help;
        this.perms = perms;
        this.playerOnly = playerOnly;
        this.hasSubCommands = true;
        helpArgs = new ArrayList<>();
        fns = null;
    }

    protected SnootCommandExecutor(List<HelpArg> helpArgs, String help, String perms, Map<Integer, BiConsumer<CommandSender, List<String>>> fns, boolean playerOnly) {
        this.helpArgs = helpArgs;
        this.help = help;
        this.perms = perms;
        hasSubCommands = false;
        this.fns = fns;
        this.playerOnly = playerOnly;
    }

    protected void addSubCommand(SubCommand subCommand) {
        subCommands.add(subCommand);
    }

    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull final Command command, @Nonnull final String label, @Nonnull final String[] args) {
        if (args.length == 1 && args[0].equals("?")) {
            if (!hasSubCommands) {
                sender.spigot().sendMessage(horizontalBar);
                Colors.sendInfo(sender, desc, " ", help);
                sender.spigot().sendMessage(new ComponentBuilder()
                        .append(usage)
                        .append(" ")
                        .append(usage(label, helpArgs))
                        .create());
                return true;
            }
            sender.spigot().sendMessage(horizontalBar);
            Colors.sendInfo(sender, desc, " ", help);
            sender.spigot().sendMessage(fullUsage(label));
            return true;
        }
        if (playerOnly && !(sender instanceof Player)) {
            Colors.sendError(sender, "Only players can use that command.");
            return true;
        }
        if (perms != null && !sender.hasPermission(perms)) {
            Colors.sendError(sender, "You do not have permission use do that command.");
            return true;
        }
        if (!hasSubCommands) {
            if (fns == null) {
                Main.getInstance().getLogger().info("Paradoxical logical conflict in SnootPlugin. Message the SnootPlugin dev that the world shall end in a few days.");
                return true;
            }
            BiConsumer<CommandSender, List<String>> fn = fns.get(args.length);
            if (fn == null) {
                sender.spigot().sendMessage(horizontalBar);
                sender.spigot().sendMessage(new ComponentBuilder()
                        .append(usage)
                        .append(" ")
                        .append(usage(label, helpArgs))
                        .create());
                return true;
            }
            fn.accept(sender, new ArrayList<>(Arrays.asList(args)));
            return true;
        }
        if (args.length != 0) {
            for (SubCommand subCommand : subCommands) {
                if (!args[0].equals(subCommand.name)) {
                    continue;
                }
                if (subCommand.playerOnly && !(sender instanceof Player)) {
                    Colors.sendError(sender, "Only players can use that command.");
                    return true;
                }
                if (subCommand.perms != null && !sender.hasPermission(subCommand.perms)) {
                    Colors.sendError(sender, "You do not have permission use do that command.");
                    return true;
                }
                if (args.length == 2 && args[1].equals("?")) {
                    sender.spigot().sendMessage(horizontalBar);
                    Colors.sendInfo(sender, desc, " ", subCommand.help);
                    sender.spigot().sendMessage(new ComponentBuilder()
                            .append(usage)
                            .append(" ")
                            .append(usage(label + " " + subCommand.name, subCommand.helpArgs))
                            .create());
                    return true;
                }
                BiConsumer<CommandSender, List<String>> fn = subCommand.fns.get(args.length - 1);
                if (fn == null) {
                    sender.spigot().sendMessage(horizontalBar);
                    sender.spigot().sendMessage(new ComponentBuilder()
                            .append(usage)
                            .append(" ")
                            .append(usage(label + " " + subCommand.name, subCommand.helpArgs))
                            .create());
                    return true;
                }
                fn.accept(sender, new ArrayList<>(Arrays.asList(args).subList(1, args.length)));
                return true;
            }
        }
        sender.spigot().sendMessage(horizontalBar);
        sender.spigot().sendMessage(fullUsage(label));
        return true;
    }

    private BaseComponent[] fullUsage(String label) {
        List<BaseComponent> baseComponents = new ArrayList<>();
        baseComponents.add(new TextComponent(usage));
        for (SubCommand subCommand : subCommands) {
            baseComponents.add(new TextComponent("\n"));
            Collections.addAll(baseComponents, usage(label + " " + subCommand.name, subCommand.helpArgs));
        }
        return baseComponents.toArray(new BaseComponent[0]);
    }

    private BaseComponent[] usage(String label, List<HelpArg> helpArgs) {
        ComponentBuilder componentBuilder = new ComponentBuilder()
                .append("/" + label)
                .color(Colors.commandColor.asBungee())
                .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + label + (helpArgs.isEmpty() ? "" : " ")));
        for (HelpArg helpArg : helpArgs) {
            componentBuilder
                    .append(" ")
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text((String)null)))
                    .append(helpArg.bracketed())
                    .color(Colors.regularColor.asBungee())
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Colors.regular + "Desc: " + Colors.dark + helpArg.description + Colors.regular + "\nFormat: " + Colors.dark + helpArg.format + Colors.regular + "\n" + Colors.dark + (helpArg.required ? "Required" : "Optional"))));
        }
        return componentBuilder.create();
    }
}

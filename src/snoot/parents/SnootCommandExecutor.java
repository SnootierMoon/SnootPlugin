package snoot.parents;

import com.mojang.brigadier.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import snoot.Main;
import snoot.util.MessageFormat;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class SnootCommandExecutor implements CommandExecutor {

    static protected class SubCommand {
        String name, args, help, perms;
        Map<Integer,  BiConsumer<CommandSender, List<String>>> fns;
        boolean playerOnly;

        public SubCommand(String name, String args, String help, String perms, Map<Integer, BiConsumer<CommandSender, List<String>>> fns) {
            this.name = name;
            this.args = args;
            this.help = help;
            this.perms = perms;
            this.fns = fns;
        }

        public SubCommand(String name, String args, String help, String perms, Map<Integer, BiConsumer<CommandSender, List<String>>> fns, boolean playerOnly) {
            this.name = name;
            this.args = args;
            this.help = help;
            this.perms = perms;
            this.fns = fns;
            this.playerOnly = playerOnly;
        }

    }

    private final List<SubCommand> subCommands = new ArrayList<>();
    private String fullUsage;
    private final String help;

    protected SnootCommandExecutor(String help) {
        this.help = help;
    }

    protected void initialize() {
        fullUsage = subCommands.stream().map(subCommand -> MessageFormat.commandUsage(subCommand.name, subCommand.args)).collect(Collectors.joining("\n"));
    }

    protected void addSubCommand(SubCommand subCommand) {
        subCommands.add(subCommand);
    }

    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull final Command command, @Nonnull final String label, @Nonnull final String[] args) {
        if (args.length == 1 && args[0].equals("?")) {
            sender.sendMessage(MessageFormat.success(help));
            sender.sendMessage(MessageFormat.usage);
            sender.sendMessage(fullUsage);
            return true;
        } else if (args.length != 0) {
            for (SubCommand subCommand : subCommands) {
                if (!args[0].equals(subCommand.name)) {
                    continue;
                } else if (subCommand.playerOnly && !(sender instanceof Player)) {
                    sender.sendMessage(MessageFormat.playerExclusive);
                    return true;
                }
                if (args.length == 2 && args[1].equals("?")) {
                    sender.sendMessage(MessageFormat.commandHelp(subCommand.name, subCommand.args, subCommand.help));
                    return true;
                }
                BiConsumer<CommandSender, List<String>> fn = subCommand.fns.get(args.length - 1);
                if (fn == null) {
                    sender.sendMessage(MessageFormat.usage);
                    sender.sendMessage(MessageFormat.commandUsage(subCommand.name, subCommand.args));
                    return false;
                } else {
                    List<String> list = new ArrayList<>(Arrays.asList(args)).subList(1, args.length);
                    fn.accept(sender, list);
                }
                return true;
            }
        }
        sender.sendMessage(MessageFormat.usage);
        sender.sendMessage(fullUsage);
        return true;
    }

}

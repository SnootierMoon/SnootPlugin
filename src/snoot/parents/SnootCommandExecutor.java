package snoot.parents;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class SnootCommandExecutor implements CommandExecutor {

    static protected class SubCommand {
        String name, helpArgs, help, perms;
        Map<Integer,  BiConsumer<CommandSender, ArrayList<String>>> fns;
        boolean playerOnly;

        public SubCommand(String name, String helpArgs, String help, String perms, Map<Integer, BiConsumer<CommandSender, ArrayList<String>>> fns) {
            this.name = name;
            this.helpArgs = helpArgs;
            this.help = help;
            this.perms = perms;
            this.fns = fns;
        }

        public SubCommand(String name, String helpArgs, String help, String perms, Map<Integer, BiConsumer<CommandSender, ArrayList<String>>> fns, boolean playerOnly) {
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
    private final int argc;
    private final String helpArgs;

    public static final String desc = ChatColor.GOLD + "" + ChatColor.BOLD + "Desc:";
    public static final String usage = ChatColor.GOLD + "" + ChatColor.BOLD + "Usage:";

    protected SnootCommandExecutor(String help, String perms, boolean playerOnly) {
        this.help = help;
        this.perms = perms;
        this.playerOnly = playerOnly;
        this.hasSubCommands = true;
        helpArgs = "";
        argc = 0;
    }

    protected SnootCommandExecutor(String helpArgs, String help, String perms, int argc, boolean playerOnly) {
        this.helpArgs = helpArgs;
        this.help = help;
        this.perms = perms;
        hasSubCommands = false;
        this.argc = argc;
        this.playerOnly = playerOnly;
    }

    protected void addSubCommand(SubCommand subCommand) {
        subCommands.add(subCommand);
    }

    public void onMainCommand(CommandSender sender, ArrayList<String> args) { }

    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull final Command command, @Nonnull final String label, @Nonnull final String[] args) {
        if (args.length == 1 && args[0].equals("?")) {
            sender.sendMessage(desc + " " + ChatColor.GREEN + help);
            sender.sendMessage(usage);
            sender.sendMessage(fullUsage(label));
            return true;
        } else if (playerOnly && !(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use that command.");
            return true;
        } else if (perms != null && !sender.hasPermission(perms)) {
            sender.sendMessage(ChatColor.RED + "You do not have permission use do that command.");
            return true;
        } else if (!hasSubCommands) {
            if (args.length != argc) {
                sender.sendMessage(usage + ChatColor.AQUA + " /" +  label + " " + ChatColor.GRAY + " " + helpArgs);
                return true;
            } else {
                onMainCommand(sender, new ArrayList<>(Arrays.asList(args)));
                return true;
            }
        } else if (args.length != 0) {
            for (SubCommand subCommand : subCommands) {
                if (!args[0].equals(subCommand.name)) {
                    continue;
                } else if (subCommand.playerOnly && !(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Only players can use that command.");
                    return true;
                } else if (subCommand.perms != null && !sender.hasPermission(subCommand.perms)) {
                    sender.sendMessage(ChatColor.RED + "You do not have permission use do that command.");
                    return true;
                }
                if (args.length == 2 && args[1].equals("?")) {
                    sender.sendMessage(desc + " " + ChatColor.DARK_GREEN + subCommand.help);
                    sender.sendMessage(usage + ChatColor.AQUA + " /" +  label + " " + subCommand.name + ChatColor.GRAY + " " + subCommand.helpArgs);
                    return true;
                }
                BiConsumer<CommandSender, ArrayList<String>> fn = subCommand.fns.get(args.length - 1);
                if (fn == null) {
                    sender.sendMessage(usage + ChatColor.AQUA + " /" +  label + " " + subCommand.name + ChatColor.GRAY + " " + subCommand.helpArgs);
                    return false;
                } else {
                    fn.accept(sender, new ArrayList<>(Arrays.asList(args).subList(1, args.length)));
                }
                return true;
            }
        }
        sender.sendMessage(usage);
        sender.sendMessage(fullUsage(label));
        return true;
    }

    private String fullUsage(String label) {
        return subCommands.stream().map(subCommand -> ChatColor.AQUA + "/" + label + " " + subCommand.name + ChatColor.GRAY + " " + subCommand.helpArgs).collect(Collectors.joining("\n"));
    }

}

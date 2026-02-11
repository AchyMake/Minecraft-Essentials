package org.achymake.essentials.providers;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.group.GroupManager;
import net.luckperms.api.model.user.UserManager;
import org.achymake.essentials.Essentials;
import org.bukkit.entity.Player;

import java.util.*;

public class LuckPermsProvider {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    public int getWeight(Player player) {
        var group = getPlayerGroup(player);
        if (group != null) {
            if (group.getWeight().isEmpty()) {
                return 0;
            } else return group.getWeight().getAsInt();
        } else return 0;
    }
    public int getWeighted(Player player) {
        var listed = new ArrayList<>(getWeightedPlayers());
        for (var i = 0; i < listed.size(); i++) {
            if (listed.get(i).getKey() == player) {
                return i;
            }
        }
        return 0;
    }
    public Group getPlayerGroup(Player player) {
        var groupName = getGroupName(player);
        if (groupName != null) {
            if (getGroupManager().isLoaded(groupName)) {
                return getGroupManager().getGroup(groupName);
            } else return (Group) getGroupManager().loadGroup(groupName);
        } else return null;
    }
    public String getGroupName(Player player) {
        var user = getUserManager().getUser(player.getUniqueId());
        if (user != null) {
            return user.getPrimaryGroup();
        } else return null;
    }
    public List<Map.Entry<Player, Integer>> getWeightedPlayers() {
        var weights = new HashMap<Player, Integer>();
        for (var player : getInstance().getOnlinePlayers()) {
            weights.put(player, getWeight(player));
        }
        var listed = new ArrayList<>(weights.entrySet());
        listed.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));
        return listed.stream().toList();
    }
    public UserManager getUserManager() {
        return getLuckPerms().getUserManager();
    }
    public GroupManager getGroupManager() {
        return getLuckPerms().getGroupManager();
    }
    public LuckPerms getLuckPerms() {
        return net.luckperms.api.LuckPermsProvider.get();
    }
}
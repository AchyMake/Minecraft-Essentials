JitPack.io Check Repository = https://jitpack.io/#AchyMake/Minecraft-Essentials
```xml
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>com.github.AchyMake</groupId>
            <artifactId>Minecraft-Essentials</artifactId>
            <version>LATEST</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
```
Example for getting Userdata

```java
package org.example.yourplugin;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class YourPlugin extends JavaPlugin {
    private static YourPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {
    }

    public boolean isPVP(OfflinePlayer offlinePlayer) {
        return getEssentials().getUserdata().isPVP(offlinePlayer);
    }

    public boolean setPVP(OfflinePlayer offlinePlayer, boolean value) {
        return getEssentials().getUserdata().setBoolean(offlinePlayer, "settings.pvp", value);
    }

    public boolean isVanished(OfflinePlayer offlinePlayer) {
        return getEssentials().getUserdata().isVanished(offlinePlayer);
    }

    public void toggleVanish(Player player) {
        getEssentials().getVanishHandler().setVanish(player, !isVanished(player));
    }

    public void setVanished(OfflinePlayer offlinePlayer, boolean value) {
        getEssentials().getVanishHandler().setVanish(offlinePlayer, value);
    }

    public Essentials getEssentials() {
        return Essentials.getInstance();
    }

    public static YourPlugin getInstance() {
        return instance;
    }
}
```

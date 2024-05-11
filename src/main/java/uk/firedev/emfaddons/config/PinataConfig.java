package uk.firedev.emfaddons.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import uk.firedev.emfaddons.EMFAddons;
import uk.firedev.emfaddons.pinatas.Pinata;
import uk.firedev.emfaddons.pinatas.PinataManager;

public class PinataConfig extends ConfigBase {

    private static PinataConfig instance;

    private PinataConfig() {
        super("piñatas.yml", EMFAddons.getInstance());
    }

    public static PinataConfig getInstance() {
        if (instance == null) {
            instance = new PinataConfig();
        }
        return instance;
    }

    public void loadAllPinatas() {
        ConfigurationSection section = getConfig().getConfigurationSection("pinatas");
        if (section == null) {
            return;
        }
        section.getKeys(false).forEach(key -> {
            ConfigurationSection pinataSection = section.getConfigurationSection(key);
            if (pinataSection == null) {
                return;
            }
            @NotNull EntityType entityType;
            try {
                entityType = EntityType.valueOf(pinataSection.getString("entity-type", "llama").toUpperCase());
            } catch (IllegalArgumentException ex) {
                entityType = EntityType.LLAMA;
            }
            Pinata pinata = new Pinata(key, entityType, pinataSection.getString("display-name"));
            pinata.setGlowing(pinataSection.getBoolean("glowing", true));
            pinata.setHealth(pinataSection.getInt("health", 120));
            pinata.setSilent(pinataSection.getBoolean("silent", true));
            pinata.setGlowColor(pinataSection.getString("glow-color", "aqua").toUpperCase());
            pinata.setRewards(pinataSection.getStringList("rewards"));
            pinata.register();
        });
    }

    @Override
    public void reload() {
        super.reload();
        PinataManager.getInstance().clearLoadedPinatas();
        loadAllPinatas();
    }

}

package me.flungo.bukkit.plotme.gridgenerator;

import com.worldcretornica.plotme_core.api.v0_14b.IPlotMe_GeneratorManager;
import me.flungo.bukkit.plotme.abstractgenerator.AbstractGenerator;
import me.flungo.bukkit.plotme.abstractgenerator.WorldGenConfig;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginDescriptionFile;

public class GridGenerator extends AbstractGenerator {

    public String PREFIX;
    public String VERSION;

    public String language;

    private GridGenManager genManager;

    @Override
    public void takedown() {
        captionsCA = null;
        genManager = null;
        PREFIX = null;
        VERSION = null;
    }

    @Override
    public void initialize() {
        setupConfig();
        initialise();
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldname, String id) {
        worldname = worldname.toLowerCase();
        if (!genManager.containsWGC(worldname)) {
            genManager.putWGC(worldname, getWorldGenConfig(worldname));
        }
        return new GridChunkGenerator(this, genManager.getWGC(worldname));
    }

    private void setupConfig() {
        // Set defaults for WorldGenConfig
        WorldGenConfig.putDefault("PlotSize", 32);

        WorldGenConfig.putDefault("XTranslation", 0);
        WorldGenConfig.putDefault("ZTranslation", 0);

        WorldGenConfig.putDefault("BottomBlock", "7");

        WorldGenConfig.putDefault("WallBlock", "44");
        WorldGenConfig.putDefault("PlotFloorBlock", "2");
        WorldGenConfig.putDefault("FillBlock", "3");

        WorldGenConfig.putDefault("BaseHeight", 64);

        // If no world are defined in our config, define a sample world for the user to be able to copy.
        if (!getConfig().contains("worlds")) {
            // Get the config for an imaginary gridplots so that the config is generated.
            getWorldGenConfig("gridplots");
            saveConfig();
        }

    }

    public void initialise() {
        PluginDescriptionFile pdfFile = this.getDescription();
        PREFIX = ChatColor.BLUE + "[" + getName() + "] " + ChatColor.RESET;
        VERSION = pdfFile.getVersion();

        genManager = new GridGenManager(this);

        ConfigurationSection worlds = getConfig().getConfigurationSection("worlds");

        for (String worldname : worlds.getKeys(false)) {
            // Get config for world
            WorldGenConfig wgc = getWorldGenConfig(worldname);

            // Validate config
            if (wgc.getInt("RoadHeight") > 240) {
                getLogger().severe(PREFIX + "RoadHeight above 240 is unsafe. This is the height at which your road is located. Setting it to 240.");
                wgc.set("RoadHeight", 240);
            }

            // Add config to GenManager
            genManager.putWGC(worldname.toLowerCase(), wgc);
        }

        saveConfig();
    }

    public String caption(String s) {
        FileConfiguration config = captionsCA.getConfig();
        if (config.contains(s)) {
            return addColor(config.getString(s));
        } else {
            getLogger().warning(PREFIX + "Missing caption: " + s);
            return "ERROR:Missing caption '" + s + "'";
        }
    }

    public String addColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    @Override
    public IPlotMe_GeneratorManager getGeneratorManager() {
        return genManager;
    }
}

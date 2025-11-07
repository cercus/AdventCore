package fr.cercusmc.adventcore.utils.files;

import fr.cercusmc.adventcore.utils.messages.LoggingCategory;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class YamlFile {

    private final File file;

    private final String fileName;

    private final FileConfiguration fileConfiguration;

    private final String moduleName;

    public YamlFile(File folder, String moduleName, String fileName) {
        if (!fileName.endsWith(".yml"))
            this.fileName = fileName + ".yml";
        else
            this.fileName = fileName;

        this.moduleName = moduleName;
        this.file = new File(folder, this.fileName);

        // Crée seulement le dossier parent, pas le fichier
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        this.fileConfiguration = new YamlConfiguration();
        // Ne pas charger le fichier ici, il sera copié depuis le JAR avant le chargement
    }

    public void reloadFile() {
        if (!file.exists()) return;
        try {
            fileConfiguration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create or load a .yml file
     * @param plugin Plugin
     * @param fileName Name of file
     */
    public YamlFile(JavaPlugin plugin, String fileName) {
        if(!fileName.contains(".yml"))
            this.fileName = fileName+".yml";
        else this.fileName = fileName;

        this.moduleName = "";
        this.file = new File(plugin.getDataFolder(), fileName);
        if(!file.exists()) {
            LoggingCategory.getKernel("").createInfo("Création du fichier {0}...", this.fileName);
            plugin.saveResource(fileName, false);
            LoggingCategory.getKernel("").createInfo("Création du fichier terminé.");
        }

        this.fileConfiguration = new YamlConfiguration();
        reloadFile();
    }


    /**
     * Save a file <br />
     * @return true if save is successfull
     */
    public boolean save() {
        try {
            fileConfiguration.save(file);
            return true;
        } catch(IOException e) {
            LoggingCategory.getKernel(this.moduleName).createError("Une erreur est survenu pendant la sauvegarde du fichier {0}", e, this.fileName);
            return false;
        }
    }


    /**
     * Insert in the file a new value in given path. <br />
     * After insert the value, you don't have to use the method save()
     * @param path The path
     * @param value The value
     * @return The objetc inserted
     */
    public Object set(String path, Object value) {
        fileConfiguration.set(path, value);
        save();
        return value;
    }

    /**
     * Create a new section in yml file
     * @param path Path of new section
     * @return The ConfigurationSection
     */
    public ConfigurationSection createSection(String path) {
        return fileConfiguration.createSection(path);
    }

    /**
     * Get the value as string in given path
     * @param path Path to get the value
     * @return The value associated with path
     */
    public String getString(String path) {
        return fileConfiguration.getString(path);
    }

    /**
     * Get the value as string in given path
     * @param path Path to get the value
     * @param defaultString Default value if path is null
     * @return The value associated with path
     */
    public String getString(String path, String defaultString) {
        return fileConfiguration.getString(path, defaultString);
    }

    /**
     * Get the value as int in given path
     * @param path Path to get the value
     * @return The value associated with path
     */
    public int getInt(String path) {
        return fileConfiguration.getInt(path);
    }

    /**
     * Get the value as int in given path
     * @param path Path to get the value
     * @param defaultInt Default value if path is null
     * @return The value associated with path
     */
    public int getInt(String path, int defaultInt) {
        return fileConfiguration.getInt(path, defaultInt);
    }

    /**
     * Get the value as double in given path
     * @param path Path to get the value
     * @return The value associated with path
     */
    public double getDouble(String path) {
        return fileConfiguration.getDouble(path);
    }

    /**
     * Get the value as double in given path
     * @param path Path to get the value
     * @param defaultDouble Default value if path is null
     * @return The value associated with path
     */
    public double getDouble(String path, double defaultDouble) {
        return fileConfiguration.getDouble(path, defaultDouble);
    }

    /**
     * Get the value as list of string in given path
     * @param path Path to get the value
     * @return The value associated with path
     */
    public List<String> getStringList(String path) {
        return fileConfiguration.getStringList(path);
    }

    /**
     * Get the value as list of double in given path
     * @param path Path to get the value
     * @return The value associated with path
     */
    public List<Double> getDoubleList(String path) {
        return fileConfiguration.getDoubleList(path);
    }
    /**
     * Get the value as list of integer in given path
     * @param path Path to get the value
     * @return The value associated with path
     */
    public List<Integer> getIntegerList(String path) {
        return fileConfiguration.getIntegerList(path);
    }

    /**
     * Get the value as long in given path
     * @param path Path to get the value
     * @param defaultLong Default value if path is null
     * @return The value associated with path
     */
    public Long getLong(String path, long defaultLong) {
        return fileConfiguration.getLong(path, defaultLong);
    }

    /**
     * Get the value as long in given path
     * @param path Path to get the value
     * @return The value associated with path
     */
    public Long getLong(String path) {
        return fileConfiguration.getLong(path);
    }

    /**
     * Get the value as Location in given path
     * @param path Path to get the value
     * @return The value associated with path
     */
    public Location getLocation(String path) {
        return fileConfiguration.getLocation(path);
    }


    /**
     * Get the configurationSection in given path
     * @param path Path
     * @return The configurationSection
     */
    public ConfigurationSection getConfigurationSection(String path) {
        return fileConfiguration.getConfigurationSection(path);
    }

    /**
     * Check if path is in yml file
     * @param path Path
     * @return True if path is in yml file, false otherwise
     */
    public boolean contains(String path) {
        return fileConfiguration.contains(path);
    }

    /**
     * Get the value as boolean in given path
     * @param path Path to get the value
     * @param defaultBoolean Default value if path is null
     * @return The value associated with path
     */
    public boolean getBoolean(String path, boolean defaultBoolean) {
        return fileConfiguration.getBoolean(path, defaultBoolean);
    }

    /**
     * Get the value as boolean in given path
     * @param path Path to get the value
     * @return The value associated with path
     */
    public boolean getBoolean(String path) {
        return fileConfiguration.getBoolean(path);
    }

    /**
     * Get the file associated with this YamlFile
     * @return The file
     */
    public File getFile() {
        return file;
    }

    public String getModuleName() {
        return moduleName;
    }
}

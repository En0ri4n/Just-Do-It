package fr.en0ri4n.justdo.config.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;

public abstract class BaseConfig
{
    private final JavaPlugin plugin;
    private final String configFileName;
    private final boolean shouldReplace;

    protected BaseConfig(JavaPlugin plugin, String configFileName, boolean shouldReplace)
    {
        this.plugin = plugin;
        this.configFileName = configFileName;
        this.shouldReplace = shouldReplace;
    }

    public final void load()
    {
        saveResource(configFileName, shouldReplace);

        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), configFileName));

        loadConfig(config);
    }

    protected abstract void loadConfig(FileConfiguration config);

    public String getConfigFileName()
    {
        return configFileName;
    }

    public boolean shouldReplace()
    {
        return shouldReplace;
    }

    /**
     * From JavaPlugin
     * @param filename The filename
     */
    public InputStream getResource(String filename)
    {
        if(filename == null)
            throw new IllegalArgumentException("Filename cannot be null");

        try
        {
            URL url = plugin.getClass().getClassLoader().getResource(filename);

            if(url == null)
                return null;

            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        }
        catch(IOException ex)
        {
            return null;
        }
    }

    /**
     * From JavaPlugin#saveResource
     * @param resourcePath The resource path
     * @param replace If the resource should be replaced
     */
    public void saveResource(String resourcePath, boolean replace)
    {
        if(resourcePath == null || resourcePath.equals(""))
        {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }

        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = getResource(resourcePath);

        if(in == null)
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in " + plugin.getDataFolder());

        File outFile = new File(plugin.getDataFolder(), resourcePath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(plugin.getDataFolder(), resourcePath.substring(0, Math.max(lastIndex, 0)));

        if(!outDir.exists())
        {
            outDir.mkdirs();
        }

        try
        {
            if(!outFile.exists() || replace)
            {
                OutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while((len = in.read(buf)) > 0)
                {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            }

            // Useless warning
            // plugin.getLogger().log(Level.WARNING, "Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
        }
        catch(IOException ex)
        {
            plugin.getLogger().log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, ex);
        }
    }
}

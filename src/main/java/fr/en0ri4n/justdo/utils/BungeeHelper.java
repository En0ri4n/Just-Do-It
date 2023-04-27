package fr.en0ri4n.justdo.utils;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.en0ri4n.justdo.JustDoMain;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class BungeeHelper implements PluginMessageListener
{
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message)
    {
        if(!channel.equals("BungeeCord"))
            return;

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();

        if(subchannel.equals("SomeSubChannel"))
        {
            // Use the code sample in the 'Response' sections below to read
            // the data.
        }
    }

    public static void teleportPlayerTo(Player player, String serverName)
    {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ConnectOther");
        out.writeUTF(player.getName());
        out.writeUTF(serverName);

        player.sendPluginMessage(JustDoMain.getInstance(), "BungeeCord", out.toByteArray());
    }
}

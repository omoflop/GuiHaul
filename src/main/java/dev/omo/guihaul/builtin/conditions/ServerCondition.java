package dev.omo.guihaul.builtin.conditions;

import dev.omo.guihaul.api.Property;
import dev.omo.guihaul.api.PropertyHolder;
import dev.omo.guihaul.api.data.HaulCondition;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;

public class ServerCondition extends HaulCondition {
    public static final Property<String> SERVER_NAME = Property.of("name", String.class);
    public static final Property<String> SERVER_ADDRESS = Property.of("address", String.class);

    @Override
    public void appendProperties(PropertyHolder.Builder builder) {
        builder.add(SERVER_NAME, "").add(SERVER_ADDRESS, "");

    }

    @Override
    protected void init(PropertyHolder holder) {
        super.init(holder);
    }

    @Override
    protected boolean passesCondition(PropertyHolder holder, Context ctx) {
        ServerInfo server = MinecraftClient.getInstance().getCurrentServerEntry();
        if (server == null) return false;

        String name = holder.getProperty(SERVER_NAME);
        String ip = holder.getProperty(SERVER_ADDRESS);

        boolean shouldCheckName = !name.isEmpty();
        boolean shouldCheckIp = !ip.isEmpty();

        if (!shouldCheckIp && !shouldCheckName)
            return false;

        boolean matchesName = shouldCheckName && (server.name.matches(name));
        boolean matchesIp = shouldCheckIp && (server.address.matches(ip));
        return (shouldCheckName == matchesName) && (shouldCheckIp == matchesIp);
    }
}
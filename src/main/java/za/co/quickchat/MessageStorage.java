package za.co.quickchat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MessageStorage {

    private final Path filePath;
    private final Gson gson;

    public MessageStorage() {
        this("messages.json");
    }

    public MessageStorage(String fileName) {
        filePath = Paths.get(fileName);
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void saveMessages(List<Message> messages)
            throws IOException {
        String json = gson.toJson(messages);
        Path parent = filePath.toAbsolutePath().getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        Files.writeString(filePath, json);
    }

    public List<Message> loadMessages() throws IOException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        String json = Files.readString(filePath);

        if (json.isBlank()) {
            return new ArrayList<>();
        }

        Type type = new TypeToken<ArrayList<Message>>() {}.getType();
        List<Message> messages = gson.fromJson(json, type);

        return messages == null ? new ArrayList<>() : messages;
    }

    public boolean storageFileExists() {
        return Files.exists(filePath);
    }
}


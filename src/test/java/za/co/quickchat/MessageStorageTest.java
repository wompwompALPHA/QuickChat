package za.co.quickchat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MessageStorageTest {

    @TempDir
    Path tempDirectory;

    @Test
    public void testSaveAndLoadMessages() throws IOException {
        Path file = tempDirectory.resolve("test-messages.json");
        MessageStorage storage = new MessageStorage(file.toString());

        List<Message> messages = new ArrayList<>();
        Message message = new Message(
                0,
                "+27718693002",
                "Hi Mike, join us for dinner?"
        );

        message.SentMessage();
        messages.add(message);

        storage.saveMessages(messages);

        assertTrue(storage.storageFileExists());

        List<Message> loaded = storage.loadMessages();

        assertEquals(1, loaded.size());
        assertEquals(message.getMessageID(), loaded.get(0).getMessageID());
        assertEquals(message.getRecipient(), loaded.get(0).getRecipient());
        assertEquals(message.getMessageText(), loaded.get(0).getMessageText());
        assertEquals(message.getMessageHash(), loaded.get(0).getMessageHash());
        assertEquals(message.getStatus(), loaded.get(0).getStatus());
    }

    @Test
    public void testMissingFileReturnsEmptyList() throws IOException {
        Path file = tempDirectory.resolve("missing.json");
        MessageStorage storage = new MessageStorage(file.toString());

        List<Message> loaded = storage.loadMessages();

        assertTrue(loaded.isEmpty());
    }
}

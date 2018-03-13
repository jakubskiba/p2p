import service.ChunkSender;

public class SenderApp {
    public static void main(String[] args) {
        ChunkSender chunkSender = new ChunkSender();
        chunkSender.start();
    }
}

import java.util.UUID;

public record IdService() {
    public UUID generateId() {
        return UUID.randomUUID();
    }
}

public enum StateOfDelivery {
    PROCESSING("Processing."),
    IN_DELIVERY("In delivery."),
    COMPLETED("Completed.");

    private final String description;

    StateOfDelivery(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

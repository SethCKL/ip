public class Event extends Task{
    protected String setAt;

    public Event(String description, String setAt) {
        super(description);
        this.setAt = setAt;
    }

    @Override
    public String toString() {
        return String.format("[E]%s(at: %s)", super.toString(), setAt);
    }
}

package restsecurity;

import java.time.LocalDateTime;

public class Greeting {

    private final long id;
    private final String content;
    private final LocalDateTime ldt;

    public Greeting(long id, String content, LocalDateTime ldt) {
        this.id = id;
        this.content = content;
        this.ldt = ldt;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
    
}

package restsecurity;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    
    @Autowired
    private GreetingMapper greetingMapper;
    
    @RequestMapping("/api/greeting")
    public GreetingDTO greeting(@RequestParam(value="name", defaultValue="World") String name) {
    	System.out.println(" GreetingController.greeting() called.");
    	
        Greeting greet = new Greeting(counter.incrementAndGet(), String.format(template, name), LocalDateTime.now());
        
      //GreetingDTO greetDto = GreetingMapper.INSTANCE.greetingToGreetingDto(greet);
    	GreetingDTO greetDto = greetingMapper.greetingToGreetingDto(greet);
    	
    	return greetDto;
    }
}
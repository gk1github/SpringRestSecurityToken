package restsecurity;

import java.time.LocalDateTime;

import org.mapstruct.Mapper;

@Mapper
public class GreetingDTO {	
	private String content1;
	private LocalDateTime ldt1;

	public String getContent1() {
		return content1;
	}

	public void setContent1(String content1) {
		this.content1 = content1;
	}

	public LocalDateTime getLdt1() {
		return ldt1;
	}

	public void setLdt1(LocalDateTime ldt1) {
		this.ldt1 = ldt1;
	}
	
	
}

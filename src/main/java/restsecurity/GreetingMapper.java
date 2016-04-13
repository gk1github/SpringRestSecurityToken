package restsecurity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel="spring")
public interface GreetingMapper {
	//GreetingMapper INSTANCE = Mappers.getMapper( GreetingMapper.class ); 

    @Mapping(source = "content", target = "content1")
    GreetingDTO greetingToGreetingDto(Greeting greet);
}

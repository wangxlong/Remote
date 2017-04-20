package sport.user.register.validator;

import java.util.Hashtable;
import java.util.Map;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import sport.user.register.domain.UserRegisterBean;

public class UserRegisterValidator {
    
    
    
	public Hashtable validate(UserRegisterBean userRegisterBean){
		
		Map hashTable =new Hashtable();		
		return (Hashtable) hashTable;
	}
}

package studentDB.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class StudentAdvice {

	@ResponseBody
	@ExceptionHandler(StudentException.class)
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	public String studentExceptionHandler(StudentException ex) {
		return ex.getMessage();
	}
}

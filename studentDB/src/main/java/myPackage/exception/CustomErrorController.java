package myPackage.exception;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController implements ErrorController {

	@RequestMapping("/error")
	public String handleError(HttpServletRequest request) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		Integer statusCode = null;
		if (status != null) {
			statusCode = Integer.valueOf(status.toString());
		}
		
		return "<h2>Si è verificato un errore: " + statusCode + "</h2><br/>"
				+"<a href=\"/login\">Torna al login</a>";
	    
	
	}
}
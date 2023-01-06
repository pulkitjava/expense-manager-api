package in.pulkitjain.expensetrackerapi.controller;

import javax.validation.Valid;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.pulkitjain.expensetrackerapi.entity.AuthModel;
import in.pulkitjain.expensetrackerapi.entity.JwtResponse;
import in.pulkitjain.expensetrackerapi.entity.User;
import in.pulkitjain.expensetrackerapi.entity.UserModel;
import in.pulkitjain.expensetrackerapi.security.CustomUserDetailsService;
import in.pulkitjain.expensetrackerapi.service.UserService;
import in.pulkitjain.expensetrackerapi.util.JwtTokenUtil;
import javassist.expr.NewArray;

@RestController
public class AuthController {
	@Autowired
	private UserService userService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@PostMapping("/register")
	public ResponseEntity<User> save(@Valid @RequestBody UserModel user) {
		return new ResponseEntity<User>(userService.createUser(user), HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody AuthModel authModel) throws Exception {
		authenticate(authModel.getEmail(), authModel.getPassword());
		//we need to generate jwt token
		final UserDetails userDetails=customUserDetailsService.loadUserByUsername(authModel.getEmail());
		final String token=jwtTokenUtil.generateToken(userDetails);
		return new ResponseEntity<JwtResponse>(new JwtResponse(token),HttpStatus.OK);
	}

	private void authenticate(String email, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
		} catch (DisabledException e) {
			throw new Exception("User Disabled");
		}catch (BadCredentialsException e) {
			throw new Exception("Bad Credentials");
		}

	}
}

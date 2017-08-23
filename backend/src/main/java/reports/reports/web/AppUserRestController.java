package reports.reports.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reports.reports.dto.AppUserDTO;
import reports.reports.dto.support.PageRequestByExample;
import reports.reports.dto.support.PageResponse;
import reports.reports.service.AppUserService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/api/users")
public class AppUserRestController {

	private final Logger log = LoggerFactory.getLogger(AppUserRestController.class);

	@Autowired
	private AppUserService appUserService;

	/**
	 * Find a Page of User using query by example.
	 */
	@RequestMapping(value = "/page", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<PageResponse<AppUserDTO>> findAll(@RequestBody PageRequestByExample<AppUserDTO> prbe) throws URISyntaxException {
		PageResponse<AppUserDTO> pageResponse = appUserService.findAll(prbe);
		return new ResponseEntity<>(pageResponse, new HttpHeaders(), HttpStatus.OK);
	}

	/**
	 * Delete by id User.
	 */
	@RequestMapping(value = "/{id}", method = DELETE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) throws URISyntaxException {

		log.debug("Delete by id User : {}", id);

		try {
			appUserService.delete(id);
			return ResponseEntity.ok().build();
		} catch (Exception x) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}

	/**
	 * Find by id User.
	 */
	@RequestMapping(value = "/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<AppUserDTO> findById(@PathVariable Integer id) throws URISyntaxException {

		log.debug("Find by id User : {}", id);

		return Optional.ofNullable(appUserService.findOne(id)).map(userDTO -> new ResponseEntity<>(userDTO, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * Find by userName.
	 */
	@RequestMapping(value = "/name/{userName}", method = GET, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<AppUserDTO> findByUserName(@PathVariable String userName) throws URISyntaxException {

		log.debug("Find by userName : {}", userName);

		return Optional.ofNullable(appUserService.findByUserName(userName)).map(userDTO -> new ResponseEntity<>(userDTO, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * Update User.
	 */
	@RequestMapping(value = "/", method = PUT, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<AppUserDTO> update(@RequestBody AppUserDTO userDTO) throws URISyntaxException {

		log.debug("Update UserDTO : {}", userDTO);

		if (!userDTO.isIdSet()) {
			return create(userDTO);
		}

		AppUserDTO result = appUserService.save(userDTO);

		return ResponseEntity.ok().body(result);
	}

	/**
	 * Create a new User.
	 */
	@RequestMapping(value = "/", method = POST, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<AppUserDTO> create(@RequestBody AppUserDTO userDTO) throws URISyntaxException {

		log.debug("Create UserDTO : {}", userDTO);

		if (userDTO.isIdSet()) {
			return ResponseEntity.badRequest().header("Failure", "Cannot create User with existing ID").body(null);
		}

		AppUserDTO result = appUserService.save(userDTO);

		return ResponseEntity.created(new URI("/api/users/" + result.id)).body(result);
	}

	@RequestMapping(value = "/findAllAppUsersWhichDoNotHaveReportWithThisId/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<List<AppUserDTO>> findAllAppUsersWhichDoNotHaveReportWithThisId(@PathVariable Integer id) throws URISyntaxException {

		List<AppUserDTO> results = appUserService.findAllAppUsersWhichDoNotHaveReportWithThisId(id);

		return new ResponseEntity<>(results, new HttpHeaders(), HttpStatus.OK);
	}
}

package reports.reports;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import reports.reports.domain.AppUser;
import reports.reports.dto.AppUserDTO;
import reports.reports.dto.support.LazyLoadEvent;
import reports.reports.dto.support.PageRequestByExample;
import reports.reports.dto.support.PageResponse;
import reports.reports.service.admin.AppUserService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppUserTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AppUserService appUserService;

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        appUserService.deleteAll();

        List<AppUser> appUsers = new ArrayList<>();
        appUsers.add(new AppUser("admin", "admin", "Jakub", "Loryś", "kuba35@vp.pl"));
        appUsers.add(new AppUser("user1", "user1", "Dylon", "Sanawabicz", "ds@x.pl"));
        appUsers.add(new AppUser("user2", "user2", "Arkadiusz", "Głowa", "ag@x.pl"));

        appUsers.stream().forEach(appUser -> appUserService.save(appUserService.toDTO(appUser)));
    }

    @Test
    public void findByUserName_ShouldReturnOneEntry() {

        AppUserDTO appUserDTO = appUserService.findByUserName("admin");
        assertEquals(appUserDTO.firstName, "Jakub");

    }

    @Test
    public void findAllPageableByExample_ShouldReturnOneEntry() {

        PageRequestByExample<AppUserDTO> appUserDTOPageRequestByExample = new PageRequestByExample<>();
        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.firstName = "J";
        appUserDTOPageRequestByExample.example = appUserDTO;

        PageResponse<AppUserDTO> appUserDTOPageResponse = appUserService.findAll(appUserDTOPageRequestByExample);
        assertEquals(appUserDTOPageResponse.totalElements, 1);

    }

    @Test
    public void findAllPageableByPageRows_ShouldReturnTwoPages() {

        PageRequestByExample<AppUserDTO> appUserDTOPageRequestByExample = new PageRequestByExample<>();
        LazyLoadEvent lazyLoadEvent = new LazyLoadEvent();
        lazyLoadEvent.rows = 2;
        appUserDTOPageRequestByExample.lazyLoadEvent = lazyLoadEvent;

        PageResponse<AppUserDTO> appUserDTOPageResponse = appUserService.findAll(appUserDTOPageRequestByExample);

        assertEquals(appUserDTOPageResponse.totalPages, 2);

    }

    @Test
    public void saveAppUser_ShouldCreateNewAppUser() {

        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.id = 1;
        appUserDTO.firstName = "Jacek";
        appUserDTO.lastName = "Balcerzak";
        appUserDTO.email = "jb@x.pl";
        appUserDTO.password = passwordEncoder.encode("Jacek");

        assertEquals(appUserService.save(appUserDTO).firstName, "Jacek");

    }

}

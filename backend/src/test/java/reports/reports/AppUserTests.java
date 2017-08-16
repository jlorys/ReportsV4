package reports.reports;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reports.reports.domain.AppUser;
import reports.reports.dto.AppUserDTO;
import reports.reports.service.AppUserService;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppUserTests {

    @Autowired
    private AppUserService appUserService;

    /**
     *
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

}

package reports.reports.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import reports.reports.domain.Report;
import reports.reports.domain.Report_;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Integer> {

    default List<Report> complete(String query, int maxResults) {
        Report probe = new Report();
        probe.setDescription(query);

        ExampleMatcher matcher = ExampleMatcher.matching() //
                .withMatcher(Report_.description.getName(), match -> match.ignoreCase().startsWith());

        Page<Report> page = findAll(Example.of(probe, matcher), new PageRequest(0, maxResults));
        return page.getContent();
    }

}

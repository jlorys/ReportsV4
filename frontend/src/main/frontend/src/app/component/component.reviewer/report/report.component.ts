import {Component, Input, SimpleChanges} from "@angular/core";
import {PageResponse} from "../../../support/paging";
import {LazyLoadEvent, Message} from "primeng/primeng";
import {Router} from "@angular/router";
import {ReviewerReportDataService} from "./report.data.service";
import {Report} from "../../component.admin/report/report";

@Component({
  selector: 'reports',
  templateUrl: './report.component.html'
})
export class ReviewerReportComponent {

  @Input() header = "Raporty...";
  // list is paginated
  currentPage: PageResponse<Report> = new PageResponse<Report>(0, 0, []);
  example: Report = new Report();
  msgs: Message[] = [];

  constructor(private router: Router, private reviewerReportDataService: ReviewerReportDataService) {}

  ngOnChanges(changes: SimpleChanges) {
    this.loadPage({first: 0, rows: 10, sortField: null, sortOrder: null, filters: null, multiSortMeta: null});
  }

  /**
   * Invoked automatically by primeng datatable.
   */
  loadPage(event: LazyLoadEvent) {
    this.msgs = []; //this line fix disappearing of messages
    this.reviewerReportDataService.getPage(this.example, event).subscribe(
      pageResponse => this.currentPage = pageResponse,
      error => this.msgs.push({severity:'error', summary:'Błąd pobierania danych!', detail: error})
    );
  }

  onRowSelect(event : any) {
    let id =  event.data.id;
    this.router.navigate(['/reviewerReports', id]);
  }

  //This method is for refreshing dataTable
  visible: boolean = true;
  updateVisibility(): void {
    this.visible = false;
    setTimeout(() => this.visible = true, 0);
  }
}

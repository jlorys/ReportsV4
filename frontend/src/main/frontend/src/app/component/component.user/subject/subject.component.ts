import {Component, Input, SimpleChanges} from "@angular/core";
import {PageResponse} from "../../../support/paging";
import {LazyLoadEvent, Message} from "primeng/primeng";
import {Router} from "@angular/router";
import {SubjectDataService} from "../../component.admin/subject/subject.data.service";
import {Subject} from "../../component.admin/subject/subject";

@Component({
  selector: 'userSubjects',
  templateUrl: './subject.component.html'
})
export class UserSubjectComponent {

  @Input() header = "Przedmioty...";
  // list is paginated
  currentPage: PageResponse<Subject> = new PageResponse<Subject>(0, 0, []);
  // basic search criterias (visible if not in 'sub' mode)
  example: Subject = new Subject();

  msgs: Message[] = [];

  constructor(private router: Router,
              private subjectDataService: SubjectDataService) {
  }

  /**
   * When used as a 'sub' component (to display one-to-many list), refreshes the table
   * content when the input changes.
   */
  ngOnChanges(changes: SimpleChanges) {
    this.loadPage({first: 0, rows: 10, sortField: null, sortOrder: null, filters: null, multiSortMeta: null});
  }

  /**
   * Invoked automatically by primeng datatable.
   */
  loadPage(event: LazyLoadEvent) {
    this.msgs = []; //this line fix disappearing of messages
    this.subjectDataService.getPage(this.example, event).subscribe(
      pageResponse => this.currentPage = pageResponse,
      error => this.msgs.push({severity:'error', summary:'Błąd pobierania danych!', detail: error})
    );
  }

  onRowSelect(event : any) {
    let id =  event.data.id;
    this.router.navigate(['/userSubjects', id]);
  }

  //This method is for refreshing dataTable
  visible: boolean = true;
  updateVisibility(): void {
    this.visible = false;
    setTimeout(() => this.visible = true, 0);
  }
}

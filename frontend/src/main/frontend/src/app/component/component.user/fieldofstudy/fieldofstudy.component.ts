import {Component, Input, SimpleChanges} from "@angular/core";
import {PageResponse} from "../../../support/paging";
import {LazyLoadEvent, Message} from "primeng/primeng";
import {Router} from "@angular/router";
import {FieldOfStudyDataService} from "../../component.admin/fieldofstudy/fieldofstudy.data.service";
import {FieldOfStudy} from "../../component.admin/fieldofstudy/fieldofstudy";

@Component({
  selector: 'userFieldsOfStudies',
  templateUrl: './fieldofstudy.component.html'
})
export class UserFieldOfStudyComponent {

  @Input() header = "Kierunki studiów...";
  // list is paginated
  currentPage: PageResponse<FieldOfStudy> = new PageResponse<FieldOfStudy>(0, 0, []);
  example: FieldOfStudy = new FieldOfStudy();
  msgs: Message[] = [];

  constructor(protected router: Router,
              private fieldOfStudyDataService: FieldOfStudyDataService) {
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
    this.fieldOfStudyDataService.getPage(this.example, event).subscribe(
      pageResponse => this.currentPage = pageResponse,
      error => this.msgs.push({severity:'error', summary:'Błąd pobierania danych!', detail: error})
    );
  }

  onRowSelect(event : any) {
    let id =  event.data.id;
    this.router.navigate(['/userFieldsOfStudies', id]);
  }

  //This method is for refreshing dataTable
  visible: boolean = true;
  updateVisibility(): void {
    this.visible = false;
    setTimeout(() => this.visible = true, 0);
  }
}

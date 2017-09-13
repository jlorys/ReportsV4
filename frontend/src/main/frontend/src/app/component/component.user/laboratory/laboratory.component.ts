import {Component, Input, SimpleChanges} from "@angular/core";
import {PageResponse} from "../../../support/paging";
import {LazyLoadEvent, Message} from "primeng/primeng";
import {Router} from "@angular/router";
import {Laboratory} from "../../component.admin/laboratory/laboratory";
import {UserLaboratoryDataService} from "./laboratory.data.service";

@Component({
  selector: 'userLaboratories',
  templateUrl: './laboratory.component.html'
})
export class UserLaboratoryComponent {

  @Input() header = "Laboratoria...";
  // list is paginated
  currentPage: PageResponse<Laboratory> = new PageResponse<Laboratory>(0, 0, []);
  // basic search criterias (visible if not in 'sub' mode)
  example: Laboratory = new Laboratory();

  msgs: Message[] = [];

  constructor(private router: Router,
              private userLaboratoryDataService: UserLaboratoryDataService) {}

  ngOnChanges(changes: SimpleChanges) {
    this.loadPage({first: 0, rows: 10, sortField: null, sortOrder: null, filters: null, multiSortMeta: null});
  }

  /**
   * Invoked automatically by primeng datatable.
   */
  loadPage(event: LazyLoadEvent) {
    this.msgs = []; //this line fix disappearing of messages
    this.userLaboratoryDataService.getPage(this.example, event).subscribe(
      pageResponse => this.currentPage = pageResponse,
      error => this.msgs.push({severity:'error', summary:'Błąd pobierania danych!', detail: error})
    );
  }

  onRowSelect(event : any) {
    let id =  event.data.id;
    this.router.navigate(['/userLaboratories', id]);
  }

  //This method is for refreshing dataTable
  visible: boolean = true;
  updateVisibility(): void {
    this.visible = false;
    setTimeout(() => this.visible = true, 0);
  }
}

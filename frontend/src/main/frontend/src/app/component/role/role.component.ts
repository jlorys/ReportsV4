import {LazyLoadEvent, Message} from "primeng/primeng";
import {Component, Input, SimpleChanges} from "@angular/core";
import {RoleDataService} from "./role.data.service";
import {Role} from "./role";
import {PageResponse} from "../../support/paging";

@Component({
  selector: 'roles',
  templateUrl: './role.component.html'
})
export class RolesComponent {

  @Input() header = "Role...";
  // list is paginated
  currentPage: PageResponse<Role> = new PageResponse<Role>(0, 0, []);
  // basic search criterias (visible if not in 'sub' mode)
  example: Role = new Role();
  msgs: Message[] = [];

  constructor(private roleDataService: RoleDataService) {
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
    this.roleDataService.getPage(this.example, event).subscribe(
      pageResponse => this.currentPage = pageResponse,
      error => this.msgs.push({severity:'error', summary:'Could not get the results!', detail: error})
    );
  }
}

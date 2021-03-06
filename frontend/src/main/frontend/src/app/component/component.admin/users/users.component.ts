import {Component, Input, SimpleChanges} from '@angular/core';
import {AppUser} from './user';
import {AppUserDataService} from './user.data.service';
import {PageResponse} from "../../../support/paging";
import {LazyLoadEvent, Message} from "primeng/primeng";
import {MdDialog} from '@angular/material';
import {ConfirmDeleteDialogComponent} from "../../../support/confirm-delete-dialog.component";
import {Router} from "@angular/router";

@Component({
  selector: 'users',
  templateUrl: './users.component.html'
})
export class AppUsersComponent {

  @Input() header = "Użytkownicy...";
  // list is paginated
  currentPage: PageResponse<AppUser> = new PageResponse<AppUser>(0, 0, []);
  // basic search criterias
  example: AppUser = new AppUser();

  msgs: Message[] = [];

  constructor(private router: Router,
              private appUserDataService: AppUserDataService,
              private confirmDeleteDialog: MdDialog) {}

  showDeleteDialog(rowData: any) {
    let userToDelete: AppUser = <AppUser> rowData;

    let dialogRef = this.confirmDeleteDialog.open(ConfirmDeleteDialogComponent);
    dialogRef.afterClosed().subscribe(result => {
      if (result === 'delete') {
        this.delete(userToDelete);
      }
    });
  }

  private delete(userToDelete: AppUser) {
    let id = userToDelete.id;

    this.msgs = []; //this line fix disappearing of messages
    this.appUserDataService.delete(id).subscribe(
      response => {
        this.currentPage.remove(userToDelete);
        this.updateVisibility();
      },
      error => this.msgs.push({severity:'error', summary:'Nie można usunąć!', detail: error})
    );
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
    this.appUserDataService.getPage(this.example, event).subscribe(
      pageResponse => this.currentPage = pageResponse,
      error => this.msgs.push({severity:'error', summary:'Błąd pobierania danych!', detail: error})
    );
  }

  addNew() {
      this.router.navigate(['/users/add']);
  }

  deleteOneDayUnconfirmedUsers() {
    this.appUserDataService.deleteOneDayUnconfirmedUsers().subscribe(
      response => {
        this.updateVisibility();
      },
      error => this.msgs.push({severity:'error', summary:'Nie można usunąć!', detail: error})
    );
  }

  onRowSelect(event : any) {
    let id =  event.data.id;
    this.router.navigate(['/users', id]);
  }

  //This method is for refreshing dataTable
  visible: boolean = true;
  updateVisibility(): void {
    this.visible = false;
    setTimeout(() => this.visible = true, 0);
  }
}

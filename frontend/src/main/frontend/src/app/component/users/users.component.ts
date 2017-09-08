import {Component, EventEmitter, Input, Output, SimpleChanges} from '@angular/core';
import {AppUser} from './user';
import {AppUserDataService} from './user.data.service';
import {PageResponse} from "../../support/paging";
import {LazyLoadEvent, Message} from "primeng/primeng";
import {MdDialog} from '@angular/material';
import {ConfirmDeleteDialogComponent} from "../../support/confirm-delete-dialog.component";
import {Router} from "@angular/router";
import {AuthService} from "../../service/auth.service";

@Component({
  selector: 'users',
  templateUrl: './users.component.html'
})
export class AppUsersComponent {

  @Input() header = "Użytkownicy...";
  // list is paginated
  currentPage: PageResponse<AppUser> = new PageResponse<AppUser>(0, 0, []);
  // basic search criterias (visible if not in 'sub' mode)
  example: AppUser = new AppUser();
  /** When 'sub' is true, it means this list is used as a one-to-many list.
   * It belongs to a parent entity, as a result the addNew operation
   * must prefill the parent entity. The prefill is not done here, instead we
   * emit an event. When 'sub' is false, we display basic search criterias
   */
  @Input() sub: boolean;
  @Output() onAddNewClicked = new EventEmitter();

  msgs: Message[] = [];

  userHasRoleAdmin: boolean;
  userHasRoleUser: boolean;

  constructor(private router: Router,
              private appUserDataService: AppUserDataService,
              private confirmDeleteDialog: MdDialog,
              private authService: AuthService) {

    this.authService.isLoggedUserHasRoleAdmin().subscribe(
      result => this.userHasRoleAdmin = result,
      error =>this.msgs.push({severity:'error', summary:'Błąd pobierania roli!', detail: error})
    );

    this.authService.isLoggedUserHasRoleUser().subscribe(
      result => this.userHasRoleUser = result,
      error =>this.msgs.push({severity:'error', summary:'Błąd pobierania roli!', detail: error})
    );

  }

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
    if (this.sub) {
      this.onAddNewClicked.emit("addNew");
    } else {
      this.router.navigate(['/users/add']);
    }
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

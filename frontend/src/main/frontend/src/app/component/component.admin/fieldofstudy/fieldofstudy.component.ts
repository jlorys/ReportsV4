import {Component, Input, SimpleChanges} from "@angular/core";
import {PageResponse} from "../../../support/paging";
import {FieldOfStudy} from "./fieldofstudy";
import {LazyLoadEvent, Message} from "primeng/primeng";
import {Router} from "@angular/router";
import {FieldOfStudyDataService} from "./fieldofstudy.data.service";
import {MdDialog} from "@angular/material";
import {ConfirmDeleteDialogComponent} from "../../../support/confirm-delete-dialog.component";

@Component({
  selector: 'fieldsOfStudies',
  templateUrl: './fieldofstudy.component.html'
})
export class FieldOfStudyComponent {

  @Input() header = "Kierunki studiów...";
  // list is paginated
  currentPage: PageResponse<FieldOfStudy> = new PageResponse<FieldOfStudy>(0, 0, []);
  // basic search criterias
  example: FieldOfStudy = new FieldOfStudy();

  msgs: Message[] = [];

  constructor(protected router: Router,
              private fieldOfStudyDataService: FieldOfStudyDataService,
              private confirmDeleteDialog: MdDialog) {
  }

  showDeleteDialog(rowData: any) {
    let fieldOfStudyToDelete: FieldOfStudy = <FieldOfStudy> rowData;

    let dialogRef = this.confirmDeleteDialog.open(ConfirmDeleteDialogComponent);
    dialogRef.afterClosed().subscribe(result => {
      if (result === 'delete') {
        this.delete(fieldOfStudyToDelete);
      }
    });
  }

  private delete(fieldOfStudyToDelete: FieldOfStudy) {
    let id = fieldOfStudyToDelete.id;

    this.msgs = []; //this line fix disappearing of messages
    this.fieldOfStudyDataService.delete(id).subscribe(
      response => {
        this.currentPage.remove(fieldOfStudyToDelete);
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
    this.fieldOfStudyDataService.getPage(this.example, event).subscribe(
      pageResponse => this.currentPage = pageResponse,
      error => this.msgs.push({severity:'error', summary:'Błąd pobierania danych!', detail: error})
    );
  }

  addNew() {
    this.router.navigate(['/fieldsOfStudies/add']);
  }

  onRowSelect(event : any) {
    let id =  event.data.id;
    this.router.navigate(['/fieldsOfStudies', id]);
  }

  //This method is for refreshing dataTable
  visible: boolean = true;
  updateVisibility(): void {
    this.visible = false;
    setTimeout(() => this.visible = true, 0);
  }
}

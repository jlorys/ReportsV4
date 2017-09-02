import {Component, EventEmitter, Input, Output, SimpleChanges} from "@angular/core";
import {PageResponse} from "../../support/paging";
import {FieldOfStudy} from "./fieldofstudy";
import {LazyLoadEvent, Message} from "primeng/primeng";
import {Router} from "@angular/router";
import {FieldOfStudyDataService} from "./fieldofstudy.data.service";
import {MdDialog} from "@angular/material";
import {ConfirmDeleteDialogComponent} from "../../support/confirm-delete-dialog.component";

@Component({
  selector: 'fieldsOfStudies',
  templateUrl: './fieldofstudy.component.html'
})
export class FieldOfStudyComponent {

  @Input() header = "Kierunki studiów...";
  // list is paginated
  currentPage: PageResponse<FieldOfStudy> = new PageResponse<FieldOfStudy>(0, 0, []);
  // basic search criterias (visible if not in 'sub' mode)
  example: FieldOfStudy = new FieldOfStudy();
  /** When 'sub' is true, it means this list is used as a one-to-many list.
   * It belongs to a parent entity, as a result the addNew operation
   * must prefill the parent entity. The prefill is not done here, instead we
   * emit an event. When 'sub' is false, we display basic search criterias
   */
  @Input() sub: boolean;
  @Output() onAddNewClicked = new EventEmitter();

  msgs: Message[] = [];

  constructor(private router: Router,
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
        this.msgs.push({severity:'info', summary:'Deleted OK', detail: 'Angular Rocks!'});
        this.updateVisibility();
      },
      error => this.msgs.push({severity:'error', summary:'Could not delete!', detail: error})
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
      error => this.msgs.push({severity:'error', summary:'Could not get the results!', detail: error})
    );
  }

  addNew() {
    if (this.sub) {
      this.onAddNewClicked.emit("addNew");
    } else {
      this.router.navigate(['/fieldsOfStudies/add']);
    }
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
